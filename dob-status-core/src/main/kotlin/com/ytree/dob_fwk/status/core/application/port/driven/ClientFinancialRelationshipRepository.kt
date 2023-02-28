package com.ytree.dob_fwk.status.core.application.port.driven

import com.ytree.dob_fwk.status.core.application.port.driven.model.ClientFinancialRelationshipEntity
import com.ytree.dob_fwk.status.core.domain.FinancialRelationshipId

interface ClientFinancialRelationshipRepository {
    fun getClientFinancialRelationshipById(relationshipId: FinancialRelationshipId): Result<ClientFinancialRelationshipEntity?>
    fun updateClientFinancialRelationship(entity: ClientFinancialRelationshipEntity): Result<Unit>
    fun saveClientFinancialRelationship(entity: ClientFinancialRelationshipEntity): Result<Unit>
}