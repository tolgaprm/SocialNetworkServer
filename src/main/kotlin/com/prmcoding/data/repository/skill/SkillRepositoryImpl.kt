package com.prmcoding.data.repository.skill

import com.prmcoding.data.models.Skill
import org.litote.kmongo.coroutine.CoroutineDatabase

class SkillRepositoryImpl(
    private val db: CoroutineDatabase
) : SkillRepository {
    private val skills = db.getCollection<Skill>()

    override suspend fun getSkills(): List<Skill> {
        return skills.find().toList()
    }
}