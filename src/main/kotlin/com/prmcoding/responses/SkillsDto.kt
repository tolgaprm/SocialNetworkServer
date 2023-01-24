package com.prmcoding.responses

import com.prmcoding.data.models.Skill
import kotlinx.serialization.Serializable

@Serializable
data class SkillsDto(
    val name: String,
    val imageUrl: String
) {
    fun toSkill(): Skill {
        return Skill(
            name = name,
            imageUrl = imageUrl
        )
    }
}
