package com.realwatch.pocwhois.service

import com.realwatch.pocwhois.exception.InvalidURLException
import org.apache.commons.validator.routines.UrlValidator
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader

@Service
class DomainService() {

    val path = "C:\\Users\\user\\Documents\\who"

    fun whoIs(domain: String): String {
        return try {
            val validDomain = checkURL(domain)
            val process = ProcessBuilder("$path/whois.exe", validDomain).start()
            val output = StringBuilder()

            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                reader.forEachLine { line ->
                    output.append(line).append('\n')
                }
            }

            process.waitFor()
            parseWhoIsQuery(output.toString())
        } catch (e: Exception) {
            "Error executing whois query: ${e.message}"
        }
    }

    fun parseWhoIsQuery(query: String): String {
        val stringBuilder = StringBuilder()
        val singleMatchFields = mapOf(
            "Domain Name" to Regex("Domain Name:\\s*(.+)", RegexOption.IGNORE_CASE),
            "Registrar" to Regex("Registrar:\\s*(.+)", RegexOption.IGNORE_CASE),
            "Registrar URL" to Regex("Registrar URL:\\s*(.+)", RegexOption.IGNORE_CASE),
            "Creation Date" to Regex("Creation Date:\\s*(.+)", RegexOption.IGNORE_CASE),
            "Updated Date" to Regex("Updated Date:\\s*(.+)", RegexOption.IGNORE_CASE),
            "Expiration Date" to Regex("Expiration Date:\\s*(.+)", RegexOption.IGNORE_CASE),
            "Registry Expiry Date" to Regex("Registry Expiry Date:\\s*(.+)", RegexOption.IGNORE_CASE),
            "DNSSEC" to Regex("DNSSEC:\\s*(.+)", RegexOption.IGNORE_CASE)
        )

        singleMatchFields.forEach { (label, regex) ->
            extractSingleMatch(query, regex)?.let { stringBuilder.append("$label: $it\n") }
        }

        val domainStatuses = extractMultipleMatches(query, Regex("Domain Status:\\s*(.+)", RegexOption.IGNORE_CASE))
            .map {
                it.replace(Regex("[()\\[\\]]"), "")
                    .replace("www.", "")
                    .lowercase()
                    .trim()
            }
            .toSet()

        if (domainStatuses.isNotEmpty()) {
            stringBuilder.append("Domain Status:\n")
            domainStatuses.forEach { stringBuilder.append("- $it\n") }
        }

        val nameServers = extractMultipleMatches(query, Regex("Name Server:\\s*(.+)", RegexOption.IGNORE_CASE))
            .map { it.lowercase() }
            .toSet()

        if (nameServers.isNotEmpty()) {
            stringBuilder.append("Name Servers:\n")
            nameServers.forEach { stringBuilder.append("- $it\n") }
        }
        return stringBuilder.toString().trim()
    }

    fun extractSingleMatch(query: String, regex: Regex): String? {
        return regex.find(query)?.groupValues?.get(1)?.trim()
    }

    fun extractMultipleMatches(query: String, regex: Regex): List<String> {
        return regex.findAll(query).map { it.groupValues[1].trim() }.toList()
    }

    fun checkURL(url: String): String {
        if (!isValidURL(url)) {
            throw InvalidURLException("Invalid URL: $url")
        }
        return cleanUrl(url)
    }

    fun cleanUrl(url: String): String {
        return url.replace(Regex("^(https?://)?(www\\.)?"), "")
            .replace(Regex("(\\.[a-z]{2,})([/?#].*)?$"), "$1")
    }

    fun isValidURL(url: String): Boolean {
        val validator = UrlValidator()
        return validator.isValid(url)
    }
}