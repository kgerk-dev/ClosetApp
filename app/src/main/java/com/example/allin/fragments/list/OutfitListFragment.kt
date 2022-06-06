package com.example.allin.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allin.R
import com.example.allin.model.Clothing
import com.example.allin.model.FavoriteClothingCrossRef
import com.example.allin.model.Outfit
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_add_outfit.view.*
import kotlinx.android.synthetic.main.fragment_clothing_list.view.*
import kotlinx.android.synthetic.main.fragment_outfit_list.view.*
import kotlinx.android.synthetic.main.fragment_outfit_list.view.total_items
import kotlinx.android.synthetic.main.grid_clothing_item.view.*
import kotlinx.android.synthetic.main.outfit_custom_row.view.*

class OutfitListFragment : Fragment() {

    private lateinit var mClosetViewModel: ClosetViewModel
    private lateinit var adapter: OutfitAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_outfit_list, container, false)

        adapter = OutfitAdapter(requireParentFragment())
        //RecyclerView standard Layout for now.
        val recyclerView = view.outfit_recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //OutfitViewModel
        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)
        mClosetViewModel.readAllOutfitData.observe(viewLifecycleOwner, Observer { outfit ->
            adapter.setData(outfit)
            view.total_items.text = "Outfits Created: ${adapter.itemCount}"
        })

        //Add button adds new Outfit Item.
        view.add_outfit_btn.setOnClickListener{
            findNavController().navigate(R.id.action_outfitListFragment_to_addOutfitFragment)
        }
        //Add menu
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            deleteAllUsers()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUsers() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mClosetViewModel.deleteAllOutfits()
            Toast.makeText(
                requireContext(),
                "Successfully removed all Outfits.",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No") { _, _ ->}
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to delete all Outfits")
        builder.create().show()
    }
}

class OutfitAdapter(private val fragment: Fragment): RecyclerView.Adapter<OutfitAdapter.MyViewHolder>(){

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
        holder.itemView.outfit_row_card.setOnClickListener {
            val action = OutfitListFragmentDirections.actionOutfitListFragmentToOutfitEditFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

        holder.itemView.outfit_options_button.setOnClickListener {
            val cardOptionsBtn = holder.itemView.outfit_options_button
            popUpMenuOptionsSelected(cardOptionsBtn, fragment, currentItem)
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
        currentItem: Outfit
    ) {
        val popupMenu: PopupMenu = PopupMenu(fragment.requireContext(), cardOptionsBtn)
        popupMenu.menuInflater.inflate(R.menu.outfit_menu_popup,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete_outfit -> {
                    currentItem.id?.let { mClosetViewModel.deleteOutfit(it) }
                    Toast.makeText(fragment.requireContext(), "You deleted ${currentItem.outfitName}: the current outfits ", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }
}