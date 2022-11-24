package com.prmcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class Skill(
    @BsonId
    val id: String = ObjectId().toString(),
    val skill: String,
    val iconUrl: String
)
