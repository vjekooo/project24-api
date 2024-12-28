package com.example.project24.config

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.BufferedReader
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@Component
class RequestLoggingFilter : OncePerRequestFilter() {

    private val logger =
        LoggerFactory.getLogger(RequestLoggingFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requestDetails = StringBuilder()
        requestDetails.append("HTTP Method: ").append(request.method)
            .append("\n")
        requestDetails.append("Request URI: ").append(request.requestURI)
            .append("\n")
        requestDetails.append("Headers: ").append(logHeaders(request))
            .append("\n")
        if (request.method.equals("POST", true) || request.method.equals(
                "PUT",
                true
            )
        ) {
//            requestDetails.append("Request Body: ").append(logBody(request))
//                .append("\n")
        }

        logger.info("Incoming API Request: \n$requestDetails")

        // Continue the filter chain
        filterChain.doFilter(request, response)
    }

    private fun logHeaders(request: HttpServletRequest): String {
        val headers = mutableListOf<String>()
        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val header = headerNames.nextElement()
            headers.add("$header: ${request.getHeader(header)}")
        }
        return headers.joinToString(", ")
    }

//    private fun logBody(request: HttpServletRequest): String {
//        val stringBuilder = StringBuilder()
//        try {
//            val reader: BufferedReader = request.reader
//            var line: String? = reader.readLine()
//            while (line != null) {
//                stringBuilder.append(line)
//                line = reader.readLine()
//            }
//        } catch (e: Exception) {
//            logger.error("Failed to read request body", e)
//        }
//        return stringBuilder.toString()
//    }
}