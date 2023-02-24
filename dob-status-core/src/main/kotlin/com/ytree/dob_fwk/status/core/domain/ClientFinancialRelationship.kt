package com.ytree.dob_fwk.status.core.domain

import com.ytree.dob_fwk.status.core.domain.DOBStatus.UNKNOWN

internal class ClientFinancialRelationship private constructor(
    val relationshipNumber: FinancialRelationshipNumber?,
    val relationshipManager: FinancialRelationshipManager?,
    val selectedDataPipe: DataPipe?,
    val targetedDataPipe: DataPipe?,
    val loaContactEmail: LOAContactEmail?,
    val status: DOBStatus
) {
    companion object {
        private val statusEvaluator = DOBStatusEvaluatorContext

        fun new(
            relationshipNumber: FinancialRelationshipNumber?,
            relationshipManager: FinancialRelationshipManager?,
            selectedDataPipe: DataPipe?,
            targetedDataPipe: DataPipe?,
            loaContactEmail: LOAContactEmail?,
            status: DOBStatus? = null
        ): ClientFinancialRelationship {
            status?.let {
                return ClientFinancialRelationship(
                    relationshipNumber,
                    relationshipManager,
                    selectedDataPipe,
                    targetedDataPipe,
                    loaContactEmail,
                    status
                )
            } ?: kotlin.run {
                val temp = ofUnknown(
                    relationshipNumber,
                    relationshipManager,
                    selectedDataPipe,
                    targetedDataPipe,
                    loaContactEmail
                )
                val newStatus = statusEvaluator(temp)
                return ClientFinancialRelationship(
                    temp.relationshipNumber,
                    temp.relationshipManager,
                    temp.selectedDataPipe,
                    temp.targetedDataPipe,
                    temp.loaContactEmail,
                    newStatus
                )
            }
        }

        /*
        We still need to calculate the correct status of a relationship that is not in our systems yet.
        So we're just creating one with an 'UNKNOWN' status, that will be processed in DOBStatusEvaluator
        to evaluate the correct status.
        */
        private fun ofUnknown(
            relationshipNumber: FinancialRelationshipNumber?,
            relationshipManager: FinancialRelationshipManager?,
            selectedDataPipe: DataPipe?,
            targetedDataPipe: DataPipe?,
            loaContactEmail: LOAContactEmail?
        ): ClientFinancialRelationship =
            ClientFinancialRelationship(
                relationshipNumber,
                relationshipManager,
                selectedDataPipe,
                targetedDataPipe,
                loaContactEmail,
                UNKNOWN
            )
    }

    fun update(patch: ClientFinancialRelationshipPatch): ClientFinancialRelationship {
        val toUpdate = patchRelationship(patch)
        val status = statusEvaluator(toUpdate)
        return toUpdate.patchStatus(status)
    }

    private fun patchRelationship(patch: ClientFinancialRelationshipPatch): ClientFinancialRelationship =
        copy(
            patch.relationshipNumber,
            patch.relationshipManager,
            patch.selectedDataPipe,
            patch.targetedDataPipe,
            patch.loaContactEmail
        )

    private fun patchStatus(status: DOBStatus): ClientFinancialRelationship = copy(status = status)

    private fun copy(
        relationshipNumber: FinancialRelationshipNumber? = this.relationshipNumber,
        relationshipManager: FinancialRelationshipManager? = this.relationshipManager,
        selectedDataPipe: DataPipe? = this.selectedDataPipe,
        targetedDataPipe: DataPipe? = this.targetedDataPipe,
        loaContactEmail: LOAContactEmail? = this.loaContactEmail,
        status: DOBStatus = this.status
    ): ClientFinancialRelationship =
        ClientFinancialRelationship(
            relationshipNumber,
            relationshipManager,
            selectedDataPipe,
            targetedDataPipe,
            loaContactEmail,
            status
        )

}