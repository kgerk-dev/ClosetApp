package com.example.allin.model

import android.os.Parcelable
import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * This class is going to begin with a finite number of clothing items.
 * The constructor will require an [outfitName], for each [Outfit] item.
 *
 * The [Parcelize] is a Kotlin annnotation that allows us to bind the data from each Entity item stored
 * while navigating through pages.
 */
@Parcelize
@Entity
data class Outfit(

    @PrimaryKey
    @ColumnInfo(name = "outfitId")val id: Long?,
    var outfitName: String,
    var theme: String

): Parcelable

