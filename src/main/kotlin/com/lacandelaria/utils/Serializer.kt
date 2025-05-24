package com.lacandelaria.utils

import com.lacandelaria.domain.Product
import com.lacandelaria.dto.ProductDTO

object Serializer {
    fun buildProductDTO(product: Product): ProductDTO {
        return ProductDTO(
            product.id,
            product.title,
            product.description,
            product.price,
            product.menu,
            product.takeAway,
            product.active,
            product.category.name,
            product.image,
            product.getActualPrice()
        )
    }

    fun buildProductDTOList(productList: List<Product>): List<ProductDTO> {
        return productList.map {
            buildProductDTO(it)
        }
    }
}