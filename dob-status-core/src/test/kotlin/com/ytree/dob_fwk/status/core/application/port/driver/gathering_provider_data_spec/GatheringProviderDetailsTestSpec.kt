package com.ytree.dob_fwk.status.core.application.port.driver.gathering_provider_data_spec

import com.ytree.dob_fwk.status.core.application.port.driver.ClientFinancialRelationshipUpsertRequestBuilder
import com.ytree.dob_fwk.status.core.application.port.driver.gathering_provider_data_spec.cases.AllRelationships
import com.ytree.dob_fwk.status.core.application.port.driver.gathering_provider_data_spec.cases.ClientSpecificRelationships
import com.ytree.dob_fwk.status.core.application.port.driver.gathering_provider_data_spec.cases.ProviderSpecificRelationships

/**
 * This is a namespace to gather all the relevant tests of the target specification.
 *
 * Ideally, concrete tests would implement the specification, and thus will need to specify
 * an implementation of the system under test defined in [UpsertClientFinancialRelationshipUseCaseTest].
 *
 * All the tests that the specification inherits, they also inherit the interface defining
 * what system is under test.
 */
abstract class GatheringProviderDetailsTestSpec: AllRelationships, ProviderSpecificRelationships, ClientSpecificRelationships {
    abstract override fun requestBuilder(): ClientFinancialRelationshipUpsertRequestBuilder
}
