package com.example.allin


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.allin.model.Clothing
import com.example.allin.viewmodel.ClosetViewModel
import kotlinx.android.synthetic.main.fragment_favorite_list.view.*
import kotlinx.android.synthetic.main.fragment_favorite_list.view.total_items
import kotlinx.android.synthetic.main.grid_clothing_item.view.*


class FavoriteListFragment : Fragment() {

    private var adapter = FavoriteListAdapter()
    private lateinit var favRecyclerView: RecyclerView
    private lateinit var mClosetViewModel: ClosetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite_list, container, false)

        mClosetViewModel = ViewModelProvider(this).get(ClosetViewModel::class.java)

        favRecyclerView = view.fav_list_rv
        favRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        favRecyclerView.adapter = adapter

        mClosetViewModel.getFavoritesClothingItemsList().observe(viewLifecycleOwner, Observer { favClothing ->
            adapter.setData(favClothing.first().favorites)
            view.total_items.text = "Number of Favorite Clothes: ${adapter.itemCount}"
        })

        return view
    }

}

class FavoriteListAdapter : RecyclerView.Adapter<FavoriteListAdapter.MyViewHolder>() {

    private var favClothingList = emptyList<Clothing>()

    inner class MyViewHolder(item: View): RecyclerView.ViewHolder(item){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.grid_clothing_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = favClothingList[position]
        holder.itemView.gl_clothing_type.text = currentItem.type
        holder.itemView.gl_clothing_style.text = currentItem.style
        holder.itemView.gl_clothing_theme.text = currentItem.theme
        holder.itemView.gl_clothing_color.text = currentItem.color
        holder.itemView.gl_clothing_brand.text = currentItem.brand
        holder.itemView.clothing_cb.isVisible = false
        holder.itemView.gl_optionsBtn.isVisible = false
        //holder.itemView.gl_clothing_item_photo.setImageURI( Uri.parse(currentItem.image))

    }

    override fun getItemCount(): Int {
        return favClothingList.size
    }

    fun setData(clothing: List<Clothing>) {
        this.favClothingList = clothing
        notifyDataSetChanged()
    }

}