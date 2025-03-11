package com.example.project24

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class Application

private val log: Logger = LoggerFactory.getLogger(Application::class.java)

fun main(args: Array<String>) {
    log.info("Starting application")
    try {
        runApplication<Application>(*args)
        log.info("Application started successfully")
    } catch (ex: Exception) {
        if(ex.javaClass.name.contains("SilentExitException")) {
            log.error("Spring is restarting the main thread - See spring-boot-devtools")
            return
        }
        log.error("Application failed to start: ${ex.message}", ex)
        throw ex
    }
}
