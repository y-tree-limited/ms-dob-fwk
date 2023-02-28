package com.ytree.dob_fwk.status.core.application.port.driver.gathering_provider_data_spec.cases

import com.ytree.dob_fwk.status.core.application.port.driver.ClientFinancialRelationshipUpsertRequestBuilder
import com.ytree.dob_fwk.status.core.application.port.driver.UpsertClientFinancialRelationshipUseCaseTest
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipBody
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipResponse
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipUpsertRequest
import com.ytree.dob_fwk.status.core.domain.DOBStatus.GATHERING_PROVIDER_DETAILS
import com.ytree.dob_fwk.status.core.domain.DataPipe
import com.ytree.dob_fwk.status.core.domain.FinancialRelationshipId
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface ClientSpecificRelationships: UpsertClientFinancialRelationshipUseCaseTest {

    fun requestBuilder(): ClientFinancialRelationshipUpsertRequestBuilder

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideClientSpecificRelationshipsMissingRequiredDataPoints")
    fun `creating client-specific financial relationship should be of status 'Gathering Provider Details'`(
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
    @MethodSource("provideClientSpecificRelationshipsMissingRequiredDataPoints")
    fun `updating client-specific financial relationship should be of status 'Gathering Provider Details'`(
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

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideClientSpecificRelationshipsMissingNotRequiredDataPoints")
    fun `updating client-specific financial relationship should NOT be of status 'Gathering Provider Details'`(
        description: String, givenUpsertRequest: ClientFinancialRelationshipUpsertRequest
    ) {
        val id = givenUpsertRequest.relationshipHeader.relationshipId
        financialRelationshipAlreadyProcessed(id)

        val result = upsertClientFinancialRelationshipUseCase(givenUpsertRequest)

        assertThat(result.isSuccess, equalTo(true))
        val relationshipResponse = result.getOrThrow()
        assertThat(relationshipResponse.status, not(GATHERING_PROVIDER_DETAILS))
    }

    fun provideClientSpecificRelationshipsMissingRequiredDataPoints(): Stream<Arguments> = Stream.of(
        openBankingMissingAll(),
        clientPortalMissingAll(),
        csvPdfFromClientMissingAll(),
        clientUpdateViaAppMissingAll(),
        openBankingMissingTargetedDataPipe(),
        clientPortalMissingTargetedDataPipe(),
        csvPdfFromClientMissingTargetedDataPipe(),
        clientPortalMissingTargetedDataPipe()
    )

    fun provideClientSpecificRelationshipsMissingNotRequiredDataPoints(): Stream<Arguments> = Stream.of(
        openBankingMissingLoaContactEmail(),
        clientPortalMissingLoaContactEmail(),
        csvPdfFromClientMissingLoaContactEmail(),
        clientUpdateViaAppMissingLoaContactEmail(),
        openBankingMissingRelationshipNumber(),
        clientPortalMissingRelationshipNumber(),
        csvPdfFromClientMissingRelationshipNumber(),
        clientUpdateViaAppMissingRelationshipNumber(),
        openBankingMissingRelationshipManager(),
        clientPortalMissingRelationshipManager(),
        csvPdfFromClientMissingRelationshipManager(),
        clientUpdateViaAppMissingRelationshipManager(),
        openBankingMissingRelationshipNumberAndManager(),
        clientPortalMissingRelationshipNumberAndManager(),
        csvPdfFromClientMissingRelationshipNumberAndManager(),
        clientUpdateViaAppMissingRelationshipNumberAndManager()
    )

    private fun openBankingMissingAll(): Arguments = Arguments.of(
        "'Open Banking' selected data pipe - missing all",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(DataPipe.OPEN_BANKING)
            .get()
    )
    private fun clientPortalMissingAll(): Arguments = Arguments.of(
        "'Client Portal' selected data pipe - missing all",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(DataPipe.CLIENT_PORTAL)
            .get()
    )
    private fun csvPdfFromClientMissingAll(): Arguments = Arguments.of(
        "'CSV/PDF from client' selected data pipe - missing all",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(DataPipe.CSV_PDF_FROM_CLIENT)
            .get()
    )
    private fun clientUpdateViaAppMissingAll(): Arguments = Arguments.of(
        "'Client update via app' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(DataPipe.CLIENT_UPDATE_VIA_APP)
            .get()
    )
    private fun openBankingMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'Open Banking' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.OPEN_BANKING)
            .withMissingTargetedDataPipe()
            .get()
    )
    private fun clientPortalMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'Client Portal' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CLIENT_PORTAL)
            .withMissingTargetedDataPipe()
            .get()
    )
    private fun csvPdfFromClientMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'CSV/PDF from client' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CSV_PDF_FROM_CLIENT)
            .withMissingTargetedDataPipe()
            .get()
    )
    private fun clientUpdateViaAppMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'Client update via app' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CLIENT_UPDATE_VIA_APP)
            .withMissingTargetedDataPipe()
            .get()
    )
    private fun openBankingMissingLoaContactEmail(): Arguments = Arguments.of(
        "'Open Banking' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.OPEN_BANKING)
            .withMissingLoaContactEmail()
            .get()
    )
    private fun clientPortalMissingLoaContactEmail(): Arguments = Arguments.of(
        "'Client Portal' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CLIENT_PORTAL)
            .withMissingLoaContactEmail()
            .get()
    )
    private fun csvPdfFromClientMissingLoaContactEmail(): Arguments = Arguments.of(
        "'CSV/PDF from client' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CSV_PDF_FROM_CLIENT)
            .withMissingLoaContactEmail()
            .get()
    )
    private fun clientUpdateViaAppMissingLoaContactEmail(): Arguments = Arguments.of(
        "'Client update via app' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CLIENT_UPDATE_VIA_APP)
            .withMissingLoaContactEmail()
            .get()
    )
    private fun openBankingMissingRelationshipNumber(): Arguments = Arguments.of(
        "'Open Banking' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.OPEN_BANKING)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun clientPortalMissingRelationshipNumber(): Arguments = Arguments.of(
        "'Client Portal' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CLIENT_PORTAL)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun csvPdfFromClientMissingRelationshipNumber(): Arguments = Arguments.of(
        "'CSV/PDF from client' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CSV_PDF_FROM_CLIENT)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun clientUpdateViaAppMissingRelationshipNumber(): Arguments = Arguments.of(
        "'Client update via app' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CLIENT_UPDATE_VIA_APP)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun openBankingMissingRelationshipManager(): Arguments = Arguments.of(
        "'Open Banking' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.OPEN_BANKING)
            .withMissingRelationshipManager()
            .get()
    )
    private fun clientPortalMissingRelationshipManager(): Arguments = Arguments.of(
        "'Client Portal' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CLIENT_PORTAL)
            .withMissingRelationshipManager()
            .get()
    )
    private fun csvPdfFromClientMissingRelationshipManager(): Arguments = Arguments.of(
        "'CSV/PDF from client' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CSV_PDF_FROM_CLIENT)
            .withMissingRelationshipManager()
            .get()
    )
    private fun clientUpdateViaAppMissingRelationshipManager(): Arguments = Arguments.of(
        "'Client update via app' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CLIENT_UPDATE_VIA_APP)
            .withMissingRelationshipManager()
            .get()
    )
    private fun openBankingMissingRelationshipNumberAndManager(): Arguments = Arguments.of(
        "'Open Banking' selected data pipe - missing relationship number and manager",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.OPEN_BANKING)
            .withMissingRelationshipNumber()
            .withMissingRelationshipManager()
            .get()
    )
    private fun clientPortalMissingRelationshipNumberAndManager(): Arguments = Arguments.of(
        "'Client Portal' selected data pipe - missing relationship number and manager",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CLIENT_PORTAL)
            .withMissingRelationshipNumber()
            .withMissingRelationshipManager()
            .get()
    )
    private fun csvPdfFromClientMissingRelationshipNumberAndManager(): Arguments = Arguments.of(
        "'CSV/PDF from client' selected data pipe - missing relationship number and manager",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CSV_PDF_FROM_CLIENT)
            .withMissingRelationshipNumber()
            .withMissingRelationshipManager()
            .get()
    )
    private fun clientUpdateViaAppMissingRelationshipNumberAndManager(): Arguments = Arguments.of(
        "'Client update via app' selected data pipe - missing relationship number and manager",
        requestBuilder()
            .withSelectedDataPipe(DataPipe.CLIENT_UPDATE_VIA_APP)
            .withMissingRelationshipNumber()
            .withMissingRelationshipManager()
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