package com.ytree.dob_fwk.status.core.application.port.driver

import com.ytree.dob_fwk.status.core.application.port.driven.model.ClientFinancialRelationshipEntity
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipBody
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipHeader
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipResponse
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipUpsertRequest
import com.ytree.dob_fwk.status.core.domain.ClientFinancialRelationship
import com.ytree.dob_fwk.status.core.domain.ClientFinancialRelationshipPatch

internal fun ClientFinancialRelationship.Companion.newFrom(request: ClientFinancialRelationshipUpsertRequest): ClientFinancialRelationship =
    new(
        request.relationshipNumber,
        request.relationshipManager,
        request.selectedDataPipe,
        request.targetedDataPipe,
        request.loaContactEmail
    )

internal fun ClientFinancialRelationshipPatch.Companion.from(request: ClientFinancialRelationshipUpsertRequest): ClientFinancialRelationshipPatch =
    ClientFinancialRelationshipPatch(
        request.relationshipNumber,
        request.relationshipManager,
        request.selectedDataPipe,
        request.targetedDataPipe,
        request.loaContactEmail
    )

internal fun ClientFinancialRelationshipResponse.Companion.from(entity: ClientFinancialRelationshipEntity): ClientFinancialRelationshipResponse =
    ClientFinancialRelationshipResponse(
        ClientFinancialRelationshipHeader(entity.id, entity.clientId, entity.providerId, entity.baseCurrency),
        ClientFinancialRelationshipBody(
            entity.relationshipNumber,
            entity.relationshipManager,
            entity.selectedDataPipe,
            entity.targetedDataPipe,
            entity.loaContactEmail
        ),
        entity.status
    )

/*
Helper extension function to flatten Kotlin's Result<T> model

When a transform function within a Result<T> itself returns another Result<R>, we would get a Result<Result<R>>.
flatMap() in the above scenario would flatten the type to Result<R>.
 */
internal fun <T, R> Result<T>.flatMap(f: (T) -> Result<R>): Result<R> = kotlin.runCatching { map(f).getOrThrow().getOrThrow() }