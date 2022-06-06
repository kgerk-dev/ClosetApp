package com.example.allin.fragments.add

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.allin.R
import com.example.allin.fragments.update.UpdateFragment
import com.example.allin.model.Clothing
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*



/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {

    /**
     ******* Declare Variables, Buttons, etc. that are used throughout this file. *********
     */

    private lateinit var mClosetViewModel: ClosetViewModel
    lateinit var currentPhotoPath: String
    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 2
    private val REQUEST_PERMISSION = 100
    private lateinit var photoView: ImageView
    private lateinit var imageGallery: ImageButton
    private lateinit var captureImage: ImageButton
    var imageuri: Uri? = null
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Intent>
    private lateinit var captureImageLauncher: ActivityResultLauncher<Array<String>>
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var dateReturned = GregorianCalendar(year, month, day).time

    /**
     *  ADD FRAGMENT view using add_fragment.xml layout --
     *
     *  ONLY BUTTONS AND MENUS DEFINED HERE. Any Functions should be displayed outside of the view code block
     */


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        photoView = view.findViewById(R.id.clothing_item_photo) as ImageView
        imageGallery = view.findViewById(R.id.image_gallery_button) as ImageButton
        captureImage = view.findViewById(R.id.camera_button) as ImageButton

        // Options Menu is True for the page to use Menu Bar to Add Clothing
        setHasOptionsMenu(true)

        //Takes the entered data
        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)

        //these are the camera action buttons when on clicked what they are doing starts to run


        captureImage.apply {
            val packageManager: PackageManager = requireActivity().packageManager

            val captureImageLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    try {
                        imageuri = Uri.parse(currentPhotoPath)
                        photoView.setImageURI(imageuri)
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
                    WRITE_EXTERNAL_STORAGE,
                    CAMERA, READ_EXTERNAL_STORAGE
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
                imageuri = Uri
                photoView.setImageURI(imageuri)

            }

            fun selectImageFromGallery() = selectImageFromGalleryResult.launch(arrayOf("image/*"))

            //New Addition
            setOnClickListener {
                selectImageFromGallery()
            }
        }


        /**
         * **************** CLOTHING TYPE Spinner AND Adapter *********************
         */
        val clothingTypeSpinner: Spinner = view.findViewById(R.id.clothingType_spinner)
        val clothingType = resources.getStringArray(R.array.Clothing_type)
        val clothingType_adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.Clothing_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            //Allows the Spinners to modify their appearance and size using the
            // "layout/spinner_text_settings.xml" file.
            adapter.setDropDownViewResource(R.layout.spinner_text_settings)
        }
        clothingTypeSpinner.adapter = clothingType_adapter

        /**
         * **************** CLOTHING STYLE Spinner AND Adapter *********************
         */
        val clothingStyleSpinner: Spinner = view.clothingStyle_spinner
        val topstyle = resources.getStringArray(R.array.top_style)
        var styleChild_adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.top_style,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            //Allows the Spinners to modify their appearance and size using the
            // "layout/spinner_text_settings.xml" file.
            adapter.setDropDownViewResource(R.layout.spinner_text_settings)
        }
        clothingStyleSpinner.adapter = styleChild_adapter

        /**
         * Clothing Type AND Clothing Style Item Selection Logic
         * Style Options change with Clothing Type Selections
         */

        clothingTypeSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //Toast.makeText(requireContext(), clothingType[position], Toast.LENGTH_SHORT).show()
                when (position) {
                    0 -> {
                        styleChild_adapter = ArrayAdapter.createFromResource(
                            requireContext(),
                            R.array.top_style,
                            android.R.layout.simple_spinner_item
                        ).also { adapter ->
                            //Allows the Spinners to modify their appearance and size using the
                            // "layout/spinner_text_settings.xml" file.
                            adapter.setDropDownViewResource(R.layout.spinner_text_settings)
                        }
                        clothingStyleSpinner.adapter = styleChild_adapter
                    }
                    1 -> {
                        styleChild_adapter = ArrayAdapter.createFromResource(
                            requireContext(),
                            R.array.bottom_style,
                            android.R.layout.simple_spinner_item
                        ).also { adapter ->
                            //Allows the Spinners to modify their appearance and size using the
                            // "layout/spinner_text_settings.xml" file.
                            adapter.setDropDownViewResource(R.layout.spinner_text_settings)
                        }
                        clothingStyleSpinner.adapter = styleChild_adapter
                    }
                    2 -> {
                        styleChild_adapter = ArrayAdapter.createFromResource(
                            requireContext(),
                            R.array.shoes_style,
                            android.R.layout.simple_spinner_item
                        ).also { adapter ->
                            //Allows the Spinners to modify their appearance and size using the
                            // "layout/spinner_text_settings.xml" file.
                            adapter.setDropDownViewResource(R.layout.spinner_text_settings)
                        }
                        clothingStyleSpinner.adapter = styleChild_adapter
                    }
                    3 -> {
                        styleChild_adapter = ArrayAdapter.createFromResource(
                            requireContext(),
                            R.array.outerwear_style,
                            android.R.layout.simple_spinner_item
                        ).also { adapter ->
                            //Allows the Spinners to modify their appearance and size using the
                            // "layout/spinner_text_settings.xml" file.
                            adapter.setDropDownViewResource(R.layout.spinner_text_settings)
                        }
                        clothingStyleSpinner.adapter = styleChild_adapter
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        /**
         * **************** CLOTHING COLOR Spinner and Adapter *********************
         */
        val clothingColorSpinner: Spinner = view.clothingColor_Spinner
        val color = resources.getStringArray(R.array.colors)
        val color_adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.colors,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            //Allows the Spinners to modify their appearance and size using the
            // "layout/spinner_text_settings.xml" file.
            adapter.setDropDownViewResource(R.layout.spinner_text_settings)
        }
        clothingColorSpinner.adapter = color_adapter

        /**
         * **************** CLOTHING COLOR Selection Logic *********************
         */
        clothingColorSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                val colorEntry = parent.selectedItemPosition

                }
                //Toast.makeText(requireContext(), color[position], Toast.LENGTH_SHORT).show()


            override fun onNothingSelected(parent: AdapterView<*>) {
                //No action determined yet for no selection.
            }
        }

        /**
         * **************** CLOTHING BRAND Spinner and Adapter *********************
         */
        val brandSpinner: Spinner = view.clothingBrand_spinner
        val brand = resources.getStringArray(R.array.brands)
        val brandAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.brands,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            //Allows the Spinners to modify their appearance and size using the
            // "layout/spinner_text_settings.xml" file.
            adapter.setDropDownViewResource(R.layout.spinner_text_settings)
        }
        brandSpinner.adapter = brandAdapter

        brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val brandEntry = parent.getItemAtPosition(position).toString()
                //Toast.makeText(requireContext(), brand[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                //No action determined yet for no selection.
            }
        }

        /**
         * **************** CLOTHING THEME Spinner and Adapter *********************
         */
        val themeSpinner: Spinner = view.clothingTheme_spinner
        val themes = resources.getStringArray(R.array.themes)
        val themeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.themes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            //Allows the Spinners to modify their appearance and size using the
            // "layout/spinner_text_settings.xml" file.
            adapter.setDropDownViewResource(R.layout.spinner_text_settings)
        }
        themeSpinner.adapter = themeAdapter

        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val themeEntry = parent.getItemAtPosition(position).toString()
                //Toast.makeText(requireContext(), themes[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        /**
         * **************** DATE ADDED Selection Dialog *********************
         */
        //DatePicker Fragment called from the button labeled on the XML file.
        view.dateAdded_button.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->

                    dateReturned = GregorianCalendar(year, month, day).time
                    //Toast.makeText(requireContext(), "$dateReturned", Toast.LENGTH_SHORT).show()
                    dateAdded_button.text = toSimpleString(dateReturned)

                },
                year,
                month,
                day
            )
            //This is necessary to display the calendar fragment on the current view.
            datePicker.show()
        }



        return view
    }

    /**
     * ************ END OF VIEW  -- Further Down are functions used from buttons and listeners. *******
     */

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //Call the menu you want to use
        inflater.inflate(R.menu.add_clothing_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // If item (Add button) is selected call InsertToDatabase()
        if (item.itemId == R.id.add_clothing_button) {
            insertDataToDatabase()
        }
        return super.onOptionsItemSelected(item)
    }


    //Puts entered data into a variable
    private fun insertDataToDatabase() {
        val type = clothingType_spinner.selectedItem.toString()
        val color = clothingColor_Spinner.selectedItem.toString()
        val style = clothingStyle_spinner.selectedItem.toString()
        val brand = clothingBrand_spinner.selectedItem.toString()
        val theme = clothingTheme_spinner.selectedItem.toString()
        // the value to store the image uri as a string into the database
        val imageString = imageuri.toString()

        val description = description.text.toString()
        //Checks that the fields aren't empty
        if (inputCheck(type, color)) {
            //Create Clothing Object
            val clothing = Clothing(null,
                type,
                color,
                style,
                description,
                dateReturned,
                brand,
                theme,
                imageString
            )
            //Add data to database
            mClosetViewModel.addClothing(clothing)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            //Navigate Back
            //findNavController().navigate(R.id.action_addClothingFragment_to_clothingListFragment)
            findNavController().navigateUp()
        } else {
            Toast.makeText(requireContext(), "Please Fill Out All Fields.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun inputCheck(type: String, color: String): Boolean {
        return !(TextUtils.isEmpty(type) && TextUtils.isEmpty(color))
    }

    // Only used to print the format of Date into a String for users to read.
    @SuppressLint("SimpleDateFormat")
    private fun toSimpleString(date: Date?) = with(date ?: Date()) {
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






