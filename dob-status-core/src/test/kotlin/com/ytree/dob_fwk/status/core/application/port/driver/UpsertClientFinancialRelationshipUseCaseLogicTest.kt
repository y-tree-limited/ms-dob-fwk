package com.ytree.dob_fwk.status.core.application.port.driver

import com.ytree.dob_fwk.status.core.application.port.driven.InMemoryClientFinancialRelationshipRepository
import com.ytree.dob_fwk.status.core.application.port.driver.gathering_provider_data_spec.GatheringProviderDetailsTestSpec
import org.junit.jupiter.api.DisplayName

@DisplayName("[UpsertClientFinancialRelationshipUseCaseLogicTest] Gathering Provider Details Specification")
class UpsertClientFinancialRelationshipUseCaseLogicTest: GatheringProviderDetailsTestSpec() {
    override fun requestBuilder(): ClientFinancialRelationshipUpsertRequestBuilder =
        ClientFinancialRelationshipUpsertRequestBuilder()
    override val upsertClientFinancialRelationshipUseCase: IUpsertClientFinancialRelationshipUseCase =
        UpsertClientFinancialRelationshipUseCase(InMemoryClientFinancialRelationshipRepository())
}