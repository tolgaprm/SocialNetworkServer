package com.prmcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class Like(
    @BsonId
    val id: String = ObjectId().toString(),
    val userId: String,
    val parentId: String
)
