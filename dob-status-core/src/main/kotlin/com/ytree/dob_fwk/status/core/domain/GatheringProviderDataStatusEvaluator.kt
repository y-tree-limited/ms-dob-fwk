package com.ytree.dob_fwk.status.core.domain

internal object GatheringProviderDataStatusEvaluator: DOBStatusEvaluator {

    override fun invoke(relationship: ClientFinancialRelationship): Result<DOBStatus> =
        kotlin.runCatching {
            relationship.selectedDataPipe?.let {
                when (it.type) {
                    DataPipe.Type.PROVIDER_SPECIFIC -> providerDataPipeStatusEvaluator(relationship)
                    DataPipe.Type.CLIENT_SPECIFIC -> clientDataPipeStatusEvaluator(relationship)
                }
            } ?: DOBStatus.GATHERING_PROVIDER_DETAILS
        }

    private fun providerDataPipeStatusEvaluator(relationship: ClientFinancialRelationship): DOBStatus =
        with(relationship) {
            if ((targetedDataPipeIsNull() or loaContactEmailIsNull())
                or
                (rmIsNull() and rnIsNull()))
                DOBStatus.GATHERING_PROVIDER_DETAILS
            else throw IllegalArgumentException(errorMessage(relationship))
        }

    private fun clientDataPipeStatusEvaluator(relationship: ClientFinancialRelationship): DOBStatus =
        with(relationship) {
            if (targetedDataPipeIsNull())
                DOBStatus.GATHERING_PROVIDER_DETAILS
            else throw IllegalArgumentException(errorMessage(relationship))
        }

    private fun ClientFinancialRelationship.targetedDataPipeIsNull(): Boolean = targetedDataPipe == null
    private fun ClientFinancialRelationship.loaContactEmailIsNull(): Boolean = loaContactEmail == null
    private fun ClientFinancialRelationship.rmIsNull(): Boolean = relationshipManager == null
    private fun ClientFinancialRelationship.rnIsNull(): Boolean = relationshipNumber == null

    private fun errorMessage(r: ClientFinancialRelationship): String =
        "Can not evaluate 'Gathering Provider Data' status for relationship: $r"
}