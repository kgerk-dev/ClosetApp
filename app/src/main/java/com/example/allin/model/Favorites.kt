package com.example.allin.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Favorites(
    @PrimaryKey
    @ColumnInfo(name = "favClothingId") val favClothingId: Long? = null

): Parcelable


@Parcelize
@Entity( primaryKeys = ["favClothingIdRef", "clothingIdRef"],
    foreignKeys =
    [
        ForeignKey(
            entity = Favorites::class,
            parentColumns = ["favClothingId"],
            childColumns = ["favClothingIdRef"],
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
data class FavoriteClothingCrossRef(
    val favClothingIdRef: Long,
    @ColumnInfo(index = true)
    val clothingIdRef: Long
) : Parcelable