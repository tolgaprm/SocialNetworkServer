package com.prmcoding.data.models

import com.prmcoding.responses.SkillsResponse
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@kotlinx.serialization.Serializable
data class Skill(
    @BsonId
    val id: String = ObjectId().toString(),
    val name: String,
    val imageUrl: String
) {
    fun toSkillResponse(): SkillsResponse {
        return SkillsResponse(
            name = name,
            imageUrl = imageUrl
        )
    }
}
