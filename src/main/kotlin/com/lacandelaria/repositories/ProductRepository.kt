package com.lacandelaria.repositories

import com.lacandelaria.domain.Product
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : CrudRepository<Product, String> {
    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    fun getAllProductsByCategoryId(id: String): List<Product>

    @Query("SELECT p FROM Product p WHERE p.active = true AND p.menu = true")
    fun getActiveMenuProducts(): List<Product>

    @Query("SELECT p FROM Product p JOIN Category c ON p.category.id = c.id WHERE p.active = true AND p.takeAway = true ORDER BY c.position")
    fun getActiveTakeAwayProducts(): List<Product>
}