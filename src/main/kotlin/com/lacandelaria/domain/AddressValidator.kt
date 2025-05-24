package com.lacandelaria.domain

import com.lacandelaria.dto.AddressValidationResult
import org.springframework.web.client.RestTemplate

object AddressValidator {
    val baseAddress = "San Isidro, Buenos Aires, Argentina"
    val deliveryRadiusFree = 2.5
    val deliveryRadiusMax = 3.5
    val restaurantLat = -34.4706058
    val restaurantLon = -58.5163538

    fun validateAddressDistance(direccion: String): AddressValidationResult {
        val nominatimUrl = "https://nominatim.openstreetmap.org/search?q=$direccion, $baseAddress&format=json&addressdetails=1&limit=1"
        val response = RestTemplate().getForObject(nominatimUrl, Array<GeocodeResponse>::class.java)
        val location = response?.firstOrNull() ?: return AddressValidationResult(valid = false)

        val distance = haversineFormula(restaurantLat, restaurantLon, location.lat.toDouble(), location.lon.toDouble())

        return if(distance <= deliveryRadiusMax) {
            if(distance <= deliveryRadiusFree) AddressValidationResult(valid = true, deliveryPaid = false)
            else AddressValidationResult(valid = true, deliveryPaid = true)
        } else AddressValidationResult(valid = false)
    }

    private fun haversineFormula(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371.0 // Radio de la tierra en kilómetros
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c // Retorna la distancia en kilómetros
    }
}

data class GeocodeResponse(val lat: String, val lon: String)