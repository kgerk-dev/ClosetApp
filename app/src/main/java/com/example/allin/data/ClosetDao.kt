package com.example.allin.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.allin.data.relations.ClothingWithOutfitsList
import com.example.allin.data.relations.FavoritesClothingItemsList
import com.example.allin.data.relations.OutfitsWithClothingList
import com.example.allin.data.relations.PackingWithOutfitList
import com.example.allin.model.*

@Dao
interface ClosetDao {

    @Query("SELECT * FROM Clothing ORDER BY clothingId ASC")
    fun readAllClothingData(): LiveData<List<Clothing>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addClothing(clothing: Clothing): Long

    @Update
    suspend fun updateClothing(clothing: Clothing)

    @Delete
    suspend fun deleteClothing(clothing: Clothing)

    @Query("DELETE FROM Clothing")
    suspend fun deleteAllClothing()

    @Query("SELECT * FROM Clothing WHERE type='Top' AND theme=:theme ORDER BY clothingId ASC")
    fun selectClothingTops(theme: String): LiveData<List<Clothing>>

    @Query("SELECT * FROM Clothing WHERE type='Bottom' AND theme=:theme ORDER BY clothingId ASC")
    fun selectClothingBottoms(theme: String): LiveData<List<Clothing>>

    @Query("SELECT * FROM Clothing WHERE type='Shoes' AND theme=:theme ORDER BY clothingId ASC")
    fun selectClothingShoes(theme: String): LiveData<List<Clothing>>

    @Query("SELECT * FROM Clothing WHERE type='OuterWear' AND theme=:theme ORDER BY clothingId ASC")
    fun selectClothingOuterWear(theme: String): LiveData<List<Clothing>>

    //Called in ListFragment Searchbar. Queries Clothing Type or Clothing Color.
    @Query("SELECT * FROM Clothing WHERE type LIKE :searchQuery OR color LIKE :searchQuery OR style LIKE :searchQuery OR theme LIKE :searchQuery OR brand LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Clothing>>

    /**
     * Outfit DAO section
     */

    //Create the Outfit First, then bring the users to a new page to insert the list of clothing items.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOutfit(outfit: Outfit): Long

    @Query("SELECT * FROM Outfit ORDER BY outfitId ASC")
    fun readAllOutfitData(): LiveData<List<Outfit>>

    @Query("SELECT * FROM OUTFIT WHERE outfitName = :name ORDER BY outfitId DESC LIMIT 1")
    fun getOutfit(name: String): Outfit

    @Query("DELETE FROM Outfit WHERE outfitId = :outfitId ")
    suspend fun deleteOutfit(outfitId: Long)

    @Query("DELETE FROM Outfit")
    suspend fun deleteAllOutfits()

    //New Update Function
    @Update
    suspend fun updateOutfit(outfit: Outfit)

    /**
     * Insert Clothing into Outfits using New M-M relation table [OutfitClothingTable].
     */

    /* "Long" value not of much use other than if 1 or greater insert, if -1 not inserted */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOutfitClothingMap(outfitClothingTable: OutfitClothingTable): Long

    ///Given an OutfitID it will return a list of Clothing Items associated with it.
    @Transaction
    @Query("SELECT * FROM outfit WHERE outfitId = :outfitId")
    fun getAllOutfitsWithClothingList(outfitId: Long): LiveData<List<OutfitsWithClothingList>>

    @Transaction
    @Query("SELECT * FROM clothing")
    fun getAllClothingWithOutfitList(): LiveData<List<ClothingWithOutfitsList>>


    /**
     * Add items to the Packing List
     */
    @Query("SELECT * FROM Packing ORDER BY packingID ASC")
    fun readAllPackingData(): LiveData<List<Packing>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addNewPackingList(packing: Packing): Long

    @Delete
    suspend fun deletePackingList(packing: Packing)

    @Query("DELETE FROM Packing")
    suspend fun deleteAllPacking()

    @Query("SELECT * FROM Packing WHERE packingListName = :name")
    fun getPackingList(name: String): Packing

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPackingWithOutfits(packingWithOutfitsTable: PackingWithOutfitsTable): Long

    @Transaction
    @Query("SELECT * FROM Packing WHERE packingID = :id")
    fun getPackingListWithOutfits(id: Long): LiveData<List<PackingWithOutfitList>>

    /**
     * Favorites Class Queries
     */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteList(favorites: Favorites): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavClothingToList(favoriteClothingCrossRef: FavoriteClothingCrossRef): Long

    @Transaction
    @Query("SELECT * FROM Favorites")
    fun getFavClothingList(): LiveData<List<FavoritesClothingItemsList>>
}

