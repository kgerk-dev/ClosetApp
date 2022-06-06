package com.example.allin.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * This class is unused still. Trying to find the correct way to implement it.
 *
 * It is a Relational Class required for the two relational tables:
 *  1.  ClothingWithOutfits
 *  2.  OutfitsWithClothing
 *
 */
@Entity(
    primaryKeys = ["outfitIdRef", "clothingIdRef"],
    foreignKeys =
    [
        ForeignKey(
            entity = Outfit::class,
            parentColumns = ["outfitId"],
            childColumns = ["outfitIdRef"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Clothing::class,
            parentColumns = ["clothingId"],
            childColumns = ["clothingIdRef"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE)
    ]
)
data class OutfitClothingTable(
    @NonNull val outfitIdRef: Long,
    @ColumnInfo(index = true)
    @NonNull val clothingIdRef: Long
)