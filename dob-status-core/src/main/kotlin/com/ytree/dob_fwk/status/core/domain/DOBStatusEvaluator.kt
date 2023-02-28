package com.ytree.dob_fwk.status.core.domain

internal fun interface DOBStatusEvaluator: (ClientFinancialRelationship) -> Result<DOBStatus>