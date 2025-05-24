package com.lacandelaria.dto

data class AddressValidationResult(
    val valid: Boolean,
    val deliveryPaid: Boolean? = null
)