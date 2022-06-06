package com.example.allin.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.allin.model.Clothing
import com.example.allin.model.FavoriteClothingCrossRef
import com.example.allin.model.Favorites

data class FavoritesClothingItemsList(
    @Embedded val favClothing: Favorites,
    @Relation(
        entity = Clothing::class,
        parentColumn = "favClothingId",
        entityColumn = "clothingId",
        associateBy = Junction(
            value = FavoriteClothingCrossRef::class,
            parentColumn = "favClothingIdRef",
            entityColumn = "clothingIdRef"
        )
    )
    val favorites: List<Clothing>
)