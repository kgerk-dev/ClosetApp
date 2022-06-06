package com.example.allin

import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allin.model.Outfit
import com.example.allin.model.PackingWithOutfitsTable
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_packing_list_choose_outfits.view.*
import kotlinx.android.synthetic.main.outfit_custom_row.view.*

class SelectOutfitsForPackingList : Fragment() {

    private val args: SelectOutfitsForPackingListArgs by navArgs()

    private lateinit var mClosetViewModel: ClosetViewModel
    private lateinit var adapter: SelectOutfitsForPackingAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_packing_list_choose_outfits, container, false)

        view.packing_list_title.text = args.newListName

        recyclerView = view.packing_outfits_rv

        adapter = SelectOutfitsForPackingAdapter(requireParentFragment(), args.newListName)

        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //OutfitViewModel
        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)

        mClosetViewModel.readAllOutfitData.observe(viewLifecycleOwner, Observer { outfit ->
            adapter.setData(outfit)
        })

        return view
    }
}

class SelectOutfitsForPackingAdapter(
    private val fragment: Fragment,
    private val listName: String
): RecyclerView.Adapter<SelectOutfitsForPackingAdapter.MyViewHolder>(){
    /**
     * Outfits will be stored into the Outfit Table in our DB.
     */
    private var outfitList = emptyList<Outfit>()
    private val mClosetViewModel: ClosetViewModel = ViewModelProvider(fragment).get(ClosetViewModel::class.java)

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val outfitName: TextView = itemView.OutfitName
        val outfitTheme: TextView = itemView.outfit_theme
    }
    //Inflates view where the objects will be stored. XML that displays items in the row inside the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.outfit_custom_row, parent, false)
        )
    }
    // For testing purposes, only the OutFit name will be displayed until we build the Standard
    // Update, Delete, Insert Queries
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = outfitList[position]
        holder.itemView.outfit_cb.isVisible = false
        holder.outfitName.text = currentItem.outfitName
        holder.outfitTheme.text = currentItem.theme

        //When user selects options button. Popup menu of items becomes visible.
        holder.itemView.outfit_options_button.setOnClickListener{
            val cardOptionsBtn = holder.itemView.outfit_options_button
            popUpMenuOptionsSelected(cardOptionsBtn, fragment, currentItem, listName,)
        }
        //When user selects the cardview the user can view the outfit and Clothes in the outfit.
        holder.itemView.outfit_row_card.setOnClickListener {
            val action = SelectOutfitsForPackingListDirections.actionPackingListChooseOutfitsToPreviewOutfitWithClothing(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return outfitList.size
    }

    fun setData(outfit: List<Outfit>) {
        this.outfitList = outfit
        notifyDataSetChanged()
    }

    private fun popUpMenuOptionsSelected(
        cardOptionsBtn: ImageButton,
        fragment: Fragment,
        currentItem: Outfit,
        listName: String
    ) {
        val popupMenu: PopupMenu = PopupMenu(fragment.requireContext(), cardOptionsBtn)
        popupMenu.menuInflater.inflate(R.menu.packing_list_popup_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.add_outfit_to_list -> {
                    //Initializes the new Packing List. from Packing List get query
                    val packingList = mClosetViewModel.getPackingList(listName)
                    //Sets a conditional to handle to Null-Safe call
                    if(packingList.id != null  && currentItem.id != null){
                        mClosetViewModel.addPackingWithOutfits(PackingWithOutfitsTable(packingList.id, currentItem.id))
                    }
                    Toast.makeText(fragment.requireContext(), "You added ${currentItem.outfitName}: to the Favorites List ", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }

}