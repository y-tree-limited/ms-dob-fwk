package com.ytree.dob_fwk.status.core.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory

internal object DOBStatusEvaluatorContext {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    val statusEvaluator: DOBStatusEvaluator = GatheringProviderDataStatusEvaluator

    operator fun invoke(relationship: ClientFinancialRelationship): DOBStatus =
        statusEvaluator(relationship).getOrDefault(DOBStatus.UNKNOWN)
            .apply { logging(relationship.status, this) }

    private fun logging(from: DOBStatus, to: DOBStatus) {
        if (from != to) log.info("Transitioned client financial relationship of status: $from to status: $to")
        else log.info("Status remains: $to")
    }
}