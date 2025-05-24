package com.lacandelaria.controllers

import com.lacandelaria.domain.Category
import com.lacandelaria.dto.NewCategoryDTO
import com.lacandelaria.dto.ResponseWithMessageDTO
import com.lacandelaria.services.CategoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/categories")
class CategoryController {
    @Autowired lateinit var categoryService: CategoryService

    @GetMapping
    fun getCategories(): List<Category> {
        return categoryService.getAllCategories()
    }

    @GetMapping("/menu")
    fun getMenuCategories(): List<Category> {
        return categoryService.getActiveMenuCategories()
    }

    @GetMapping("/take-away")
    fun getTakeAwayCategories(): List<Category> {
        return categoryService.getActiveTakeAwayCategories()
    }

    @PostMapping
    fun createCategory(@RequestBody newCategory: NewCategoryDTO): Category {
        val savedCategory = categoryService.saveCategory(newCategory)
        return savedCategory
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: String): ResponseEntity<ResponseWithMessageDTO> {
        categoryService.deleteCategory(id)
        return ResponseEntity.ok( ResponseWithMessageDTO("Categoria con id $id eliminada correctamente") )
    }

    @PutMapping("/{id}")
    fun updateCategory(
        @PathVariable id: String,
        @RequestBody updatedCategory: NewCategoryDTO
    ): Category {
        val category = categoryService.updateCategory(id, updatedCategory)
        return category
    }

    @PutMapping
    fun updateCategories(@RequestBody updatedCategories: List<Category>): List<Category> {
        return categoryService.updateAllCategories(updatedCategories)
    }
}