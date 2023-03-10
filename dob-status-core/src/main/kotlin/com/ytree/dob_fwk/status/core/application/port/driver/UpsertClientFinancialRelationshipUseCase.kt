package com.ytree.dob_fwk.status.core.application.port.driver

import com.ytree.dob_fwk.status.core.application.port.driven.ClientFinancialRelationshipRepository
import com.ytree.dob_fwk.status.core.application.port.driven.from
import com.ytree.dob_fwk.status.core.application.port.driven.toEntity
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipResponse
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipUpsertRequest
import com.ytree.dob_fwk.status.core.domain.ClientFinancialRelationship
import com.ytree.dob_fwk.status.core.domain.ClientFinancialRelationshipPatch
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UpsertClientFinancialRelationshipUseCase(private val repository: ClientFinancialRelationshipRepository): IUpsertClientFinancialRelationshipUseCase {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override operator fun invoke(request: ClientFinancialRelationshipUpsertRequest): Result<ClientFinancialRelationshipResponse> {
        log.info("Requested to upsert client financial relationship with id: ${request.relationshipHeader.relationshipId} and body: $request")
        return repository.getClientFinancialRelationshipById(request.relationshipHeader.relationshipId).flatMap {
            it?.let { entity ->
                val financialRelationship = ClientFinancialRelationship.from(entity)
                val patch = ClientFinancialRelationshipPatch.from(request)
                val updatedFinancialRelationship = financialRelationship.update(patch)
                val updatedEntity = (request.relationshipHeader to updatedFinancialRelationship).toEntity()
                repository.updateClientFinancialRelationship(updatedEntity)
                    .map { _ -> ClientFinancialRelationshipResponse.from(updatedEntity) }
            } ?: kotlin.run {
                val financialRelationship = ClientFinancialRelationship.newFrom(request)
                val entity = (request.relationshipHeader to financialRelationship).toEntity()
                repository.saveClientFinancialRelationship(entity)
                    .map { _ -> ClientFinancialRelationshipResponse.from(entity) }
            }
        }.apply(::logging)
    }

    private fun logging(result: Result<ClientFinancialRelationshipResponse>) =
        result
            .onSuccess { response -> log.info("Responding with success: $response") }
            .onFailure { t -> log.error("Responding with error: $t") }

}