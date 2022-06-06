package com.example.allin.data.relations

import android.graphics.LightingColorFilter
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.allin.model.Outfit
import com.example.allin.model.Packing
import com.example.allin.model.PackingWithOutfitsTable

data class PackingWithOutfitList(
    @Embedded
    val packing: Packing,
    @Relation(
        entity = Outfit::class,
        parentColumn = "packingID",
        entityColumn = "outfitId",
        associateBy = Junction(
            value = PackingWithOutfitsTable::class,
            parentColumn = "packingRefID",
            entityColumn = "outfitRefID"
        )
    )
    val packedOutfitList: List<OutfitsWithClothingList>
)