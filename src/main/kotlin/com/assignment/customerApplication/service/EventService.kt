package com.assignment.customerApplication.service

import com.assignment.customerApplication.model.CustomerApplication
import org.springframework.stereotype.Service

/**
 * The below interface and stub is utilized to mimic posting events to an event stream
 */
interface EventService {
    fun publishCompletedApplication(customerApplication: CustomerApplication): Boolean
}

@Service
class StubEventService : EventService {
    override fun publishCompletedApplication(customerApplication: CustomerApplication): Boolean {
        return true
    }
}