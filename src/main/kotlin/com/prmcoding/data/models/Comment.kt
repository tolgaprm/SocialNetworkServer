package com.prmcoding.data.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class Comment(
    @BsonId
    val id: String = ObjectId().toString(),
    val username: String,
    val profileImageUrl: String,
    val comment: String,
    val likeCount: Int,
    val userId: String,
    val postId: String,
    val timestamp: Long
)
