package com.ytree.dob_fwk.status.core.application.port.driven.model

import com.ytree.dob_fwk.status.core.domain.*

data class ClientFinancialRelationshipEntity(
    val id: FinancialRelationshipId,
    val clientId: ClientId,
    val providerId: ProviderId,
    val baseCurrency: BaseCurrency,
    val relationshipNumber: FinancialRelationshipNumber?,
    val relationshipManager: FinancialRelationshipManager?,
    val selectedDataPipe: DataPipe?,
    val targetedDataPipe: DataPipe?,
    val loaContactEmail: LOAContactEmail?,
    val status: DOBStatus
)