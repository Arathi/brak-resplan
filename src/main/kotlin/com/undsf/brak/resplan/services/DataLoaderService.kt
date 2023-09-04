package com.undsf.brak.resplan.services

import com.undsf.brak.crawler.Crawler
import com.undsf.brak.domains.metadatas.enums.*
import com.undsf.brak.exceptions.DataLoaderException
import com.undsf.brak.resplan.messages.DataResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import com.undsf.brak.domains.metadatas.Student as StudentMetadata
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

private val log = LoggerFactory.getLogger(DataLoaderService::class.java)

@Service
class DataLoaderService {
    @Autowired
    private lateinit var crawler: Crawler

    @Throws(DataLoaderException::class)
    fun loadStudentMetadata(name: String): StudentMetadata {
        val pageName = name
        val article = crawler.getArticle(pageName)
        if (article == null) {
            log.warn("获取条目失败")
            throw DataLoaderException(DataResponse.ErrorWikiArticle)
        }

        val character = crawler.getCharacter(article)
        if (character == null) {
            log.warn("获取角色数据失败")
            throw DataLoaderException(DataResponse.ErrorWikiCharacter)
        }

        val student = StudentMetadata(
            id = character.Id!!,
            wikiPage = character.Name!!,
            school = character.School!!.toSchool(),
            weaponType = character.WeaponType!!.toWeaponType(),
            usesCover = (character.UsesCover == "Yes"),
            rarity = character.Rarity!!,
            combatClass = character.CombatClass!!.toCombatClass(),
            armorType = character.ArmorType!!.toArmorType(),
            attackType = character.AttackType!!.toAttackType(),
            position = character.Position!!.toPosition(),
            role = character.Role!!.toRole(),
            urban = character.CityTownAffinity!!,
            outdoors = character.OutdoorAffinity!!,
            indoors = character.IndoorAffinity!!,
            equipmentSlot1 = character.EquipmentSlot1!!.toEquipmentCategory(),
            equipmentSlot2 = character.EquipmentSlot2!!.toEquipmentCategory(),
            equipmentSlot3 = character.EquipmentSlot3!!.toEquipmentCategory(),
            pool = character.CharacterPool!!.toPoolType(),
            releaseDate = character.ReleaseDate!!.replace("/", "-")
        )

        // TODO 加载角色其他信息
        
        // TODO 角色信息入库

        return student
    }
}