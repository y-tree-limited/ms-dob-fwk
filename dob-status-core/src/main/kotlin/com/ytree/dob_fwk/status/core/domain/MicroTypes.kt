package com.ytree.dob_fwk.status.core.domain

import java.util.UUID
import java.util.Currency

data class FinancialRelationshipId(val value: UUID)
data class ClientId(val value: UUID)
data class ProviderId(val value: UUID)
data class BaseCurrency(val id: Long)

data class FinancialRelationshipNumber(val value: String)
data class FinancialRelationshipManager(val value: String)

data class LOAContactEmail(val value: String)