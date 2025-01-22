package com.realwatch.pocwhois.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/domain")
class DomainController {

    @GetMapping("/ping")
    fun ping(): String {
        return "pong"
    }
}