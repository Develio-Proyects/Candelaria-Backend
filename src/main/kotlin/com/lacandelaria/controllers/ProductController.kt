package com.lacandelaria.controllers

import com.lacandelaria.dto.NewProductDTO
import com.lacandelaria.dto.ProductDTO
import com.lacandelaria.dto.ResponseWithMessageDTO
import com.lacandelaria.services.ProductService
import com.lacandelaria.utils.Serializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController {
    @Autowired lateinit var productService: ProductService

    @GetMapping
    fun getProducts(): List<ProductDTO> {
        val productsList = productService.getAllProducts()
        return Serializer.buildProductDTOList(productsList)
    }

    @GetMapping("/menu")
    fun getMenuProducts(): List<ProductDTO> {
        val productsList = productService.getActiveMenuProducts()
        return Serializer.buildProductDTOList(productsList)
    }

    @GetMapping("/take-away")
    fun getTakeAwayProducts(): List<ProductDTO> {
        val productsList = productService.getActiveTakeAwayProducts()
        return Serializer.buildProductDTOList(productsList)
    }

    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: String): ResponseEntity<ResponseWithMessageDTO> {
        productService.deleteProduct(id)
        return ResponseEntity.ok( ResponseWithMessageDTO("Producto con id $id eliminado correctamente") )
    }

    @PostMapping(consumes = ["multipart/form-data"])
    fun createProduct(@ModelAttribute newProductDTO: NewProductDTO): ProductDTO {
        val savedProduct = productService.saveProduct(newProductDTO)
        return Serializer.buildProductDTO(savedProduct)
    }

    @PutMapping("/{id}", consumes = ["multipart/form-data"])
    fun updateProduct(
        @PathVariable id: String,
        @ModelAttribute updatedProductDTO: NewProductDTO
    ): ProductDTO {
        val updatedProduct = productService.updateProduct(id, updatedProductDTO)
        return Serializer.buildProductDTO(updatedProduct)
    }
}