package com.assignment.customerApplication.model

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "CUSTOMER_APPLICATION")
data class CustomerApplication (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var applicationId: UUID = UUID.randomUUID(),

        @Column(name = "createdDateTime")
        var createdDateTime: LocalDateTime = LocalDateTime.now(),

        @Column(name = "isValidBankAccount")
        var isValidBankAccount: Boolean = false,

        @Column(name = "hasSufficientFunds")
        var hasSufficientFunds: Boolean = false,

        @Column(name = "hasCriminalHistory")
        var hasCriminalHistory: Boolean = false,

        @Column(name = "firstName")
        var firstName: String = "",

        @Column(name = "lastName")
        var lastName: String = "",

        @Column(name = "email")
        var email: String = "",

        @Column(name = "phoneNumber")
        var phoneNumber: String = "",

        @Column(name = "ssn")
        var ssn: String = "",

        @Column(name = "bankAccountNumber")
        var bankAcctNbr: String = "",

        @Column(name = "routingNumber")
        var routingNbr: String = "",

        @Column(name = "premiumAmount")
        var premiumAmount: Double = 0.0
)