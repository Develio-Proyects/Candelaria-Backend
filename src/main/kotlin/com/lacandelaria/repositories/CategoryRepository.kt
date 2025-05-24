package com.lacandelaria.repositories

import com.lacandelaria.domain.Category
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CategoryRepository : CrudRepository<Category, String> {
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name)")
    fun findByName(name: String): Optional<Category>

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.position = :position")
    fun existsByPosition(position: Int): Boolean

    @Modifying
    @Query("UPDATE Category c SET c.position = c.position + 1 WHERE c.position >= :position")
    fun incrementPositionsFrom(position: Int): Int

    @Modifying
    @Query("UPDATE Category c SET c.position = c.position + 1 WHERE c.position BETWEEN :startPosition AND :endPosition")
    fun incrementPositionsBetween(startPosition: Int, endPosition: Int): Int

    @Modifying
    @Query("UPDATE Category c SET c.position = c.position - 1 WHERE c.position BETWEEN :startPosition AND :endPosition")
    fun decrementPositionsBetween(startPosition: Int, endPosition: Int): Int

    @Query("SELECT c FROM Category c JOIN Product p ON p.category.id = c.id WHERE p.active = true AND p.menu = true")
    fun getActiveMenuCategories(): List<Category>

    @Query("SELECT c FROM Category c JOIN Product p ON p.category.id = c.id WHERE p.active = true AND p.takeAway = true")
    fun getActiveTakeAwayCategories(): List<Category>
}