package com.ytree.dob_fwk.status.core.application.port.driver.model

import com.ytree.dob_fwk.status.core.domain.DOBStatus

data class ClientFinancialRelationshipResponse(
    val header: ClientFinancialRelationshipHeader,
    val body: ClientFinancialRelationshipBody,
    val status: DOBStatus
) {
    companion object
}