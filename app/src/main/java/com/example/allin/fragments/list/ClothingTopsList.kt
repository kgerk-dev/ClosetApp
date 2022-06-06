package com.example.allin


import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allin.model.Clothing
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_clothing_tops_list.view.*
import kotlinx.android.synthetic.main.grid_clothing_item.view.*


class ClothingTopsList : Fragment() {

    val args: ClothingTopsListArgs by navArgs()

    /**
     * Use this to get the query form Database of Tops
     */
    private lateinit var mClosetViewModel: ClosetViewModel

    private var adapter = ClothingTopsAdapter()

    //This class should only display Clothing Tops in a RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clothing_tops_list, container, false)
        //instantiate the recyclerView
        val recyclerView = view.clothing_top_rv
        //asssign the adapter
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        //Assign the correct data of Tops to the adapter of the RecyclerView
        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)
        mClosetViewModel.selectAllTops(args.currentOutfit.theme).observe(viewLifecycleOwner, Observer { tops ->
            adapter.setData(tops)
            }
        )
        //If Item was selected. Call navController
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_outfits_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_clothing_to_outfit_button){
            if(adapter.isSelected) {
                val selectedDialog = AlertDialog.Builder(this.requireContext())
                selectedDialog.setPositiveButton("Yes") { _, _ ->
                    val action = ClothingTopsListDirections.actionClothingTopsListToAddClothingToOutfits(args.currentOutfit,adapter.selectedItem,args.currentBottom,args.currentShoes, args.currentOuterWear)
                    findNavController().navigate(action)
                }
                selectedDialog.setNegativeButton("No") { _, _ -> }
                val temp = adapter.selectedItem.type
                selectedDialog.setTitle("Add $temp to the outfit?")
                Toast.makeText(this.requireContext(), "Added to Outfit", Toast.LENGTH_SHORT).show()
                selectedDialog.create().show()

            } else {
                Toast.makeText(this.requireContext(), "Please select an Item", Toast.LENGTH_LONG).show()
            }

        }
        return super.onOptionsItemSelected(item)
    }

}

/**
 * This page consists of all code for the RecyclerView of Clothing Tops for selection only to add to outfits.
 */
class ClothingTopsAdapter : RecyclerView.Adapter<ClothingTopsAdapter.MyViewHolder>() {
    private var clothingTopList = emptyList<Clothing>()
    lateinit var selectedItem: Clothing
    var isSelected = false
    inner class MyViewHolder(item: View): RecyclerView.ViewHolder(item){
        var checkBox: CheckBox = item.findViewById(R.id.clothing_cb)
    }

    //This inflates the EXACT SAME LAYOUT as ClothingList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.grid_clothing_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = clothingTopList[position]
        holder.itemView.gl_optionsBtn.isVisible = false
        holder.itemView.gl_clothing_type.text = currentItem.type
        holder.itemView.gl_clothing_style.text = currentItem.style
        holder.itemView.gl_clothing_theme.text = currentItem.theme
        holder.itemView.gl_clothing_type.setTextColor(Color.BLUE)
        holder.itemView.gl_clothing_theme.setTextColor(Color.RED)
        holder.itemView.gl_clothing_color.text = currentItem.color
        holder.itemView.gl_clothing_brand.text = currentItem.brand
        //holder.itemView.gl_clothing_item_photo.setImageURI( Uri.parse(currentItem.image))

        holder.itemView.grid_item.setOnClickListener {
            if (!holder.itemView.clothing_cb.isChecked){
                selectedItem = currentItem
                holder.itemView.clothing_cb.isChecked = true
                isSelected = true
            }else {
                holder.itemView.clothing_cb.isChecked = false
                isSelected = false
            }
        }
    }

    override fun getItemCount(): Int {
        return clothingTopList.size
    }

    fun setData(clothing: List<Clothing>) {
        this.clothingTopList = clothing
        notifyDataSetChanged()
    }

}