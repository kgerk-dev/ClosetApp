package com.example.allin

import android.app.AlertDialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allin.model.Clothing
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_outfit_display.view.*
import kotlinx.android.synthetic.main.grid_clothing_item.view.*


class OutfitDisplayFragment : Fragment() {

    private val args: OutfitDisplayFragmentArgs by navArgs()
    private lateinit var mClosetViewModel: ClosetViewModel
    private val adapter = OutfitEditAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_outfit_display, container, false)

        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)
        val recyclerView = view.findViewById<RecyclerView>(R.id.selected_outfit_recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val selectedOutfit = args.selectedOutfit
        //Toast.makeText(requireContext(), "Outfit: $selectedOutfit, outfitName: ${selectedOutfit?.outfitName} and OutfitID: ${selectedOutfit?.id}", Toast.LENGTH_SHORT).show()

        view.selected_outfit_name.text = selectedOutfit?.outfitName
        view.selected_outfit_Theme.text = selectedOutfit?.theme

        mClosetViewModel.getAllOutfitsWithClothing(selectedOutfit?.id!!).observe(viewLifecycleOwner, Observer { outfit ->
            adapter.setData(outfit.first().clothingList)
        })

        setHasOptionsMenu(true)
        return view
    }

    //OutfitEdtFragment Options Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_outfit, menu)
    }

    // When User selects the Delete Button it makes a call to the DB DELETE Query
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.delete_outfit) {
            deleteOutfit()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteOutfit() {
        val builder = AlertDialog.Builder(requireContext())
        val outfit = args.selectedOutfit
        builder.setPositiveButton("Yes") { _, _ ->
            outfit?.id?.let { mClosetViewModel.deleteOutfit(it) }
            Toast.makeText(
                requireContext(),
                "Successfully removed Outfit ${outfit?.outfitName}",
                Toast.LENGTH_SHORT
            ).show()
            //Goes back to Outfit List menu. Should remove the OutfitName from the list.
            findNavController().navigate(R.id.action_outfitDetailsFragment_to_outfitListFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${outfit?.outfitName}")
        builder.setMessage("Are you sure you want to delete this outfit?")
        builder.create().show()
    }
}

class OutfitEditAdapter: RecyclerView.Adapter<OutfitEditAdapter.MyViewHolder>() {
    private var clothingList = emptyList<Clothing>()

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
        val currentItem = clothingList[position]
        holder.itemView.gl_optionsBtn.isVisible = false
        holder.itemView.clothing_cb.isVisible = false
        holder.itemView.gl_clothing_type.text = currentItem.type
        holder.itemView.gl_clothing_style.text = currentItem.style
        holder.itemView.gl_clothing_theme.text = currentItem.theme
        holder.itemView.gl_clothing_type.setTextColor(Color.BLUE)
        holder.itemView.gl_clothing_theme.setTextColor(Color.RED)
        holder.itemView.gl_clothing_color.text = currentItem.color
        holder.itemView.gl_clothing_brand.text = currentItem.brand

    }

    override fun getItemCount(): Int {
        return clothingList.size
    }

    fun setData(clothing: List<Clothing>) {
        this.clothingList = clothing
        notifyDataSetChanged()
    }

}