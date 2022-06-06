package com.example.allin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.allin.data.ClothingDatabase
import com.example.allin.data.relations.FavoritesClothingItemsList
import com.example.allin.data.relations.OutfitsWithClothingList
import com.example.allin.data.relations.PackingWithOutfitList
import com.example.allin.model.*
import com.example.allin.repository.ClosetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Communication between the repository and the UI
class ClosetViewModel(application: Application): AndroidViewModel(application) {
    //Initialize any lists that the functions below init() can use
    val readAllClothingData: LiveData<List<Clothing>>
    val readAllOutfitData: LiveData<List<Outfit>>
    val readAllPackingData: LiveData<List<Packing>>
    val favList: Favorites

    //val getAllOutfitWithClothingList: LiveData<List<OutfitsWithClothingList>>
    private val repository: ClosetRepository

    init {
        val clothingDao = ClothingDatabase.getDatabase(application).getClosetDao()
        repository = ClosetRepository(clothingDao)
        readAllClothingData = repository.readAllClothingData
        readAllOutfitData = repository.readAllOutfitData
        readAllPackingData = repository.readAllPackingData

        favList = Favorites(1)
        addToFavorites(favList)
    }

    fun addClothing(clothing: Clothing){
        viewModelScope.launch(Dispatchers.IO){
            repository.addClothing(clothing)
        }
    }

    fun updateClothing(clothing: Clothing){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateClothing(clothing)
        }
    }

    fun deleteClothing(clothing: Clothing){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteClothing(clothing)
        }
    }

    fun deleteAllClothing(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteALLClothing()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Clothing>> {
        return repository.searchDatabase(searchQuery)
    }

    fun selectAllTops(theme: String): LiveData<List<Clothing>>{
        return repository.selectAllTops(theme)
    }

    fun selecAllBottoms(theme: String): LiveData<List<Clothing>>{
        return repository.selectAllBottoms(theme)
    }

    fun selectAllShoes(theme: String): LiveData<List<Clothing>>{
        return repository.selectAllShoes(theme)
    }

    fun selectAllOuterWear(theme: String): LiveData<List<Clothing>>{
        return repository.selectAllOuterWear(theme)
    }

    /**
     * Outfit Calls are here
     */

    fun addOutfit(outfit: Outfit){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOutfit(outfit)
        }
    }

    fun getOutfit(name: String): Outfit{
        return repository.getOutfit(name)
    }


    fun deleteOutfit(outfitId: Long){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteOutfit(outfitId)
        }
    }

    fun deleteAllOutfits(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllOutfits()
        }
    }

    //New Update Function
    fun updateOutfit(outfit: Outfit) {
        viewModelScope.launch (Dispatchers.IO){
            repository.updateOutfit(outfit)
        }
    }

    /**
     * Outfit With Clothing calls are here
     */

    fun addOutfitWithClothingMap(outfitWithClothingTable: OutfitClothingTable){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addOutfitWithClothingMap(outfitWithClothingTable)
        }
    }

    fun getAllOutfitsWithClothing(outfitId: Long): LiveData<List<OutfitsWithClothingList>>{
           return repository.getOutfitWithClothingList(outfitId)
    }

    /**
     * Packing List Queries
     */

    fun addNewPackingList(packing: Packing){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNewPackingList(packing)
        }
    }

    fun addPackingWithOutfits(packingWithOutfitsTable: PackingWithOutfitsTable){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPackingWithOutfits(packingWithOutfitsTable)
        }
    }

    fun getPackingListWithOutfits(id: Long): LiveData<List<PackingWithOutfitList>>{
        return repository.getPackingListWithOutfits(id)
    }

    fun getPackingList(name: String): Packing {
        return repository.getPackingList(name)
    }

    fun deletePackingList(packing: Packing){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePackingList(packing)
        }
    }

    fun deleteAllPackingList(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllPacking()
        }
    }

    /**
     * Favorite Clothing Calls Here
     */

    fun addToFavorites(favorites: Favorites){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavoriteList(favorites)
        }
    }

    fun getFavoritesClothingItemsList(): LiveData<List<FavoritesClothingItemsList>>{
        return repository.getFavoritesClothingItemsList()
    }

    fun addFavClothingToList(favoriteClothingCrossRef: FavoriteClothingCrossRef){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavClothingToList(favoriteClothingCrossRef)
        }
    }
}