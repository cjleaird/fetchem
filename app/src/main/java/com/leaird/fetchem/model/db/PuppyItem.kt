package com.leaird.fetchem.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "puppy_item")
data class PuppyItem(
    @PrimaryKey
    @SerializedName("id")
    val id: Int = 0,

    @ColumnInfo(name = "list_id")
    @SerializedName("listId")
    val listId: Int,

    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String?
)