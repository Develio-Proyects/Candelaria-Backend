package com.lacandelaria.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class ImageService {
    private val imageDirectory: Path = Paths.get("images")
    @Value("\${spring.url.api}") private lateinit var basePath: String
    @Value("\${SPRING_PROFILES_ACTIVE}") lateinit var profile: String

    init { Files.createDirectories(imageDirectory) }

    fun saveAndGetImagePath(image: MultipartFile, filename: String): String {
        saveImage(image, filename)
        return "$basePath/images/$filename"
    }

    private fun saveImage(image: MultipartFile, filename: String): Path {
        val targetLocation = imageDirectory.resolve(filename)
        Files.copy(image.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
        return targetLocation
    }

    fun deleteImage(imagePath: String) {
        if (profile != "dev") {
            try {
                val filePath = imageDirectory.resolve(imagePath.substringAfterLast("/"))
                Files.deleteIfExists(filePath)
            } catch (e: IOException) {
                throw RuntimeException("No se pudo eliminar la imagen: ${e.message}")
            }
        }
    }
}