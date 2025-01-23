package com.realwatch.pocwhois.controller

import com.realwatch.pocwhois.service.DomainService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class DomainController(private val domainService: DomainService) {

    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }

    @GetMapping("/whois")
    fun whoIs(@RequestParam domain: String): String {
        return domainService.whoIs(domain)
    }
}