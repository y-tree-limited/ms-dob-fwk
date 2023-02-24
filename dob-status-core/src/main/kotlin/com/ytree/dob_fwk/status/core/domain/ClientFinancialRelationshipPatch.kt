package com.ytree.dob_fwk.status.core.domain

data class ClientFinancialRelationshipPatch(
    val relationshipNumber: FinancialRelationshipNumber?,
    val relationshipManager: FinancialRelationshipManager?,
    val selectedDataPipe: DataPipe?,
    val targetedDataPipe: DataPipe?,
    val loaContactEmail: LOAContactEmail?,
) {
    companion object
}