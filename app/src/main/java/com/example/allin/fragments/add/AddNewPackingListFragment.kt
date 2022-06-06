package com.example.allin.fragments.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.allin.R
import com.example.allin.model.Packing
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_add_packing_list_item.*


class AddNewPackingListFragment : Fragment() {


    private lateinit var mClosetViewModel: ClosetViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_packing_list_item, container, false)
        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)
        setHasOptionsMenu(true)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_packing_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_packing_item){
            val packingListName: String = new_packing_list_name.text.toString()

            val packingItem = Packing(null, packingListName)

            mClosetViewModel.addNewPackingList(packingItem)

            val action = AddNewPackingListFragmentDirections.actionAddPackingListItemFragmentToPackingListChooseOutfits(packingListName)
            findNavController().navigate(action)
        }
        return super.onOptionsItemSelected(item)
    }
}