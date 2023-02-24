package com.ytree.dob_fwk.status.core.application.port.driver.model

import com.ytree.dob_fwk.status.core.domain.DataPipe
import com.ytree.dob_fwk.status.core.domain.FinancialRelationshipManager
import com.ytree.dob_fwk.status.core.domain.FinancialRelationshipNumber
import com.ytree.dob_fwk.status.core.domain.LOAContactEmail

data class ClientFinancialRelationshipUpsertRequest(
    val relationshipHeader: ClientFinancialRelationshipHeader,
    val relationshipNumber: FinancialRelationshipNumber?,
    val relationshipManager: FinancialRelationshipManager?,
    val selectedDataPipe: DataPipe?,
    val targetedDataPipe: DataPipe?,
    val loaContactEmail: LOAContactEmail?
)