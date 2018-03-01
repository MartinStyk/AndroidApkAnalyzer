package sk.styk.martin.apkanalyzer.model.server

import java.math.BigDecimal

/**
 * Created by Martin Styk on 27.01.2018.
 */
data class RepackagedDetectionResult(
        var appRecordId: Int,
        var status: RepackagedDetectionStatus,
        var totalRepackagedApps: Int,
        var totalDifferentRepackagedApps: Int,
        var percentageMajoritySignature: BigDecimal,
        var percentageSameSignature: BigDecimal,
        var signaturesNumberOfApps: Map<String, Int>
)