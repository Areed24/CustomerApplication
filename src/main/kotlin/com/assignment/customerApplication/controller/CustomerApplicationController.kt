package com.assignment.customerApplication.controller

import com.assignment.customerApplication.model.CustomerApplication
import com.assignment.customerApplication.service.CustomerApplicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/application")
class CustomerApplicationController() {
    @Autowired
    lateinit var customerApplicationService: CustomerApplicationService

    @PostMapping
    fun createApplication(@RequestBody application: InitialApplicationInfo): ResponseEntity<CustomerApplication> {
        val saved = customerApplicationService.createApplication(application)
        return ResponseEntity.created(URI("/application/${saved.applicationId}")).body(saved)
    }

    @PutMapping("/{id}/addBankAccount")
    fun addBankAccount(@PathVariable id: UUID, @RequestBody bankAccountAddition: BankAccountAddition): ResponseEntity<CustomerApplication> =
            customerApplicationService.addBankAccount(id, bankAccountAddition).map {
                ResponseEntity.ok().body(it)
            }.orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}/premiumAmount")
    fun setPremiumAmount(@PathVariable id: UUID, @RequestBody premiumAmount: Premium): ResponseEntity<CustomerApplication> =
            customerApplicationService.setPremiumAmount(id, premiumAmount).map {
                ResponseEntity.ok().body(it)
            }.orElse(ResponseEntity.notFound().build())

    @PutMapping("/{id}/complete")
    fun completeApplication(@PathVariable id: UUID): ResponseEntity<Boolean> =
        customerApplicationService.completeApplicationAndPublish(id).map {
            ResponseEntity.ok().body(it)
        }.orElse(ResponseEntity.notFound().build())
    }

data class InitialApplicationInfo(var firstName: String, var lastName: String, var email: String, var phoneNumber: String,
                              var ssn: String)

data class BankAccountAddition(var bankAcctNbr: String, var routingNbr: String)

data class Premium(var premiumAmount: Double)