package com.example.allin.data

import androidx.room.TypeConverter
import java.util.*

/**
 * Room Database will use this class to convert certain Java values into datatypes that SQLite can
 * read and store.
 * EX: Date() is from the Java.util.* library
 */
class DBTypeConverters {

    @TypeConverter
    fun fromDateToLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(date: Long): Date {
        return Date(date)
        //If this returns a null value, try removing the safe call and use below.
        // return Date(millisSinceEpoch)
    }
}