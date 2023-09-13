package com.sagrd.personas.domain.repository

import com.sagrd.personas.data.PersonDB
import com.sagrd.personas.domain.model.Person
import javax.inject.Inject

class PersonRepository @Inject constructor(
    private val personDB: PersonDB
) {
    suspend fun  SavePerson(person: Person) = personDB.personDao().save(person)
    fun getAll() = personDB.personDao().getAll()
}