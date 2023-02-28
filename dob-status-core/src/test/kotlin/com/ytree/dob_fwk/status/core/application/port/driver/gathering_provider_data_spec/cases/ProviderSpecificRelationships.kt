package com.ytree.dob_fwk.status.core.application.port.driver.gathering_provider_data_spec.cases

import com.ytree.dob_fwk.status.core.application.port.driver.ClientFinancialRelationshipUpsertRequestBuilder
import com.ytree.dob_fwk.status.core.application.port.driver.UpsertClientFinancialRelationshipUseCaseTest
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipBody
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipResponse
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipUpsertRequest
import com.ytree.dob_fwk.status.core.domain.*
import com.ytree.dob_fwk.status.core.domain.DOBStatus.GATHERING_PROVIDER_DETAILS
import com.ytree.dob_fwk.status.core.domain.DataPipe.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface ProviderSpecificRelationships: UpsertClientFinancialRelationshipUseCaseTest {

    fun requestBuilder(): ClientFinancialRelationshipUpsertRequestBuilder

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideProviderSpecificRelationshipsMissingRequiredDataPoints")
    fun `creating provider-specific financial relationship should be of status 'Gathering Provider Details'`(
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
    @MethodSource("provideProviderSpecificRelationshipsMissingRequiredDataPoints")
    fun `updating provider-specific financial relationship should be of status 'Gathering Provider Details'`(
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
    @MethodSource("provideProviderSpecificRelationshipsMissingNotRequiredDataPoints")
    fun `updating provider-specific financial relationship should NOT be of status 'Gathering Provider Details'`(
        description: String, givenUpsertRequest: ClientFinancialRelationshipUpsertRequest
    ) {
        val id = givenUpsertRequest.relationshipHeader.relationshipId
        financialRelationshipAlreadyProcessed(id)

        val result = upsertClientFinancialRelationshipUseCase(givenUpsertRequest)

        assertThat(result.isSuccess, equalTo(true))
        val relationshipResponse = result.getOrThrow()
        assertThat(relationshipResponse.status, not(GATHERING_PROVIDER_DETAILS))
    }

    fun provideProviderSpecificRelationshipsMissingRequiredDataPoints(): Stream<Arguments> = Stream.of(
        sftpMissingAll(),
        apiMissingAll(),
        csvPdfAutoChasedMissingAll(),
        csvPdfManuallyChasedMissingAll(),
        informationOnlyPortalMissingAll(),
        adviserPortalManualMissingAll(),
        adviserPortalScrapeMissingAll(),
        sftpMissingTargetedDataPipe(),
        apiMissingTargetedDataPipe(),
        csvPdfAutoChasedMissingTargetedDataPipe(),
        csvPdfManuallyChasedMissingTargetedDataPipe(),
        informationOnlyPortalMissingTargetedDataPipe(),
        adviserPortalManualMissingTargetedDataPipe(),
        adviserPortalScrapeMissingTargetedDataPipe(),
        sftpMissingLoaContactEmail(),
        apiMissingLoaContactEmail(),
        csvPdfAutoChasedMissingLoaContactEmail(),
        csvPdfManuallyChasedMissingLoaContactEmail(),
        informationOnlyPortalMissingLoaContactEmail(),
        adviserPortalManualMissingLoaContactEmail(),
        adviserPortalScrapeMissingLoaContactEmail(),
        sftpMissingRelationshipManagerAndNumber(),
        apiMissingRelationshipManagerAndNumber(),
        csvPdfAutoChasedMissingRelationshipManagerAndNumber(),
        csvPdfManuallyChasedMissingRelationshipManagerAndNumber(),
        informationOnlyPortalMissingRelationshipManagerAndNumber(),
        adviserPortalManualMissingRelationshipManagerAndNumber(),
        adviserPortalScrapeMissingRelationshipManagerAndNumber(),
    )

    fun provideProviderSpecificRelationshipsMissingNotRequiredDataPoints(): Stream<Arguments> = Stream.of(
        sftpMissingNone(),
        apiMissingNone(),
        csvPdfAutoChasedMissingNone(),
        csvPdfManuallyChasedMissingNone(),
        informationOnlyPortalMissingNone(),
        adviserPortalManualMissingNone(),
        adviserPortalScrapeMissingNone(),
        sftpMissingRelationshipNumber(),
        apiMissingRelationshipNumber(),
        csvPdfAutoChasedMissingRelationshipNumber(),
        csvPdfManuallyChasedMissingRelationshipNumber(),
        informationOnlyPortalMissingRelationshipNumber(),
        adviserPortalManualMissingRelationshipNumber(),
        adviserPortalScrapeMissingRelationshipNumber(),
        sftpMissingRelationshipManager(),
        apiMissingRelationshipManager(),
        csvPdfAutoChasedMissingRelationshipManager(),
        csvPdfManuallyChasedMissingRelationshipManager(),
        informationOnlyPortalMissingRelationshipManager(),
        adviserPortalManualMissingRelationshipManager(),
        adviserPortalScrapeMissingRelationshipManager(),
    )

    private fun sftpMissingAll(): Arguments = Arguments.of(
        "'SFTP' selected data pipe - missing all",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(SFTP)
            .get()
    )
    private fun apiMissingAll(): Arguments = Arguments.of(
        "'API' selected data pipe - missing all",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(API)
            .get()
    )
    private fun csvPdfAutoChasedMissingAll(): Arguments = Arguments.of(
        "'CSV/PDF auto chased' selected data pipe - missing all",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(CSV_PDF_AUTO_CHASED)
            .get()
    )
    private fun csvPdfManuallyChasedMissingAll(): Arguments = Arguments.of(
        "'CSV/PDF manually chased' selected data pipe - missing all",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(CSV_PDF_MANUALLY_CHASED)
            .get()
    )
    private fun informationOnlyPortalMissingAll(): Arguments = Arguments.of(
        "'Information only portal' selected data pipe - missing all",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(INFORMATION_ONLY_PORTAL)
            .get()
    )
    private fun adviserPortalManualMissingAll(): Arguments = Arguments.of(
        "'Adviser Portal manual' selected data pipe - missing all",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(ADVISER_PORTAL_MANUAL)
            .get()
    )
    private fun adviserPortalScrapeMissingAll(): Arguments = Arguments.of(
        "'Adviser Portal scrape' selected data pipe - missing all",
        requestBuilder()
            .withMissingAll()
            .withSelectedDataPipe(ADVISER_PORTAL_SCRAPE)
            .get()
    )
    private fun sftpMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'SFTP' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withMissingTargetedDataPipe()
            .withSelectedDataPipe(SFTP)
            .get()
    )
    private fun apiMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'API' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withMissingTargetedDataPipe()
            .withSelectedDataPipe(API)
            .get()
    )
    private fun csvPdfAutoChasedMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'CSV/PDF auto chased' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withMissingTargetedDataPipe()
            .withSelectedDataPipe(CSV_PDF_AUTO_CHASED)
            .get()
    )
    private fun csvPdfManuallyChasedMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'CSV/PDF manually chased' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withMissingTargetedDataPipe()
            .withSelectedDataPipe(CSV_PDF_MANUALLY_CHASED)
            .get()
    )
    private fun informationOnlyPortalMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'Information only portal' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withMissingTargetedDataPipe()
            .withSelectedDataPipe(INFORMATION_ONLY_PORTAL)
            .get()
    )
    private fun adviserPortalManualMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'Adviser Portal manual' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withMissingTargetedDataPipe()
            .withSelectedDataPipe(ADVISER_PORTAL_MANUAL)
            .get()
    )
    private fun adviserPortalScrapeMissingTargetedDataPipe(): Arguments = Arguments.of(
        "'Adviser Portal scrape' selected data pipe - missing targeted data pipe",
        requestBuilder()
            .withMissingTargetedDataPipe()
            .withSelectedDataPipe(ADVISER_PORTAL_SCRAPE)
            .get()
    )
    private fun sftpMissingLoaContactEmail(): Arguments = Arguments.of(
        "'SFTP' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withMissingLoaContactEmail()
            .withSelectedDataPipe(SFTP)
            .get()
    )
    private fun apiMissingLoaContactEmail(): Arguments = Arguments.of(
        "'API' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withMissingLoaContactEmail()
            .withSelectedDataPipe(API)
            .get()
    )
    private fun csvPdfAutoChasedMissingLoaContactEmail(): Arguments = Arguments.of(
        "'CSV/PDF auto chased' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withMissingLoaContactEmail()
            .withSelectedDataPipe(CSV_PDF_AUTO_CHASED)
            .get()
    )
    private fun csvPdfManuallyChasedMissingLoaContactEmail(): Arguments = Arguments.of(
        "'CSV/PDF manually chased' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withMissingLoaContactEmail()
            .withSelectedDataPipe(CSV_PDF_MANUALLY_CHASED)
            .get()
    )
    private fun informationOnlyPortalMissingLoaContactEmail(): Arguments = Arguments.of(
        "'Information only portal' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withMissingLoaContactEmail()
            .withSelectedDataPipe(INFORMATION_ONLY_PORTAL)
            .get()
    )
    private fun adviserPortalManualMissingLoaContactEmail(): Arguments = Arguments.of(
        "'Adviser Portal manual' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withMissingLoaContactEmail()
            .withSelectedDataPipe(ADVISER_PORTAL_MANUAL)
            .get()
    )
    private fun adviserPortalScrapeMissingLoaContactEmail(): Arguments = Arguments.of(
        "'Adviser Portal scrape' selected data pipe - missing LOA contact email",
        requestBuilder()
            .withMissingLoaContactEmail()
            .withSelectedDataPipe(ADVISER_PORTAL_SCRAPE)
            .get()
    )
    private fun sftpMissingRelationshipManagerAndNumber(): Arguments = Arguments.of(
        "'SFTP' selected data pipe - missing relationship manager and number",
        requestBuilder()
            .withMissingRelationshipManager()
            .withMissingRelationshipNumber()
            .withSelectedDataPipe(SFTP)
            .get()
    )
    private fun apiMissingRelationshipManagerAndNumber(): Arguments = Arguments.of(
        "'API' selected data pipe - missing relationship manager and number",
        requestBuilder()
            .withMissingRelationshipManager()
            .withMissingRelationshipNumber()
            .withSelectedDataPipe(API)
            .get()
    )
    private fun csvPdfAutoChasedMissingRelationshipManagerAndNumber(): Arguments = Arguments.of(
        "'CSV/PDF auto chased' selected data pipe - missing relationship manager and number",
        requestBuilder()
            .withMissingRelationshipManager()
            .withMissingRelationshipNumber()
            .withSelectedDataPipe(CSV_PDF_AUTO_CHASED)
            .get()
    )
    private fun csvPdfManuallyChasedMissingRelationshipManagerAndNumber(): Arguments = Arguments.of(
        "'CSV/PDF manually chased' selected data pipe - missing relationship manager and number",
        requestBuilder()
            .withMissingRelationshipManager()
            .withMissingRelationshipNumber()
            .withSelectedDataPipe(CSV_PDF_MANUALLY_CHASED)
            .get()
    )
    private fun informationOnlyPortalMissingRelationshipManagerAndNumber(): Arguments = Arguments.of(
        "'Information only portal' selected data pipe - missing relationship manager and number",
        requestBuilder()
            .withMissingRelationshipManager()
            .withMissingRelationshipNumber()
            .withSelectedDataPipe(INFORMATION_ONLY_PORTAL)
            .get()
    )
    private fun adviserPortalManualMissingRelationshipManagerAndNumber(): Arguments = Arguments.of(
        "'Adviser Portal manual' selected data pipe - missing relationship manager and number",
        requestBuilder()
            .withMissingRelationshipManager()
            .withMissingRelationshipNumber()
            .withSelectedDataPipe(ADVISER_PORTAL_MANUAL)
            .get()
    )
    private fun adviserPortalScrapeMissingRelationshipManagerAndNumber(): Arguments = Arguments.of(
        "'Adviser Portal scrape' selected data pipe - missing relationship manager and number",
        requestBuilder()
            .withMissingRelationshipManager()
            .withMissingRelationshipNumber()
            .withSelectedDataPipe(ADVISER_PORTAL_SCRAPE)
            .get()
    )
    private fun sftpMissingNone(): Arguments = Arguments.of(
        "'SFTP' selected data pipe - missing none",
        requestBuilder()
            .withSelectedDataPipe(SFTP)
            .get()
    )
    private fun apiMissingNone(): Arguments = Arguments.of(
        "'API' selected data pipe - missing none",
        requestBuilder()
            .withSelectedDataPipe(API)
            .get()
    )
    private fun csvPdfAutoChasedMissingNone(): Arguments = Arguments.of(
        "'CSV/PDF auto chased' selected data pipe - missing none",
        requestBuilder()
            .withSelectedDataPipe(CSV_PDF_AUTO_CHASED)
            .get()
    )
    private fun csvPdfManuallyChasedMissingNone(): Arguments = Arguments.of(
        "'CSV/PDF manually chased' selected data pipe - missing none",
        requestBuilder()
            .withSelectedDataPipe(CSV_PDF_MANUALLY_CHASED)
            .get()
    )
    private fun informationOnlyPortalMissingNone(): Arguments = Arguments.of(
        "'Information only portal' selected data pipe - missing none",
        requestBuilder()
            .withSelectedDataPipe(INFORMATION_ONLY_PORTAL)
            .get()
    )
    private fun adviserPortalManualMissingNone(): Arguments = Arguments.of(
        "'Adviser Portal manual' selected data pipe - missing none",
        requestBuilder()
            .withSelectedDataPipe(ADVISER_PORTAL_MANUAL)
            .get()
    )
    private fun adviserPortalScrapeMissingNone(): Arguments = Arguments.of(
        "'Adviser Portal scrape' selected data pipe - missing none",
        requestBuilder()
            .withSelectedDataPipe(ADVISER_PORTAL_SCRAPE)
            .get()
    )
    private fun sftpMissingRelationshipNumber(): Arguments = Arguments.of(
        "'SFTP' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(SFTP)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun apiMissingRelationshipNumber(): Arguments = Arguments.of(
        "'API' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(API)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun csvPdfAutoChasedMissingRelationshipNumber(): Arguments = Arguments.of(
        "'CSV/PDF auto chased' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(CSV_PDF_AUTO_CHASED)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun csvPdfManuallyChasedMissingRelationshipNumber(): Arguments = Arguments.of(
        "'CSV/PDF manually chased' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(CSV_PDF_MANUALLY_CHASED)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun informationOnlyPortalMissingRelationshipNumber(): Arguments = Arguments.of(
        "'Information only portal' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(INFORMATION_ONLY_PORTAL)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun adviserPortalManualMissingRelationshipNumber(): Arguments = Arguments.of(
        "'Adviser Portal manual' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(ADVISER_PORTAL_MANUAL)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun adviserPortalScrapeMissingRelationshipNumber(): Arguments = Arguments.of(
        "'Adviser Portal scrape' selected data pipe - missing relationship number",
        requestBuilder()
            .withSelectedDataPipe(ADVISER_PORTAL_SCRAPE)
            .withMissingRelationshipNumber()
            .get()
    )
    private fun sftpMissingRelationshipManager(): Arguments = Arguments.of(
        "'SFTP' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(SFTP)
            .withMissingRelationshipManager()
            .get()
    )
    private fun apiMissingRelationshipManager(): Arguments = Arguments.of(
        "'API' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(API)
            .withMissingRelationshipManager()
            .get()
    )
    private fun csvPdfAutoChasedMissingRelationshipManager(): Arguments = Arguments.of(
        "'CSV/PDF auto chased' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(CSV_PDF_AUTO_CHASED)
            .withMissingRelationshipManager()
            .get()
    )
    private fun csvPdfManuallyChasedMissingRelationshipManager(): Arguments = Arguments.of(
        "'CSV/PDF manually chased' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(CSV_PDF_MANUALLY_CHASED)
            .withMissingRelationshipManager()
            .get()
    )
    private fun informationOnlyPortalMissingRelationshipManager(): Arguments = Arguments.of(
        "'Information only portal' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(INFORMATION_ONLY_PORTAL)
            .withMissingRelationshipManager()
            .get()
    )
    private fun adviserPortalManualMissingRelationshipManager(): Arguments = Arguments.of(
        "'Adviser Portal manual' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(ADVISER_PORTAL_MANUAL)
            .withMissingRelationshipManager()
            .get()
    )
    private fun adviserPortalScrapeMissingRelationshipManager(): Arguments = Arguments.of(
        "'Adviser Portal scrape' selected data pipe - missing relationship manager",
        requestBuilder()
            .withSelectedDataPipe(ADVISER_PORTAL_SCRAPE)
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