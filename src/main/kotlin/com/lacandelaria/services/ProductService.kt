package com.lacandelaria.services

import com.lacandelaria.domain.Category
import com.lacandelaria.domain.Product
import com.lacandelaria.dto.NewProductDTO
import com.lacandelaria.exceptions.BadRequestException
import com.lacandelaria.exceptions.NotFoundException
import com.lacandelaria.repositories.CategoryRepository
import com.lacandelaria.repositories.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ProductService {
    @Autowired lateinit var productRepository: ProductRepository
    @Autowired lateinit var categoryRepository: CategoryRepository
    @Autowired lateinit var imageService: ImageService

    // GET

    fun getProductById(id: String): Product {
        return productRepository.findById(id).orElseThrow { NotFoundException("Producto no encontrado") }
    }

    fun getAllProducts(): List<Product> {
        return productRepository.findAll().toList()
    }

    fun getActiveMenuProducts(): List<Product> {
        return productRepository.getActiveMenuProducts()
    }

    fun getActiveTakeAwayProducts(): List<Product> {
        return productRepository.getActiveTakeAwayProducts()
    }

    // SAVE

    @Transactional(Transactional.TxType.REQUIRED)
    fun saveProduct(newProductDTO: NewProductDTO): Product {
        val category = getCategoryByName(newProductDTO.category)
        this.validateProduct(newProductDTO)

        val product = Product(
            title = newProductDTO.title,
            description = newProductDTO.description,
            price = newProductDTO.price,
            menu = newProductDTO.menu,
            takeAway = newProductDTO.takeAway,
            active = newProductDTO.active,
            category = category
        )
        productRepository.save(product)

        saveImageInProduct(product, newProductDTO.image)
        product.discountPercentage = newProductDTO.discountPercentage

        productRepository.save(product)
        return product
    }

    // UPDATE

    fun updateProduct(id: String, updatedProductDTO: NewProductDTO): Product {
        this.validateProduct(updatedProductDTO)
        val category = getCategoryByName(updatedProductDTO.category)
        val existingProduct = this.getProductById(id)

        existingProduct.title = updatedProductDTO.title
        existingProduct.description = updatedProductDTO.description
        existingProduct.price = updatedProductDTO.price
        existingProduct.discountPercentage = updatedProductDTO.discountPercentage
        existingProduct.menu = updatedProductDTO.menu
        existingProduct.takeAway = updatedProductDTO.takeAway
        existingProduct.active = updatedProductDTO.active
        existingProduct.category = category

        deleteImage(existingProduct.image)
        saveImageInProduct(existingProduct, updatedProductDTO.image)

        productRepository.save(existingProduct)
        return existingProduct
    }

    // DELETE

    fun deleteProduct(id: String) {
        val product = this.getProductById(id)
        imageService.deleteImage(product.image)
        productRepository.delete(product)
    }

    // UTILS

    fun deleteImage(imagePath: String) {
        imageService.deleteImage(imagePath)
    }

    fun saveImageInProduct(product: Product, image: MultipartFile) {
        val extension = image.originalFilename?.substringAfterLast('.', "")
        val filename = if (!extension.isNullOrEmpty()) "${product.id}.$extension" else product.id
        val imagePath = imageService.saveAndGetImagePath(image, filename)
        product.image = imagePath
    }

    fun getCategoryByName(name: String): Category {
        return categoryRepository.findByName(name).orElseThrow { NotFoundException("Categoría no encontrada") }
    }

    fun validateProduct(newProductDTO: NewProductDTO) {
        if (newProductDTO.title.isBlank()) throw BadRequestException("El título no puede estar vacío")
        if (newProductDTO.title.length > 50) throw BadRequestException("El título no puede tener más de 50 caracteres")
        if (newProductDTO.description.length > 100) throw BadRequestException("La descripción no puede tener más de 100 caracteres")
        if (newProductDTO.price <= 0) throw BadRequestException("El precio debe ser mayor a 0")
        if (newProductDTO.discountPercentage < 0) throw BadRequestException("El porcentaje de descuento debe ser mayor o igual a 0")
    }
}