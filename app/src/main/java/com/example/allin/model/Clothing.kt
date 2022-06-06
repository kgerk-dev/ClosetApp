package com.example.allin.model

import android.os.Parcelable
import androidx.room.ColumnInfo

import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * Class member values. These values will also represent the Table "Clothing"
 * and its columns by value types
 * Parcelize allow the data to be bound to navigation arguments and transferred between
 * fragments and activities on navigation calls
 *
 */
@Parcelize
@Entity
data class Clothing(
    //Sets all attributes and primary key
    @PrimaryKey
    @ColumnInfo(name = "clothingId") val  clothingId: Long?= null,
    val type: String,
    val color: String,
    val style: String,
    val description: String,
    val dateAdded: Date = Date(),
    val brand: String,
    val theme: String,
    val image: String,

): Parcelable