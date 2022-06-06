package com.example.allin

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allin.data.relations.OutfitsWithClothingList
import com.example.allin.model.Clothing
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_add_clothing_to_outfits.view.*
import kotlinx.android.synthetic.main.fragment_packing_list_display.view.*
import kotlinx.android.synthetic.main.grid_clothing_item.view.*
import kotlinx.android.synthetic.main.outfit_clothing_list_row.view.*

class PackingListDisplayFragment : Fragment() {

    private val args: PackingListDisplayFragmentArgs by navArgs()
    private lateinit var mClosetViewModel: ClosetViewModel
    private val adapter = PackingListDisplayAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_packing_list_display, container, false)

        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.pl_display_rv)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val packingList = args.selectedPackingList?.id
        //Toast.makeText(requireContext(), "Outfit: $selectedOutfit, outfitName: ${selectedOutfit?.outfitName} and OutfitID: ${selectedOutfit?.id}", Toast.LENGTH_SHORT).show()

        view.packing_list_display_title.text = args.selectedPackingList?.packingListName

        mClosetViewModel.getPackingListWithOutfits(packingList!!).observe(viewLifecycleOwner, Observer { list ->
            adapter.setData(list.first().packedOutfitList)
        })

        return view
    }


}

class PackingListDisplayAdapter: RecyclerView.Adapter<PackingListDisplayAdapter.MyViewHolder>() {
    private var list = emptyList<OutfitsWithClothingList>()

    inner class MyViewHolder(item: View): RecyclerView.ViewHolder(item){

    }

    //This inflates the EXACT SAME LAYOUT as ClothingList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.outfit_clothing_list_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = list[position]
        holder.itemView.outfit_cb.isVisible = false
        holder.itemView.outfit_options_button.isVisible = false
        holder.itemView.outfit_name.text = currentItem.Outfit.outfitName
        holder.itemView.outfit_theme.text = currentItem.Outfit.theme


        holder.itemView.outfit_row_card.setOnClickListener{

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<OutfitsWithClothingList>) {
        this.list = list
        notifyDataSetChanged()
    }

}