package com.example.allin.fragments.update

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.allin.R
import com.example.allin.model.Clothing
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [UpdateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdateFragment : Fragment() {

    // Each Argument is labeled by their KotlinClass name not the name provided in the nav_graph.xml
    // To find the name in nav_graph.xml, go to Fragment on graph > Attributes Window > Name
    private val args by navArgs<UpdateFragmentArgs>()
    lateinit var currentPhotoPath: String
    private lateinit var mClosetViewModel: ClosetViewModel
    private lateinit var photoView: ImageView
    private lateinit var imageGallery: ImageButton
    private lateinit var captureImage: ImageButton
    private var imageuriUpdate: Uri? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Intent>
    private lateinit var captureImageLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var updatetheme: TextView
    private lateinit var updatecolor: TextView
    private lateinit var updatestyle: TextView
    private lateinit var updatebrand: TextView
    private lateinit var updateClothingtype: TextView
    private lateinit var editColor: ImageButton
    private lateinit var editBrand: ImageButton
    private lateinit var editTheme: ImageButton
    private lateinit var editClothingType: ImageButton
    private lateinit var editStyle: ImageButton
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var dateReturned: Date = GregorianCalendar(year, month, day).time

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)
        photoView = view.findViewById(R.id.updatedclothing_item_photo) as ImageView
        imageGallery = view.findViewById(R.id.updateimage_gallery_button) as ImageButton
        captureImage = view.findViewById(R.id.updatecamera_button) as ImageButton


        val displayDate: Date = args.currentClothing.dateAdded
        toSimpleString(displayDate)

      view.updateType_editText2.setText(args.currentClothing.type)
        view.updateColor2.setText(args.currentClothing.color)
         view.updateStyle2.setText(args.currentClothing.style)
        view.updateDescrip.setText(args.currentClothing.description)
       view.updateBrand_editText2.setText(args.currentClothing.brand)
        view.updateTheme_editText2.setText(args.currentClothing.theme)
        view.updatedclothing_item_photo.setImageURI(Uri.parse(args.currentClothing.image))

        view.update_dateAddedButton.text=(toSimpleString(displayDate))

        imageuriUpdate =  Uri.parse(args.currentClothing.image)

        captureImage.apply {
            val packageManager: PackageManager = requireActivity().packageManager

            val captureImageLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    try {
                        imageuriUpdate = Uri.parse(currentPhotoPath)
                        photoView.setImageURI(imageuriUpdate)
                    } catch (ignored: Exception) {
                    }
                }

            fun launchCameraIntent() {
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    // Ensure that there's a camera activity to handle the intent
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        // Create the File where the photo should go
                        val photoFile: File? = try {
                            createCapturedPhoto()
                        } catch (ex: IOException) {

                            null
                        }
                        // Continue only if the File was successfully created
                        photoFile?.also {
                            val photoURI: Uri = FileProvider.getUriForFile(
                                requireActivity(),
                                "com.example.allin.provider",
                                it
                            )
                            takePictureIntent.putExtra(
                                MediaStore.EXTRA_OUTPUT,
                                photoURI
                            )
                            captureImageLauncher.launch(takePictureIntent)
                        }
                    }
                }
            }

            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { isGranted: Map<String, Boolean> ->
                if (isGranted.containsValue(false)) {
                    //All Permissions are not Granted
                    let {
                        Toast.makeText(
                            requireContext(),
                            "Permissions Not Granted",
                            Toast.LENGTH_LONG
                        )
                    }
                } else {
                    //All Permissions are Granted
                    launchCameraIntent()
                }
            }


            setOnClickListener {
                val requestedPermissions = arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE
                )
                requestPermissionLauncher.launch(requestedPermissions)
            }
        }
        /**
         * Does not work with Permission settings at the moment. When user selects photo from gallery and closes app. App will not reopen
         */


        imageGallery.apply {


            val selectImageFromGalleryResult = registerForActivityResult(
                ActivityResultContracts.OpenDocument()
            ) { Uri ->
                imageuriUpdate = Uri
                photoView.setImageURI(imageuriUpdate)

            }

            fun selectImageFromGallery() = selectImageFromGalleryResult.launch(arrayOf("image/*"))

            //New Addition
            setOnClickListener {
                selectImageFromGallery()
            }
        }


        updateClothingtype= view.findViewById(R.id.updateType_editText2)
        updatestyle = view.findViewById(R.id.updateStyle2)
        editClothingType = view.findViewById(R.id.imageButton)
        editStyle = view.findViewById(R.id.imageButton2)
        editClothingType.setOnClickListener{
            val typepopupMenu: PopupMenu = PopupMenu(requireContext(), editClothingType)
            typepopupMenu.menuInflater.inflate(R.menu.clothingtype_popup_menu,typepopupMenu.menu)
            typepopupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener{ item ->
                when(item.itemId) {
                    R.id.Top -> {
                        updateClothingtype.setText(item.title)
                        Toast.makeText(
                            requireContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                        editStyle.setOnClickListener {
                            val topstylepopupMenu: PopupMenu = PopupMenu(requireContext(), editStyle)
                            topstylepopupMenu.menuInflater.inflate(
                                R.menu.topstyle_popup_menu,
                                topstylepopupMenu.menu
                            )
                            topstylepopupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.ShortSleeve -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.LongSleeve -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.TankTop -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.Blouse -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                                true
                            })
                            topstylepopupMenu.show()
                        }
                    }
                    R.id.Bottom -> {
                        updateClothingtype.setText(item.title)
                        Toast.makeText(
                            requireContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                        editStyle.setOnClickListener {
                            val bottomstylepopupMenu: PopupMenu = PopupMenu(requireContext(), editStyle)
                            bottomstylepopupMenu.menuInflater.inflate(
                                R.menu.bottomstyle_popup_menu,
                                bottomstylepopupMenu.menu
                            )
                            bottomstylepopupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.Pants -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.Shorts -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.Skirt -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.DressPants -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.SweatPants -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                true
                            })
                            bottomstylepopupMenu.show()
                        }
                    }
                    R.id.Shoes -> {
                        updateClothingtype.setText(item.title)
                        Toast.makeText(
                            requireContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                        editStyle.setOnClickListener {
                            val shoestylepopupMenu: PopupMenu = PopupMenu(requireContext(), editStyle)
                            shoestylepopupMenu.menuInflater.inflate(
                                R.menu.shoestyle_popup_menu,
                                shoestylepopupMenu.menu
                            )
                            shoestylepopupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.Sneakers -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.Boots -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.DressShoes -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                true
                            })
                            shoestylepopupMenu.show()
                        }
                    }
                    R.id.OuterWear -> {
                        updateClothingtype.setText(item.title)
                        Toast.makeText(
                            requireContext(),
                            "You Clicked : " + item.title,
                            Toast.LENGTH_SHORT
                        ).show()
                        editStyle.setOnClickListener {
                            val outWearstylepopupMenu: PopupMenu = PopupMenu(requireContext(), editStyle)
                            outWearstylepopupMenu.menuInflater.inflate(
                                R.menu.outerwear_popup_menu,
                                outWearstylepopupMenu.menu
                            )
                            outWearstylepopupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.Coat -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.Jacket -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.Sweater -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    R.id.Hoodie -> {
                                        updatestyle.setText(item.title)
                                        Toast.makeText(
                                            requireContext(),
                                            "You Clicked : " + item.title,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                                true
                            })
                            outWearstylepopupMenu.show()
                        }
                    }
                }
                true
            })
            typepopupMenu.show()
        }



    updatecolor = view.findViewById(R.id.updateColor2)
        editColor = view.findViewById(R.id.imageButton3)
        editColor.setOnClickListener{
            val popupMenu: PopupMenu = PopupMenu(requireContext(), editColor)
            popupMenu.menuInflater.inflate(R.menu.color_popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener{ item ->
                when(item.itemId) {
                    R.id.Grey -> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.White-> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Black ->{
                       updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Brown -> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Red -> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Orange-> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Yellow -> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Green -> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Blue -> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Purple -> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Pink -> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Multicolor -> {
                        updatecolor.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                }
                true
            })
            popupMenu.show()
                }

        updatebrand = view.findViewById(R.id.updateBrand_editText2)
        editBrand = view.findViewById(R.id.imageButton4)
        editBrand.setOnClickListener{
            val brandpopupMenu: PopupMenu = PopupMenu(requireContext(), editBrand)
            brandpopupMenu.menuInflater.inflate(R.menu.brand_popup_menu,brandpopupMenu.menu)
            brandpopupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener{ item ->
                when(item.itemId) {
                    R.id.Nike -> {
                        updatebrand.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Gucci-> {
                        updatebrand.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.UnderArmour ->{
                        updatebrand.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    R.id.Other -> {
                        updatebrand.setText(item.title)
                        Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }

                }
                true
            })
            brandpopupMenu.show()
        }

        updatetheme = view.findViewById(R.id.updateTheme_editText2)
        editTheme = view.findViewById(R.id.imageButton5)
        editTheme.setOnClickListener{
            val themepopupMenu: PopupMenu = PopupMenu(requireContext(), editTheme)
            themepopupMenu.menuInflater.inflate(R.menu.theme_popup_menu,themepopupMenu.menu)
            themepopupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener{ item ->
                when(item.itemId) {
                        R.id.Athletic -> {
                            updatetheme.setText(item.title)
                            Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        }
                        R.id.Casual -> {
                            updatetheme.setText(item.title)
                            Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        }
                        R.id.Business ->{
                            updatetheme.setText(item.title)
                            Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        }
                        R.id.School -> {
                            updatetheme.setText(item.title)
                            Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        }
                        R.id.Swimwear -> {
                            updatetheme.setText(item.title)
                            Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        }
                        R.id.Work -> {
                            updatetheme.setText(item.title)
                            Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        }
                        R.id.LoungeWear -> {
                            updatetheme.setText(item.title)
                            Toast.makeText(requireContext(), "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        }

                }
                true
            })
            themepopupMenu.show()
        }


        view.update_dateAddedButton.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireActivity(), DatePickerDialog.OnDateSetListener {
                        _: DatePicker, year: Int, month: Int, day: Int ->

                    dateReturned = GregorianCalendar(year, month, day).time
                    //Toast.makeText(requireContext(), "$dateReturned", Toast.LENGTH_SHORT).show()
                    update_dateAddedButton.text = toSimpleString(dateReturned)

                }, year, month, day
            )
            //This is necessary to display the calendar fragment on the current view.
            datePicker.show()
        }

        view.upButton.setOnClickListener() {
            updateItem()
        }

        //Add Menu
        setHasOptionsMenu(true)

        return view
    }

    private fun updateItem() {
        val type = updateType_editText2.text.toString()
        val color = updateColor2.text.toString()
        val style = updateStyle2.text.toString()
        val descrip = updateDescrip.text.toString()
        val brand = updateBrand_editText2.text.toString()
        val theme = updateTheme_editText2.text.toString()
        val image2 = imageuriUpdate.toString()



        if (inputCheck(type, color)) {
            //Create Clothing Object
            val updatedClothing = Clothing(args.currentClothing.clothingId, type, color,style, descrip, dateReturned, brand, theme,image2)

            //Update Current Clothing Object
            mClosetViewModel.updateClothing(updatedClothing)
            Toast.makeText(requireContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show()
            //Navigate Back to Clothing List screen
            //findNavController().navigate(R.id.action_updateClothingFragment_to_clothingListFragment)
            findNavController().navigateUp()
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun inputCheck (type: String, color: String): Boolean {
        return !(TextUtils.isEmpty(type) && TextUtils.isEmpty(color))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deleteUser()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mClosetViewModel.deleteClothing(args.currentClothing)
            Toast.makeText(
                requireContext(),
                "Successfully removed: ${args.currentClothing.type}",
                Toast.LENGTH_SHORT).show()
            // On Delete return back to Clothing List screen
            findNavController().navigate(R.id.action_updateClothingFragment_to_clothingListFragment)
        }
        builder.setNegativeButton("No") { _, _ ->}
        builder.setTitle("Delete ${args.currentClothing.type}?")
        builder.setMessage("Are you sure you want to delete ${args.currentClothing.type}?")
        builder.create().show()
    }

    // Only used to print the format of Date into a String for users to read.
    fun toSimpleString(date: Date?) = with(date ?: Date()){
        /**
         * This format can be changed. Use the link below to see the docs.
         * Scroll down to "Date and Time Pattern" Table
         * https://developer.android.com/reference/kotlin/java/text/SimpleDateFormat
         */
        SimpleDateFormat("EEE, MMM d, yyyy").format(this)
    }

    // this is the function to create the path file for the image that gets called in camera
    @Throws(IOException::class)
    private fun createCapturedPhoto(): File {
       val timestamp: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PHOTO_${timestamp}",".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }
}



