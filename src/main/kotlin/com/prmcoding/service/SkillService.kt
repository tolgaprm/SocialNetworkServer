package com.prmcoding.service

import com.prmcoding.data.models.Skill
import com.prmcoding.data.repository.skill.SkillRepository

class SkillService(
    private val skillRepository: SkillRepository
) {
    suspend fun getSkills(): List<Skill> {
        return skillRepository.getSkills()
    }
}