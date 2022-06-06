package com.example.allin.fragments.add

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.allin.R
import com.example.allin.model.OutfitClothingTable
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_add_clothing_to_outfits.view.*
import java.lang.Exception


class AddClothingToOutfits : Fragment() {

    private val args by navArgs<AddClothingToOutfitsArgs>()
    //This argument is carrying a value of Outfit(id, outfitName)
    private lateinit var addTopButton: Button
    private lateinit var addBotButton: Button
    private lateinit var addShoesButton: Button
    private lateinit var addOuterWearButton: Button


    private lateinit var mClosetViewModel: ClosetViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_clothing_to_outfits, container, false)
        //We have the argument of Outfit, Set it as the Outfit Title Name so the user, can set those values.
        //createOutfitInstructions()
        setHasOptionsMenu(true)

        //Takes the entered data
        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)

        /**
         * Displays Outfit Title
         */
        view.outfit_title.setText(args.currentOutfit.outfitName)

        /**
         * Displays Top Attributes when selected
         */
        if(args.clothingTop != null) {
            view.top_type_chip.setText(args.clothingTop?.type)
            view.top_style_chip.setText(args.clothingTop?.style)
            view.top_color_chip.setText(args.clothingTop?.color)
            view.top_theme_chip.setText(args.clothingTop?.theme)
            if(args.clothingTop?.image != null) {
                view.outfit_top_img.setImageURI(Uri.parse(args.clothingTop?.image))
            } else {
                view.outfit_top_img.isVisible = false
            }
        } else {
            view.top_type_chip.isVisible = false
            view.top_style_chip.isVisible = false
            view.top_color_chip.isVisible = false
            view.top_theme_chip.isVisible = false
            view.outfit_top_img.setImageResource(R.drawable.no_image)
        }

        //Set the Clothing Tops Button
        addTopButton = view.outfit_add_top_btn
        addTopButton.setOnClickListener{
            //Navigate to the RecyclerView displaying only Clothing Tops
            findNavController().navigate(AddClothingToOutfitsDirections.actionAddClothingToOutfitsToClothingTopsList(args.currentOutfit,args.clothingTop,args.clothingBottom,args.currentShoes, args.currentOuterWear))
        }

        /**
         * Display Clothing Bottom Attributes when selected
         */
        if(args.clothingBottom != null){
            view.bottom_type_chip.setText(args.clothingBottom?.type)
            view.bot_style_chip.setText(args.clothingBottom?.style)
            view.bot_color_chip.setText(args.clothingBottom?.color)
            view.bot_theme_chip.setText(args.clothingBottom?.theme)
            if (args.clothingBottom?.image != null) {
                view.outfit_bot_img.setImageURI(Uri.parse(args.clothingBottom?.image))
            }
        } else {
            view.bottom_type_chip.isVisible = false
            view.bot_style_chip.isVisible = false
            view.bot_color_chip.isVisible = false
            view.bot_theme_chip.isVisible = false
            view.outfit_bot_img.setImageResource(R.drawable.no_image)
        }

        addBotButton = view.outfit_add_bot_btn
        addBotButton.setOnClickListener {
            findNavController().navigate(AddClothingToOutfitsDirections.actionAddClothingToOutfitsToClothingBotList(args.currentOutfit,args.clothingTop,args.clothingBottom,args.currentShoes, args.currentOuterWear))
        }

        /**
         * Display Clothing Shoes Attributes when selected
         */


        if (args.currentShoes != null){
            view.shoes_type_chip.setText(args.currentShoes?.type)
            view.shoes_style_chip.setText(args.currentShoes?.style)
            view.shoes_color_chip.setText(args.currentShoes?.color)
            view.shoes_theme_chip.setText(args.currentShoes?.theme)
            if(args.currentShoes?.image != null){
                view.outfit_shoes_img.setImageURI(Uri.parse(args.currentShoes?.image))
            }
        } else {
            view.shoes_type_chip.isVisible = false
            view.shoes_style_chip.isVisible = false
            view.shoes_color_chip.isVisible = false
            view.shoes_theme_chip.isVisible = false
            view.outfit_shoes_img.setImageResource(R.drawable.no_image)
        }

        addShoesButton = view.outfit_add_shoes_btn
        addShoesButton.setOnClickListener {
            findNavController().navigate(AddClothingToOutfitsDirections.actionAddClothingToOutfitsToClothingShoesList(args.currentOutfit,args.clothingTop,args.clothingBottom,args.currentShoes, args.currentOuterWear))
        }

        /**
         * Display Clothing OuterWear Attributes when selected
         */
        if(args.currentOuterWear != null){
            view.outerWear_type_chip.setText(args.currentOuterWear?.type)
            view.outerWear_style_chip.setText(args.currentOuterWear?.style)
            view.outerWear_color_chip.setText(args.currentOuterWear?.color)
            view.outerWear_theme_chip.setText(args.currentOuterWear?.theme)
            if(args.currentOuterWear?.image != null){
                view.outfit_outerwear_img.setImageURI(Uri.parse(args.currentOuterWear?.image))
            }
        } else {
            view.outerWear_type_chip.isVisible = false
            view.outerWear_style_chip.isVisible = false
            view.outerWear_color_chip.isVisible = false
            view.outerWear_theme_chip.isVisible = false
            view.outfit_outerwear_img.setImageResource(R.drawable.no_image)
        }

        addOuterWearButton = view.outfit_add_outerwear_btn
        addOuterWearButton.setOnClickListener {
            findNavController().navigate(AddClothingToOutfitsDirections.actionAddClothingToOutfitsToClothingOuterWearList(args.currentOutfit,args.clothingTop,args.clothingBottom,args.currentShoes, args.currentOuterWear))
        }

        return view
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_outfits_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // If item (Add button) is selected call InsertToDatabase()
        if (item.itemId == R.id.add_clothing_to_outfit_button) {
            insertDataToDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDatabase() {

        //Add logic before adding to Database
            val selectedDialog = AlertDialog.Builder(this.requireContext())
            selectedDialog.setPositiveButton("Yes") { _, _ ->
                //Add to the DB then return to main Outfit Page
                val outfit1 = mClosetViewModel.getOutfit(args.currentOutfit.outfitName)
                val clothingTop = args.clothingTop?.clothingId
                val clothingBottom = args.clothingBottom?.clothingId
                val clothingShoes = args.currentShoes?.clothingId
                val clothingOuterWear = args.currentOuterWear?.clothingId

                Toast.makeText(requireContext(), "${outfit1.outfitName}", Toast.LENGTH_SHORT).show()

                if(outfit1.id != null && clothingTop != null){
                    mClosetViewModel.addOutfitWithClothingMap(OutfitClothingTable(outfit1.id, clothingTop))
                }

                if(outfit1.id != null && clothingBottom != null){
                    mClosetViewModel.addOutfitWithClothingMap(OutfitClothingTable(outfit1.id, clothingBottom))
                }

                if(outfit1.id != null && clothingShoes != null){
                    mClosetViewModel.addOutfitWithClothingMap(OutfitClothingTable(outfit1.id, clothingShoes))
                }

                if(outfit1.id != null && clothingOuterWear != null){
                    mClosetViewModel.addOutfitWithClothingMap(OutfitClothingTable(outfit1.id, clothingOuterWear))
                }

                Toast.makeText(
                    requireContext(),
                    "Successfully created outfit.",
                    Toast.LENGTH_SHORT
                ).show()
                //Navigate Back
                findNavController().navigate(R.id.action_addClothingToOutfits_to_outfitListFragment)

            }
            selectedDialog.setNegativeButton("No") { _, _ -> }
            selectedDialog.setTitle("Create Outfit?")
            selectedDialog.create().show()
    }

    private fun createOutfitInstructions() {
        val outfitHelp = AlertDialog.Builder(requireContext())
        outfitHelp.setPositiveButton("Okay") { _, _ ->
            Toast.makeText(requireContext(), "Create Outfit!", Toast.LENGTH_SHORT).show()
        }
        outfitHelp.setNegativeButton("No") { _, _ ->
            // Once we have a delete button, well delete the Outfit from the DB if the user chooses not to create an Outfit.
            // deleteOutfit()
            //findNavController().navigate(R.id.action_addClothingToOutfits_to_outfitListFragment)
        }
        outfitHelp.setTitle("Create New Outfit?")
        outfitHelp.setMessage("Follow the Steps Below:\n" +
                "1. Click the Add Buttons of each clothing Type you wish to add.\n" +
                "2. Click Submit When You Are Ready\n")
        outfitHelp.create().show()
    }
}