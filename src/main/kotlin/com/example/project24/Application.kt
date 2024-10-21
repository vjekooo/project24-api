package com.example.project24

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application

private val log: Logger = LoggerFactory.getLogger(Application::class.java)

fun main(args: Array<String>) {
    log.info("Starting application")
    runApplication<Application>(*args)
}
