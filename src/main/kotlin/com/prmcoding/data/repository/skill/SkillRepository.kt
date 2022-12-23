package com.prmcoding.data.repository.skill

import com.prmcoding.data.models.Skill

interface SkillRepository {

    suspend fun getSkills(): List<Skill>
}