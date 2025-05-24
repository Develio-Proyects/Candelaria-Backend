package com.lacandelaria.domain

import jakarta.persistence.*

@Entity
class Product (
    @Column
    var title: String,
    @Column
    var description: String,
    @Column
    var price: Double,
    @Column
    var menu: Boolean,
    @Column
    var takeAway: Boolean,
    @Column
    var active: Boolean,
    @ManyToOne(fetch = FetchType.EAGER)
    var category: Category
) {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    lateinit var id: String
    @Column
    var image: String = ""
    @Column
    var discountPercentage: Int = 0

    fun getActualPrice(): Double {
        return price - (discountPercentage * price) / 100
    }
}