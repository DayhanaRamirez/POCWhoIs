package com.realwatch.pocwhois.model

data class Domain(
    val id: Int,
    val domainName: String,
    val registrar: String,
    val registrarUrl: String,
    val creationDate: String,
    val updatedDate: String,
    val expirationDate: String,
    val domainStatus: String,
    val nameServer: String,
    val dnssec: String
)