package com.ytree.dob_fwk.status.core.domain

import com.ytree.dob_fwk.status.core.domain.DataPipe.Type.CLIENT_SPECIFIC
import com.ytree.dob_fwk.status.core.domain.DataPipe.Type.PROVIDER_SPECIFIC

enum class DataPipe constructor(val type: Type) {
    // provider-specific
    SFTP(PROVIDER_SPECIFIC),
    API(PROVIDER_SPECIFIC),
    CSV_PDF_AUTO_CHASED(PROVIDER_SPECIFIC),
    CSV_PDF_MANUALLY_CHASED(PROVIDER_SPECIFIC),
    INFORMATION_ONLY_PORTAL(PROVIDER_SPECIFIC),
    ADVISER_PORTAL_MANUAL(PROVIDER_SPECIFIC),
    ADVISER_PORTAL_SCRAPE(PROVIDER_SPECIFIC),
    // client-specific
    OPEN_BANKING(CLIENT_SPECIFIC),
    CLIENT_PORTAL(CLIENT_SPECIFIC),
    CSV_PDF_FROM_CLIENT(CLIENT_SPECIFIC),
    CLIENT_UPDATE_VIA_APP(CLIENT_SPECIFIC);

    enum class Type {
        PROVIDER_SPECIFIC,
        CLIENT_SPECIFIC
    }
}