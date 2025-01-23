package com.realwatch.pocwhois.entity

import jakarta.persistence.*

@Entity
@Table(name = "domain")
data class DomainEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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