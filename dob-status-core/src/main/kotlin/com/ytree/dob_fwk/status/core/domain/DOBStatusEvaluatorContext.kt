package com.ytree.dob_fwk.status.core.domain

internal object DOBStatusEvaluatorContext {

    val statusEvaluator: DOBStatusEvaluator = GatheringProviderDataStatusEvaluator

    operator fun invoke(relationship: ClientFinancialRelationship): DOBStatus =
        statusEvaluator(relationship).getOrDefault(DOBStatus.UNKNOWN)
}