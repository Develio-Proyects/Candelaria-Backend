package com.lacandelaria.dto

data class ProductDTO(
    var id: String,
    var title: String,
    var description: String,
    var price: Double,
    var menu: Boolean,
    var takeAway: Boolean,
    var active: Boolean,
    var category: String,
    var image: String,
    var actualPrice: Double
)