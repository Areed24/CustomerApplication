package com.assignment.customerApplication.repository

import com.assignment.customerApplication.model.CustomerApplication
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CustomerApplicationRepository : JpaRepository<CustomerApplication, UUID>