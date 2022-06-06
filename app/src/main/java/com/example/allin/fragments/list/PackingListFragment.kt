package com.example.allin

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allin.model.Packing
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_packing_list.view.*
import kotlinx.android.synthetic.main.packing_list_row.view.*

class PackingListFragment : Fragment() {

    private lateinit var mClosetViewModel: ClosetViewModel
    private lateinit var adapter: PackingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_packing_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.packing_list_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PackingListAdapter(requireParentFragment())
        recyclerView.adapter = adapter

        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)
        mClosetViewModel.readAllPackingData.observe(viewLifecycleOwner, Observer { data ->
            adapter.setData(data)
        })

        view.packingList_floating_btn.setOnClickListener {
            // Add new PackingList Items
            findNavController().navigate(R.id.action_travelListFragment_to_addPackingListItemFragment)
            Toast.makeText(
                requireContext(),
                "Adding New Packing List Item",
                Toast.LENGTH_SHORT
            ).show()

        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    // When User selects the Delete Button it makes a call to the DB DELETE Query
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> deleteAllUsers()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUsers() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mClosetViewModel.deleteAllPackingList()
            Toast.makeText(
                requireContext(),
                "Successfully removed everything.",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete everything?")
        builder.setMessage("Are you sure you want to delete everything?")
        builder.create().show()
    }



}

class PackingListAdapter(private val fragment: Fragment) : RecyclerView.Adapter<PackingListAdapter.MyViewHolder>() {

    private var packingList = emptyList<Packing>()
    private val mClosetViewModel: ClosetViewModel = ViewModelProvider(fragment).get(ClosetViewModel::class.java)

    inner class MyViewHolder(item: View): RecyclerView.ViewHolder(item){

    }

    //This inflates the EXACT SAME LAYOUT as ClothingList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.packing_list_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = packingList[position]
        holder.itemView.packing_list_name.text = currentItem.packingListName
        holder.itemView.packing_list_options_btn.setOnClickListener {
            val cardOptionsBtn = holder.itemView.packing_list_options_btn
            popUpMenuOptionsSelected(cardOptionsBtn, fragment, currentItem)
        }

        holder.itemView.packing_list_row.setOnClickListener {
            val action = PackingListFragmentDirections.actionTravelListFragmentToPackingListDisplayFragment(currentItem)
            holder.itemView.findNavController().navigate(action)

        }

    }

    override fun getItemCount(): Int {
        return packingList.size
    }

    fun setData(packingList: List<Packing>) {
        this.packingList = packingList
        notifyDataSetChanged()
    }

    private fun popUpMenuOptionsSelected(
        cardOptionsBtn: ImageButton,
        fragment: Fragment,
        currentItem: Packing
    ) {
        val popupMenu: PopupMenu = PopupMenu(fragment.requireContext(), cardOptionsBtn)
        popupMenu.menuInflater.inflate(R.menu.pl_delete_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.packing_list_delete -> {
                    mClosetViewModel.deletePackingList(currentItem)
                    Toast.makeText(fragment.requireContext(), "You deleted ${currentItem.packingListName}: from the list ", Toast.LENGTH_SHORT).show()
                    }
            }
            true
        }
        popupMenu.show()
    }

}