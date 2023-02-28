package com.ytree.dob_fwk.status.core.application.port.driver.gathering_provider_data_spec.cases

import com.ytree.dob_fwk.status.core.application.port.driver.ClientFinancialRelationshipUpsertRequestBuilder
import com.ytree.dob_fwk.status.core.application.port.driver.UpsertClientFinancialRelationshipUseCaseTest
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipBody
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipResponse
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipUpsertRequest
import com.ytree.dob_fwk.status.core.domain.*
import com.ytree.dob_fwk.status.core.domain.DOBStatus.GATHERING_PROVIDER_DETAILS
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface AllRelationships: UpsertClientFinancialRelationshipUseCaseTest {

    fun requestBuilder(): ClientFinancialRelationshipUpsertRequestBuilder

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideMissingSelectedDataPipeRelationships")
    fun `creating financial relationship should be of status 'Gathering Provider Details' if selected data pipe is null`(
        description: String, givenUpsertRequest: ClientFinancialRelationshipUpsertRequest
    ) {
        val expected = ClientFinancialRelationshipResponse(
            givenUpsertRequest.relationshipHeader,
            ClientFinancialRelationshipBody(
                givenUpsertRequest.relationshipNumber,
                givenUpsertRequest.relationshipManager,
                givenUpsertRequest.selectedDataPipe,
                givenUpsertRequest.targetedDataPipe,
                givenUpsertRequest.loaContactEmail
            ),
            GATHERING_PROVIDER_DETAILS
        )

        val result = upsertClientFinancialRelationshipUseCase(givenUpsertRequest)

        assertThat(result, equalTo(Result.success(expected)))
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideMissingSelectedDataPipeRelationships")
    fun `updating financial relationship should be of status 'Gathering Provider Details' if selected data pipe is null`(
        description: String, givenUpsertRequest: ClientFinancialRelationshipUpsertRequest
    ) {
        val id = givenUpsertRequest.relationshipHeader.relationshipId
        financialRelationshipAlreadyProcessed(id)

        val expected = ClientFinancialRelationshipResponse(
            givenUpsertRequest.relationshipHeader,
            ClientFinancialRelationshipBody(
                givenUpsertRequest.relationshipNumber,
                givenUpsertRequest.relationshipManager,
                givenUpsertRequest.selectedDataPipe,
                givenUpsertRequest.targetedDataPipe,
                givenUpsertRequest.loaContactEmail
            ),
            GATHERING_PROVIDER_DETAILS
        )

        val result = upsertClientFinancialRelationshipUseCase(givenUpsertRequest)

        assertThat(result, equalTo(Result.success(expected)))
    }

    fun provideMissingSelectedDataPipeRelationships(): Stream<Arguments> = Stream.of(
        missingAll(),
        missingRelationshipNumber(),
        missingRelationshipManager(),
        missingTargetedDataPipe(),
        missingLoaContactEmail(),
        includingAllExceptSelectedDataPipe()
    )

    private fun missingAll(): Arguments = Arguments.of(
        "missing all",
        requestBuilder()
            .withMissingAll()
            .get()
    )
    private fun missingRelationshipNumber(): Arguments = Arguments.of(
        "missing relationship number",
        requestBuilder()
            .withMissingSelectedDataPipe()
            .withMissingRelationshipNumber()
            .get()
    )
    private fun missingRelationshipManager(): Arguments = Arguments.of(
        "missing relationship manager",
        requestBuilder()
            .withMissingSelectedDataPipe()
            .withMissingRelationshipManager()
            .get()
    )
    private fun missingTargetedDataPipe(): Arguments = Arguments.of(
        "missing targeted data pipe",
        requestBuilder()
            .withMissingSelectedDataPipe()
            .withMissingTargetedDataPipe()
            .get()
    )
    private fun missingLoaContactEmail(): Arguments = Arguments.of(
        "missing loa contact email",
        requestBuilder()
            .withMissingSelectedDataPipe()
            .withMissingLoaContactEmail()
            .get()
    )
    private fun includingAllExceptSelectedDataPipe(): Arguments = Arguments.of(
        "including all except selected data pipe",
        requestBuilder()
            .withMissingSelectedDataPipe()
            .get()
    )
    private fun financialRelationshipAlreadyProcessed(id: FinancialRelationshipId): ClientFinancialRelationshipResponse =
        upsertClientFinancialRelationshipUseCase(
            requestBuilder()
                .withMissingAll()
                .withId(id)
                .get()
        ).getOrThrow()
}