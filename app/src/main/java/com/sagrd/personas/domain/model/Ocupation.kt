package com.sagrd.personas.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ocupations")
data class Ocupation(
    @PrimaryKey
    val ocupationId : Int?=null,
    var name :String="",
)