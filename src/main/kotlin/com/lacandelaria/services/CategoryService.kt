package com.lacandelaria.services

import com.lacandelaria.domain.Category
import com.lacandelaria.dto.NewCategoryDTO
import com.lacandelaria.exceptions.BadRequestException
import com.lacandelaria.exceptions.NotFoundException
import com.lacandelaria.repositories.CategoryRepository
import com.lacandelaria.repositories.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CategoryService {
    @Autowired lateinit var categoryRepository: CategoryRepository
    @Autowired lateinit var productRepository: ProductRepository

    // GET

    fun getCategoryById(id: String): Category {
        return categoryRepository.findById(id).orElseThrow {
            NotFoundException("La categoría con id '$id' no existe")
        }
    }

    fun getAllCategories(): List<Category> {
        return categoryRepository.findAll().toList()
    }

    fun getActiveMenuCategories(): List<Category> {
        return categoryRepository.getActiveMenuCategories()
    }

    fun getActiveTakeAwayCategories(): List<Category> {
        return categoryRepository.getActiveTakeAwayCategories()
    }

    // SAVE

    @Transactional(Transactional.TxType.REQUIRED)
    fun saveCategory(newCategory: NewCategoryDTO): Category {
        this.validateNewCategory(newCategory.name)
        val category = Category(newCategory.name, newCategory.position)

        if (categoryRepository.existsByPosition(newCategory.position)) {
            categoryRepository.incrementPositionsFrom(newCategory.position)
        }

        categoryRepository.save(category)

        return category
    }

    // UPDATE

    @Transactional(Transactional.TxType.REQUIRED)
    fun updateCategory(id: String, updatedCategory: NewCategoryDTO): Category {
        val existingCategory = this.getCategoryById(id)
        this.validateCategory(updatedCategory.name)

        if (existingCategory.position != updatedCategory.position) {
            adjustCategoryPositions(existingCategory.position, updatedCategory.position)
        }

        existingCategory.name = updatedCategory.name
        existingCategory.position = updatedCategory.position

        categoryRepository.save(existingCategory)
        return existingCategory
    }

    @Transactional(Transactional.TxType.REQUIRED)
    fun updateAllCategories(updatedCategories: List<Category>): List<Category> {
        this.validateAllCategoriesPresent(updatedCategories)
        this.validatePositions(updatedCategories)

        val updatedCategoryList = mutableListOf<Category>()

        updatedCategories.forEach { updatedCategory ->
            val existingCategory = categoryRepository.findById(updatedCategory.id).orElseThrow {
                NotFoundException("La categoría con id '${updatedCategory.id}' no existe")
            }

            this.validateCategory(updatedCategory.name)

            existingCategory.name = updatedCategory.name
            existingCategory.position = updatedCategory.position

            categoryRepository.save(existingCategory)
            updatedCategoryList.add(existingCategory)
        }

        return updatedCategoryList
    }

    // DELETE

    fun deleteCategory(id: String) {
        val category = this.getCategoryById(id)
        this.validateCategoryWithoutProducts(category)
        categoryRepository.delete(category)
    }

    // UTILS

    private fun adjustCategoryPositions(oldPosition: Int, newPosition: Int) {
        if (newPosition > oldPosition) {
            categoryRepository.decrementPositionsBetween(oldPosition + 1, newPosition)
        } else {
            categoryRepository.incrementPositionsBetween(newPosition, oldPosition - 1)
        }
    }

    fun validateCategoryWithoutProducts(category: Category) {
        val productList = productRepository.getAllProductsByCategoryId(category.id)
        if ( productList.isNotEmpty() ) throw BadRequestException("No se puede eliminar la categoría '${category.name}' porque tiene productos asociados")
    }

    fun validateNewCategory(name: String) {
        val category = categoryRepository.findByName(name)
        if ( category.isPresent ) throw BadRequestException("La categoría con nombre '${name.uppercase()}' ya existe")
        validateCategory(name)
    }

    fun validateCategory(name: String) {
        if (name.isBlank()) throw BadRequestException("El nombre de la categoría no puede estar vacío")
        if (name.length > 15) throw BadRequestException("El nombre de la categoría debe tener menos de 15 caracteres")
    }

    fun validatePositions(updatedCategories: List<Category>) {
        for (category in updatedCategories) {
            val repeatPosition = updatedCategories.filter { it.position == category.position }
            if (repeatPosition.size != 1) throw BadRequestException("No se pueden repetir posiciones en las categorías")
        }
    }

    fun validateAllCategoriesPresent(updatedCategories: List<Category>) {
        val existingCategories = categoryRepository.findAll()
        for (category in existingCategories) {
            val categoryExists = updatedCategories.any { it.id == category.id }
            if ( ! categoryExists ) throw BadRequestException("Falta la categoría '${category.name}' para actualizar, tienen que estar todas")
        }
    }
}