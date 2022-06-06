package com.example.allin

import android.app.AlertDialog
import android.graphics.Color
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
import kotlinx.android.synthetic.main.fragment_outfit_display.view.*
import kotlinx.android.synthetic.main.grid_clothing_item.view.*

class PreviewOutfitWithClothing : Fragment() {

    private val args: PreviewOutfitWithClothingArgs by navArgs()
    private lateinit var mClosetViewModel: ClosetViewModel
    private val adapter = PreviewOutfitWithClothingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_preview_outfit_with_clothing, container, false)

        // Inflate the layout for this fragment
        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)
        val recyclerView = view.findViewById<RecyclerView>(R.id.selected_packing_outfit_rv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val selectedOutfit = args.selectedOutfit
        //Toast.makeText(requireContext(), "Outfit: $selectedOutfit, outfitName: ${selectedOutfit?.outfitName} and OutfitID: ${selectedOutfit?.id}", Toast.LENGTH_SHORT).show()

        mClosetViewModel.getAllOutfitsWithClothing(selectedOutfit?.id!!).observe(viewLifecycleOwner, Observer { outfit ->
            adapter.setData(outfit.first().clothingList)
        })

        return view
    }
}

class PreviewOutfitWithClothingAdapter: RecyclerView.Adapter<PreviewOutfitWithClothingAdapter.MyViewHolder>() {
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
        holder.itemView.clothing_cb.isVisible = false
        holder.itemView.gl_optionsBtn.isVisible = false
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