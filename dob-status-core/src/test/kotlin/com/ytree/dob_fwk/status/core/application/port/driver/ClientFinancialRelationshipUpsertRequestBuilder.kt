package com.ytree.dob_fwk.status.core.application.port.driver

import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipHeader
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipUpsertRequest
import com.ytree.dob_fwk.status.core.domain.*
import java.util.*

class ClientFinancialRelationshipUpsertRequestBuilder {

    private val gbpCurrencyCode = Currency.getInstance(Locale.UK).numericCode.toLong()

    private var request = ClientFinancialRelationshipUpsertRequest(
        ClientFinancialRelationshipHeader(
            FinancialRelationshipId(UUID.randomUUID()),
            ClientId(UUID.randomUUID()),
            ProviderId(UUID.randomUUID()),
            BaseCurrency(gbpCurrencyCode)
        ),
        FinancialRelationshipNumber("some-number"),
        FinancialRelationshipManager("some-manager"),
        DataPipe.values().random(),
        DataPipe.values().random(),
        LOAContactEmail("name@email-provider.org")
    )

    fun withId(id: FinancialRelationshipId): ClientFinancialRelationshipUpsertRequestBuilder = apply {
        request = request.copy(request.relationshipHeader.copy(relationshipId = id))
    }

    fun withSelectedDataPipe(dataPipe: DataPipe): ClientFinancialRelationshipUpsertRequestBuilder = apply {
        request = request.copy(selectedDataPipe = dataPipe)
    }

    fun withMissingSelectedDataPipe(): ClientFinancialRelationshipUpsertRequestBuilder = apply {
        request = request.copy(selectedDataPipe = null)
    }

    fun withMissingRelationshipNumber(): ClientFinancialRelationshipUpsertRequestBuilder = apply {
        request = request.copy(relationshipNumber = null)
    }

    fun withMissingRelationshipManager(): ClientFinancialRelationshipUpsertRequestBuilder = apply {
        request = request.copy(relationshipManager = null)
    }

    fun withMissingTargetedDataPipe(): ClientFinancialRelationshipUpsertRequestBuilder = apply {
        request = request.copy(targetedDataPipe = null)
    }

    fun withMissingLoaContactEmail(): ClientFinancialRelationshipUpsertRequestBuilder = apply {
        request = request.copy(loaContactEmail = null)
    }

    fun withMissingAll(): ClientFinancialRelationshipUpsertRequestBuilder = apply {
        request = request.copy(
            relationshipNumber = null,
            relationshipManager = null,
            selectedDataPipe = null,
            targetedDataPipe = null,
            loaContactEmail = null
        )
    }

    fun get(): ClientFinancialRelationshipUpsertRequest = request
}