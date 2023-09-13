package com.sagrd.personas.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sagrd.personas.domain.model.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(person: com.sagrd.personas.domain.model.Person)

    @Query(
        """
        SELECT * 
        FROM Persons 
        WHERE personId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): Person

    @Delete
    suspend fun delete(person: com.sagrd.personas.domain.model.Person)

    @Query("SELECT * FROM Persons")
    fun getAll(): Flow<List<Person>>
}