package com.lacandelaria.services

import com.lacandelaria.domain.AddressValidator
import com.lacandelaria.dto.AddressValidationResult
import com.lacandelaria.exceptions.BadRequestException
import org.springframework.stereotype.Service

@Service
class AddressService {
    fun validateAndCheckAddress(street: String, number: Int): AddressValidationResult {
        this.validateAddress(street, number)
        return AddressValidator.validateAddressDistance("$street $number")
    }

    private fun validateAddress(street: String, number: Int) {
        if (street.isEmpty()) throw BadRequestException("La dirección no puede ser vacía")
        if (number <= 0) throw BadRequestException("El número de la dirección no puede ser menor o igual a cero")
    }
}