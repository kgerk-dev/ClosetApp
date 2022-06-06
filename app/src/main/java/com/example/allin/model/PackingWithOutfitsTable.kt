package com.example.allin.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["packingRefID", "outfitRefID"],
    foreignKeys = [
        ForeignKey(
            entity = Packing::class,
            parentColumns = ["packingID"],
            childColumns = ["packingRefID"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Outfit::class,
            parentColumns = ["outfitId"],
            childColumns = ["outfitRefID"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PackingWithOutfitsTable(
    @NonNull val packingRefID: Long,
    @ColumnInfo(index = true)
    @NonNull val outfitRefID: Long
)