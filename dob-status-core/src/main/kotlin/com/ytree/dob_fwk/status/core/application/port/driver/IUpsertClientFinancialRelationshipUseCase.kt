package com.ytree.dob_fwk.status.core.application.port.driver

import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipResponse
import com.ytree.dob_fwk.status.core.application.port.driver.model.ClientFinancialRelationshipUpsertRequest

interface IUpsertClientFinancialRelationshipUseCase {
    operator fun invoke(request: ClientFinancialRelationshipUpsertRequest): Result<ClientFinancialRelationshipResponse>
}