package com.sagrd.personas.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "Persons")
data class Person(
    @PrimaryKey
    val personId: Int?=null,
    var name:String="",
    var image: String,
    var telephone: String,
    var cellphone: String,
    var email: String,
    var direccion: String,
    var fechaNacimiento: String,
    var ocupationId: Int?=null,
)

