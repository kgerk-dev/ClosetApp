package com.example.allin.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.allin.R
import com.example.allin.model.Clothing
import com.example.allin.model.FavoriteClothingCrossRef
import com.example.allin.model.Favorites
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.grid_clothing_item.view.*

class ListAdapter(
    private val fragment: Fragment
): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var clothingList = emptyList<Clothing>()
    private val mClosetViewModel: ClosetViewModel = ViewModelProvider(fragment).get(ClosetViewModel::class.java)
    private lateinit var clothingSelected: Clothing
    private val favorite = mClosetViewModel.favList

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    //Inflates view where the objects will be stored. XML that displays items in the row inside the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.grid_clothing_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = clothingList[position]
        holder.itemView.clothing_cb.isVisible = false
        holder.itemView.gl_clothing_type.text = currentItem.type
        holder.itemView.gl_clothing_color.text = currentItem.color
        holder.itemView.gl_clothing_theme.text = currentItem.theme
        holder.itemView.gl_clothing_style.text = currentItem.style
        holder.itemView.gl_clothing_brand.text = currentItem.brand
        //holder.itemView.gl_clothing_item_photo.setImageURI( Uri.parse(currentItem.image))
        holder.itemView.grid_item.setOnClickListener {
            val action = ListFragmentDirections.actionClothingListFragmentToUpdateClothingFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

        holder.itemView.grid_item.setOnLongClickListener {
            holder.itemView.grid_item.setCardBackgroundColor(808080)
            holder.itemView.grid_item.wornStatus.text = "Worn"
            return@setOnLongClickListener true
        }
        holder.itemView.gl_optionsBtn.setOnClickListener {
            val cardOptionsBtn = holder.itemView.gl_optionsBtn
            popUpMenuOptionsSelected(cardOptionsBtn, fragment, currentItem)
        }
    }

    override fun getItemCount(): Int {
        return clothingList.size
    }

    fun setData(clothing: List<Clothing>) {
        this.clothingList = clothing
        notifyDataSetChanged()
    }

    private fun popUpMenuOptionsSelected(
        cardOptionsBtn: ImageButton,
        fragment: Fragment,
        currentItem: Clothing
    ) {
        val popupMenu: PopupMenu = PopupMenu(fragment.requireContext(), cardOptionsBtn)
        popupMenu.menuInflater.inflate(R.menu.grid_clothing_popup,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.addToFavMenuItem -> {
                    if(favorite.favClothingId != null && currentItem.clothingId != null) {
                        val newFavoriteClothing = FavoriteClothingCrossRef(favorite.favClothingId, currentItem.clothingId)
                        mClosetViewModel.addFavClothingToList(newFavoriteClothing)
                    }
                    Toast.makeText(fragment.requireContext(), "You added ${currentItem.type}: to the Favorites List ", Toast.LENGTH_SHORT).show()
                }
                R.id.deleteClothingItem -> {
                    mClosetViewModel.deleteClothing(currentItem)
                    Toast.makeText(fragment.requireContext(), "You Deleted ${currentItem.type}: from your closet List ", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popupMenu.show()
    }
}