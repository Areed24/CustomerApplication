package com.assignment.customerApplication.service

import com.assignment.customerApplication.controller.BankAccountAddition
import com.assignment.customerApplication.controller.Premium
import com.assignment.customerApplication.model.CustomerApplication
import com.assignment.customerApplication.repository.CustomerApplicationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Transactional
@SpringBootTest
class CustomerApplicationServiceTests {
    @Autowired
    lateinit var customerApplicationService: CustomerApplicationService

    @Autowired
    lateinit var customerApplicationRepository: CustomerApplicationRepository

    private val bankAccount = BankAccountAddition("12345678", "0000007651")

    private val premium = Premium(500.00)

    lateinit var savedApplication: CustomerApplication

    @BeforeEach
    internal fun setup() {
        savedApplication = customerApplicationRepository.save(CustomerApplication(
                firstName = "Micheal",
                lastName = "Scott",
                email = "mscott@hotmail.com",
                phoneNumber = "111-222-333"))
    }

    @Test
    fun `create customer application`() {
        val retrievedApplication = customerApplicationRepository.findById(savedApplication.applicationId).get()

        assertThat(savedApplication).isEqualTo(retrievedApplication)
    }

    @Test
    fun `successfully add bank account to application`() {
        val updatedApplication = customerApplicationService.addBankAccount(savedApplication.applicationId, bankAccount).get()

        assertThat(updatedApplication.bankAcctNbr).isEqualTo(bankAccount.bankAcctNbr)
        assertThat(updatedApplication.routingNbr).isEqualTo(bankAccount.routingNbr)
        assertThat(updatedApplication.isValidBankAccount).isEqualTo(true)
    }

    @Test
    fun `successfully set premium amount on application`() {
        customerApplicationService.addBankAccount(savedApplication.applicationId, bankAccount)
        val updatedApplicationWithPremiumAmount = customerApplicationService
                .setPremiumAmount(savedApplication.applicationId, premium).get()

        assertThat(updatedApplicationWithPremiumAmount.premiumAmount).isEqualTo(premium.premiumAmount)
        assertThat(updatedApplicationWithPremiumAmount.hasSufficientFunds).isEqualTo(true)
    }

    @Test
    fun `fail to set premium amount on application`() {
        val applicationWithNoBankAccount = customerApplicationService.setPremiumAmount(savedApplication.applicationId, premium)

        assertThat(applicationWithNoBankAccount).isEqualTo(Optional.empty<CustomerApplication>())
    }

    @Test
    fun `successfully complete application and publish`() {
        customerApplicationService.addBankAccount(savedApplication.applicationId, bankAccount)
        customerApplicationService.setPremiumAmount(savedApplication.applicationId, premium)
        val isSuccessfullyPublished = customerApplicationService.completeApplicationAndPublish(savedApplication.applicationId).get()

        assertThat(isSuccessfullyPublished).isEqualTo(true)
    }

    @Test
    fun `fail to complete application and publish`() {
        customerApplicationService.addBankAccount(savedApplication.applicationId, bankAccount)
        val isSuccessfullyPublished = customerApplicationService.completeApplicationAndPublish(savedApplication.applicationId)

        assertThat(isSuccessfullyPublished).isEqualTo(Optional.empty<CustomerApplication>())
    }
}