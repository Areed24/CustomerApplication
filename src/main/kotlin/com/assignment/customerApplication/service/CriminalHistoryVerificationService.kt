package com.assignment.customerApplication.service

import org.springframework.stereotype.Service

interface CriminalHistoryVerificationService {
    fun validateCriminalActivity(ssn: String): Boolean
}

@Service
class StubCriminalHistoryVerificationService : CriminalHistoryVerificationService {
    override fun validateCriminalActivity(ssn: String): Boolean {
        return false
    }
}