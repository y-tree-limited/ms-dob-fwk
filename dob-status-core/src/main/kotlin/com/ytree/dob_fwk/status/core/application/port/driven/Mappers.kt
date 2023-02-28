package com.ytree.dob_fwk.status.core.application.port.driven

import com.ytree.dob_fwk.status.core.application.port.driven.model.ClientFinancialRelationshipEntity
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipHeader
import com.ytree.dob_fwk.status.core.domain.ClientFinancialRelationship

internal fun ClientFinancialRelationship.Companion.from(entity: ClientFinancialRelationshipEntity): ClientFinancialRelationship =
    new(
        entity.relationshipNumber,
        entity.relationshipManager,
        entity.selectedDataPipe,
        entity.targetedDataPipe,
        entity.loaContactEmail,
        entity.status
    )

internal fun Pair<ClientFinancialRelationshipHeader, ClientFinancialRelationship>.toEntity(): ClientFinancialRelationshipEntity =
    ClientFinancialRelationshipEntity(
        first.relationshipId,
        first.clientId,
        first.providerId,
        first.baseCurrency,
        second.relationshipNumber,
        second.relationshipManager,
        second.selectedDataPipe,
        second.targetedDataPipe,
        second.loaContactEmail,
        second.status
    )