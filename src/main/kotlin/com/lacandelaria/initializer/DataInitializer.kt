package com.lacandelaria.initializer

import com.lacandelaria.domain.User
import com.lacandelaria.domain.Category
import com.lacandelaria.domain.Product
import com.lacandelaria.repositories.UserRepository
import com.lacandelaria.repositories.CategoryRepository
import com.lacandelaria.repositories.ProductRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository

@Repository
class DataInitializer: InitializingBean {
    @Autowired lateinit var repositorioUser: UserRepository
    @Autowired lateinit var repositorioCategory: CategoryRepository
    @Autowired lateinit var repositorioProduct: ProductRepository
    @Autowired lateinit var passwordEncoder: PasswordEncoder
    @Value("\${SPRING_PROFILES_ACTIVE}") lateinit var profile: String

    val cat1 = Category("Entradas", 1)
    val cat2 = Category("Carnes", 2)
    val cat3 = Category("Pastas", 3)
    val cat4 = Category("Postres", 4)
    val cat5 = Category("Bebidas", 5)

    val product1 = Product("Salmón Ahumado", "Delicado salmón ahumado con una suave textura y sabor intenso.", 100.0, true, true, true, cat2)
    val product2 = Product("Bife de Chorizo", "Corte de carne de primera calidad, jugoso y lleno de sabor.", 200.0, true, true, true, cat2)
    val product3 = Product("Ravioles de Espinaca", "Ravioles frescos rellenos de espinaca y ricotta, con salsa de tomate casera.", 300.0, true, true, true, cat3)
    val product4 = Product("Tabla de Quesos", "Variedad de quesos locales acompañados de frutas y mermeladas.", 400.0, true, true, true, cat1)
    val product5 = Product("Costillas Ahumadas", "Costillas de cerdo ahumadas a la perfección, servidas con salsa BBQ.", 500.0, true, true, true, cat2)
    val product6 = Product("Asado de Tira", "Tradicional asado de tira cocido a la parrilla, servido con chimichurri.", 600.0, true, true, true, cat2)
    val product7 = Product("Lasagna de Carne", "Capas de pasta, carne molida y queso, horneadas con una salsa bechamel.", 700.0, true, true,true, cat3)
    val product8 = Product("Bruschetta de Tomate", "Pan tostado con tomate fresco, ajo y albahaca, aderezado con aceite de oliva.", 800.0, true, true, true, cat1)
    val product9 = Product("Tiramisu", "Delicioso postre, ideal para cerrar una comida con broche de oro.", 900.0, true, true,true, cat4)
    val product10 = Product("Agua sin gas", "Agua sin gas.", 1000.0, true, true, true, cat5)

    override fun afterPropertiesSet() {
        if (profile == "dev") {
            inicializarUser()
            inicializarCategorias()
            inicializarProductos()
            println("Datos inicializados")
        }
    }

    fun inicializarUser() {
        val user = User("admin", passwordEncoder.encode("admin"))
        repositorioUser.save(user)
    }

    fun inicializarCategorias() {
        repositorioCategory.save(cat1)
        repositorioCategory.save(cat2)
        repositorioCategory.save(cat3)
        repositorioCategory.save(cat4)
        repositorioCategory.save(cat5)
    }

    fun inicializarProductos() {
        product2.discountPercentage = 20
        product5.discountPercentage = 30
        product6.discountPercentage = 40
        product9.discountPercentage = 10

        product1.image = "http://localhost:8080/images/generic-image-salmon.webp"
        product2.image = "http://localhost:8080/images/generic-image-carnes.webp"
        product3.image = "http://localhost:8080/images/generic-image-pastas.webp"
        product4.image = "http://localhost:8080/images/generic-image-quesos.webp"
        product5.image = "http://localhost:8080/images/generic-image-costillas.webp"
        product6.image = "http://localhost:8080/images/generic-image-asado.webp"
        product7.image = "http://localhost:8080/images/generic-image-lasagna.webp"
        product8.image = "http://localhost:8080/images/generic-image-bruschetta.webp"
        product9.image = "http://localhost:8080/images/generic-image-postre.webp"
        product10.image = "http://localhost:8080/images/generic-image-agua.webp"

        repositorioProduct.save(product1)
        repositorioProduct.save(product2)
        repositorioProduct.save(product3)
        repositorioProduct.save(product4)
        repositorioProduct.save(product5)
        repositorioProduct.save(product6)
        repositorioProduct.save(product7)
        repositorioProduct.save(product8)
        repositorioProduct.save(product9)
        repositorioProduct.save(product10)
    }
}