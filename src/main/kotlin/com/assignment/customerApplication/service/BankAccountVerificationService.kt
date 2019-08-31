package com.assignment.customerApplication.service

import org.springframework.stereotype.Service

interface BankAccountVerificationService {
    fun validateAccount(bankAccountNumber: String, routingNumber: String): Boolean
    fun hasSufficientFunds(amount: Double, bankAcctNbr: String, routingNbr: String): Boolean
}

@Service
class StubBankAccountVerificationService : BankAccountVerificationService {
    override fun validateAccount(bankAccountNumber: String, routingNumber: String): Boolean {
        return true
    }

    override fun hasSufficientFunds(amount: Double, bankAcctNbr: String, routingNbr: String): Boolean {
        return true
    }
}