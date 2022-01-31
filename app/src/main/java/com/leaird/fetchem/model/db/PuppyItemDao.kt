package com.leaird.fetchem.model.db

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface PuppyItemDao {
    @Query("SELECT * from puppy_item ORDER BY name")
    fun pagingSource(): PagingSource<Int, PuppyItem>

    @Query("SELECT * from puppy_item ORDER BY name")
    fun getPuppyItems(): List<PuppyItem>

    @Query("SELECT * from puppy_item WHERE list_id = :listId ORDER BY name")
    fun getPuppyItemsForListId(listId: Int): List<PuppyItem>

    @Query("SELECT DISTINCT list_id FROM puppy_item ORDER BY list_id")
    fun getPuppyLists(): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PuppyItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: List<PuppyItem>)

    @Update
    suspend fun update(item: PuppyItem)
}