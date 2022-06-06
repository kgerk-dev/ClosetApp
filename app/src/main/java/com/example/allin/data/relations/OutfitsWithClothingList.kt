package com.example.allin.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.allin.model.Clothing
import com.example.allin.model.OutfitClothingTable
import com.example.allin.model.Outfit

/**
 * Not in Use yet. Example of when this class will be needed.
 * For Many to Many relationships this class will define a List of Rows that meet
 * the requirements of the Embedded Clothing Item.
 *
 * Displays a list of Clothes that share the same OutfitID.
 *
 * Future Reason:
 * It will display a list of clothing items with the associated OutfitID.
 * For our Outfit List. When we select the Outfit we want, we should aim to
 * Grab this list to see each Clothing Item that is apart of it.
 */

data class OutfitsWithClothingList(
    @Embedded
    val Outfit: Outfit,
    @Relation(
        entity = Clothing::class,
        parentColumn = "outfitId",
        entityColumn = "clothingId",
        associateBy = Junction(
            value = OutfitClothingTable::class,
            parentColumn = "outfitIdRef",
            entityColumn = "clothingIdRef"
        )
    )
    val clothingList: List<Clothing>
)