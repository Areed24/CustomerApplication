package com.assignment.customerApplication.controller

import com.assignment.customerApplication.model.CustomerApplication
import com.assignment.customerApplication.service.CustomerApplicationService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest
class CustomerApplicationControllerTests {

    @Autowired
    private lateinit var mvc: MockMvc

    @MockkBean
    private lateinit var customerApplicationService: CustomerApplicationService

    private var objectMapper = ObjectMapper()

    private val initalApplicationInfo = InitialApplicationInfo(
            firstName = "Dan",
            lastName = "Smith",
            email = "dansmith@gmail.com",
            phoneNumber = "111-222-3333",
            ssn = "001-02-0003"
    )

    private val newCustomerApplication = CustomerApplication(
            firstName = initalApplicationInfo.firstName,
            lastName = initalApplicationInfo.lastName,
            email = initalApplicationInfo.email,
            phoneNumber = initalApplicationInfo.phoneNumber,
            ssn = initalApplicationInfo.ssn
    )

    private val bankAccountInfo = BankAccountAddition(bankAcctNbr = "1234566789", routingNbr = "987625147900")

    private val premium = Premium(premiumAmount = 500.00)

    @Test
    fun `successfully creates application through create endpoint`() {
        every { customerApplicationService.createApplication(initalApplicationInfo) } returns newCustomerApplication

        mvc.perform(post("/application").content(objectMapper.writeValueAsString(initalApplicationInfo))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated)
    }

    @Test
    fun `successfully adds bank account to application`() {
        every { customerApplicationService.addBankAccount(newCustomerApplication.applicationId, bankAccountInfo) } returns
                Optional.of(newCustomerApplication.copy(bankAcctNbr = bankAccountInfo.bankAcctNbr, routingNbr = bankAccountInfo.routingNbr))

        mvc.perform(put("/application/{id}/addBankAccount", newCustomerApplication.applicationId)
                .content(objectMapper.writeValueAsString(bankAccountInfo))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
    }

    @Test
    fun `fails to add bank account to application`() {
        every { customerApplicationService.addBankAccount(any(), any()) } returns Optional.empty()

        mvc.perform(put("/application/{id}/addBankAccount", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(bankAccountInfo))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `successfully sets premium amount on application`() {
        every { customerApplicationService.setPremiumAmount(newCustomerApplication.applicationId, premium) } returns
                Optional.of(newCustomerApplication.copy(premiumAmount = premium.premiumAmount, hasSufficientFunds = true))

        mvc.perform(put("/application/{id}/premiumAmount", newCustomerApplication.applicationId)
                .content(objectMapper.writeValueAsString(premium))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
    }

    @Test
    fun `fails to set premium amount on application`() {
        every { customerApplicationService.setPremiumAmount(any(), any()) } returns Optional.empty()

        mvc.perform(put("/application/{id}/premiumAmount", UUID.randomUUID())
                .content(objectMapper.writeValueAsString(premium))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `successfully completes and publishes application`() {
        every { customerApplicationService.completeApplicationAndPublish(newCustomerApplication.applicationId) } returns
                Optional.of(true)

        mvc.perform(put("/application/{id}/complete", newCustomerApplication.applicationId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk)
    }

    @Test
    fun `fails to complete and publish application`() {
        every { customerApplicationService.completeApplicationAndPublish(any()) } returns Optional.empty()

        mvc.perform(put("/application/{id}/complete", newCustomerApplication.applicationId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound)
    }

}