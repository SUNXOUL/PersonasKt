package com.sagrd.personas.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagrd.personas.domain.model.Ocupation
import kotlinx.coroutines.flow.Flow

@Dao
interface OcupationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(ocupation: Ocupation)

    @Query(
        """
        SELECT * 
        FROM ocupations 
        WHERE ocupationId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): Ocupation

    @Delete
    suspend fun delete(ocupation: Ocupation)

    @Query("SELECT * FROM ocupations")
    fun getAll(): Flow<List<Ocupation>>
}