package com.lacandelaria.dto

import org.springframework.web.multipart.MultipartFile

data class NewProductDTO(
    var title: String,
    var description: String,
    var price: Double,
    var menu: Boolean,
    var takeAway: Boolean,
    var active: Boolean,
    var category: String,
    var image: MultipartFile,
    var discountPercentage: Int
)