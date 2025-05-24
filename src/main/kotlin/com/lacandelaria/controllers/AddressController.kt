package com.lacandelaria.controllers

import com.lacandelaria.dto.AddressValidationResult
import com.lacandelaria.services.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/address")
class AddressController {
    @Autowired lateinit var addressService: AddressService

    @GetMapping
    fun validAddress(
        @RequestParam street: String,
        @RequestParam number: Int
    ): ResponseEntity<AddressValidationResult> {
        val result = addressService.validateAndCheckAddress(street, number)
        return ResponseEntity(result, HttpStatus.OK)
    }
}