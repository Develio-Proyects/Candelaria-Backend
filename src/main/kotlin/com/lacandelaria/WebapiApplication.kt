package com.lacandelaria

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebapiApplication

fun main(args: Array<String>) {
	runApplication<WebapiApplication>(*args)
}
