package com.assignment.customerApplication.service

import com.assignment.customerApplication.controller.BankAccountAddition
import com.assignment.customerApplication.controller.InitialApplicationInfo
import com.assignment.customerApplication.controller.Premium
import com.assignment.customerApplication.model.CustomerApplication
import com.assignment.customerApplication.repository.CustomerApplicationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerApplicationService {
    @Autowired
    lateinit var customerApplicationRepository: CustomerApplicationRepository

    @Autowired
    lateinit var bankAccountVerificationService: BankAccountVerificationService

    @Autowired
    lateinit var criminalHistoryVerificationService: CriminalHistoryVerificationService

    @Autowired
    lateinit var eventService: EventService

    fun createApplication(initialApplication: InitialApplicationInfo): CustomerApplication {
        val newCustomerApplication = CustomerApplication(
                firstName = initialApplication.firstName,
                lastName = initialApplication.lastName,
                email = initialApplication.email,
                phoneNumber = initialApplication.phoneNumber,
                ssn = initialApplication.ssn
        )

        newCustomerApplication.hasCriminalHistory = criminalHistoryVerificationService
                .validateCriminalActivity(newCustomerApplication.ssn)

        return customerApplicationRepository.save(newCustomerApplication)
    }

    fun addBankAccount(applicationId: UUID, bankAccountAddition: BankAccountAddition): Optional<CustomerApplication> =
            customerApplicationRepository.findById(applicationId).map {
                customerApplicationRepository.save(it.copy(
                        bankAcctNbr = bankAccountAddition.bankAcctNbr,
                        routingNbr = bankAccountAddition.routingNbr,
                        isValidBankAccount = bankAccountVerificationService.validateAccount(bankAccountAddition.bankAcctNbr,
                                bankAccountAddition.routingNbr))
            )}

    fun setPremiumAmount(applicationId: UUID, premiumDeposit: Premium): Optional<CustomerApplication> =
            customerApplicationRepository.findById(applicationId).filter { it.isValidBankAccount }.map {
                customerApplicationRepository.save(it.copy(
                        premiumAmount = premiumDeposit.premiumAmount,
                        hasSufficientFunds = bankAccountVerificationService.hasSufficientFunds(premiumDeposit.premiumAmount,
                                it.bankAcctNbr, it.routingNbr))
            )}

    fun completeApplicationAndPublish(applicationId: UUID): Optional<Boolean> =
            customerApplicationRepository.findById(applicationId)
                    .filter { it.isValidBankAccount && it.hasSufficientFunds && !it.hasCriminalHistory }
                    .map { eventService.publishCompletedApplication(it) }
}