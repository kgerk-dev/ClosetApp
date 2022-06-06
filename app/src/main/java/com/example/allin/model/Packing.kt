package com.example.allin.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Packing(

    @PrimaryKey
    @ColumnInfo(name = "packingID") val id: Long?,
    val packingListName: String

): Parcelable