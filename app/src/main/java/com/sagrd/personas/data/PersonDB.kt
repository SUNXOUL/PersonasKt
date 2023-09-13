package com.sagrd.personas.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sagrd.personas.data.local.dao.OcupationDao
import com.sagrd.personas.data.local.dao.PersonDao
import com.sagrd.personas.domain.model.Ocupation
import com.sagrd.personas.domain.model.Person

@Database(
    entities = [Person::class, Ocupation::class],
    version = 3
)
abstract class PersonDB : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun occupationDao(): OcupationDao
}
