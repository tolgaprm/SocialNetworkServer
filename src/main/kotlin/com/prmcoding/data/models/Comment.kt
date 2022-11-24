package com.prmcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class Comment(
    @BsonId
    val id: String = ObjectId().toString(),
    val comment: String,
    val userId: String,
    val postId: String,
    val timestamp: Long
)
