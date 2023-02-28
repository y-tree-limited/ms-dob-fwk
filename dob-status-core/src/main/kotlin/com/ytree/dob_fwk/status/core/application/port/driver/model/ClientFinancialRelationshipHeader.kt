package com.ytree.dob_fwk.status.core.application.port.driver.model

import com.ytree.dob_fwk.status.core.domain.BaseCurrency
import com.ytree.dob_fwk.status.core.domain.ClientId
import com.ytree.dob_fwk.status.core.domain.FinancialRelationshipId
import com.ytree.dob_fwk.status.core.domain.ProviderId

data class ClientFinancialRelationshipHeader(
    val relationshipId: FinancialRelationshipId,
    val clientId: ClientId,
    val providerId: ProviderId,
    val baseCurrency: BaseCurrency
)