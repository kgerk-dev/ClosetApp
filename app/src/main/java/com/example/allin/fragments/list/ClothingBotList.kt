package com.example.allin.fragments.list

import android.app.AlertDialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allin.ClothingTopsListDirections
import com.example.allin.R
import com.example.allin.model.Clothing
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_clothing_bot_list.view.*
import kotlinx.android.synthetic.main.grid_clothing_item.view.*

class ClothingBotList : Fragment() {

    val args: ClothingBotListArgs by navArgs()
    private lateinit var mClosetViewModel: ClosetViewModel

    private var adapter = ClothingBotAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clothing_bot_list, container, false)
        setHasOptionsMenu(true)

        val recyclerView = view.clothing_bot_rv

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)
        mClosetViewModel.selecAllBottoms(args.currentOutfit.theme).observe(viewLifecycleOwner, Observer { bots ->
            adapter.setData(bots)
        })

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
                    val action = ClothingBotListDirections.actionClothingBotListToAddClothingToOutfits(
                        args.currentOutfit,
                        args.currentOutfitTop,
                        adapter.selectedItem,
                        args.currentShoes,
                        args.currentOuterWear
                    )
                    findNavController().navigate(action)
                }
                selectedDialog.setNegativeButton("No") { _, _ -> }
                val temp = adapter.selectedItem.type
                selectedDialog.setTitle("Add $temp to the outfit?")
                Toast.makeText(this.requireContext(), "Added to Outfit", Toast.LENGTH_SHORT).show()
                selectedDialog.create().show()
            }else {
                Toast.makeText(this.requireContext(), "Please select an Item", Toast.LENGTH_LONG).show()
            }

        }
        return super.onOptionsItemSelected(item)
    }

}

class ClothingBotAdapter(): RecyclerView.Adapter<ClothingBotAdapter.MyViewHolder>() {
    private var clothingBotsList = emptyList<Clothing>()
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
        val currentItem = clothingBotsList[position]

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
        return clothingBotsList.size
    }

    fun setData(clothing: List<Clothing>) {
        this.clothingBotsList = clothing
        notifyDataSetChanged()
    }

}
