package sk.styk.martin.apkanalyzer.model.server

import com.google.gson.annotations.SerializedName

/**
 * Created by Martin Styk on 27.01.2018.
 */
enum class RepackagedDetectionStatus {

    @SerializedName("insufficient_data")
    INSUFFICIENT_DATA,

    @SerializedName("ok")
    OK,

    @SerializedName("nok")
    NOK
}