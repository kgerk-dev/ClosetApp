package com.example.allin.fragments.add

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.allin.R
import com.example.allin.model.Outfit
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_add_outfit.*
import kotlinx.android.synthetic.main.fragment_add_outfit.view.*

/**
 */
class AddOutfitFragment : Fragment() {

    //Define any buttons you have here, to help keep track of them.
    private lateinit var outfitName: EditText
    private lateinit var addOutfitBtn: Button
    private lateinit var addThemeBtn: Button
    private lateinit var themeTitle: TextView

    private lateinit var mClosetViewModel: ClosetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_outfit, container, false)

        outfitDirections()

        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)

        outfitName = view.findViewById(R.id.outfit_name_et)
        addOutfitBtn = view.findViewById(R.id.add_new_outfit_btn)
        addThemeBtn = view.add_theme
        themeTitle = view.outfit_theme_TV

        addThemeBtn.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(requireContext(), addThemeBtn)
            popupMenu.menuInflater.inflate(R.menu.theme_popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.Athletic -> {
                        themeTitle.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Casual -> {
                        themeTitle.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Business ->{
                        themeTitle.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.School -> {
                        themeTitle.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Swimwear -> {
                        themeTitle.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Work -> {
                        themeTitle.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.LoungeWear -> {
                        themeTitle.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                }
                true
            })
            popupMenu.show()
        }

        addOutfitBtn.setOnClickListener {
            val string: String = outfitName.text.toString()
            if((string.isNotEmpty())) {
                Toast.makeText(requireContext(), "Name Confirmed", Toast.LENGTH_SHORT).show()
                val name = outfit_name_et.text.toString()
                val theme = themeTitle.text.toString()
                val outfit = Outfit(null, name, theme)

                mClosetViewModel.addOutfit(outfit)

                findNavController().navigate(AddOutfitFragmentDirections.actionAddOutfitFragmentToAddClothingToOutfits(outfit, null, null, null, null))
                //findNavController().navigate(R.id.action_addOutfitFragment_to_addClothingToOutfits)
            } else {
                Toast.makeText(requireContext(), "Please enter an outfit name", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun outfitDirections() {
        val outfitHelp = AlertDialog.Builder(requireContext())
        outfitHelp.setPositiveButton("Create") { _, _ ->
            Toast.makeText(requireContext(), "Create Outfit!", Toast.LENGTH_SHORT).show()
        }
        //outfitHelp.setNegativeButton("No") { _, _ -> findNavController().navigate(R.id.action_addOutfitFragment_to_outfitListFragment)}
        outfitHelp.setTitle("Create New Outfit?")
        outfitHelp.setMessage("Follow the Steps Below:\n" +
                "1. Enter A Name For Your New Outfit\n" +
                "2. Click Submit When You Are Ready\n")
        outfitHelp.create().show()
    }
}