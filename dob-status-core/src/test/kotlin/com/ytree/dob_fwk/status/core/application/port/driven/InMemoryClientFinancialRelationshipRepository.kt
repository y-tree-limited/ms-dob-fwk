package com.ytree.dob_fwk.status.core.application.port.driven

import com.ytree.dob_fwk.status.core.application.port.driven.model.ClientFinancialRelationshipEntity
import com.ytree.dob_fwk.status.core.domain.FinancialRelationshipId

class InMemoryClientFinancialRelationshipRepository: ClientFinancialRelationshipRepository {

    private val store: MutableMap<FinancialRelationshipId, ClientFinancialRelationshipEntity> = mutableMapOf()

    override fun getClientFinancialRelationshipById(relationshipId: FinancialRelationshipId): Result<ClientFinancialRelationshipEntity?> =
        kotlin.runCatching { store[relationshipId] }

    override fun updateClientFinancialRelationship(entity: ClientFinancialRelationshipEntity): Result<Unit> =
        kotlin.runCatching { store[entity.id] = entity }

    override fun saveClientFinancialRelationship(entity: ClientFinancialRelationshipEntity): Result<Unit> =
        kotlin.runCatching {
            store.putIfAbsent(entity.id, entity)
            Unit
        }
}