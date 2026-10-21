package sk.styk.martin.apkanalyzer.feature.appdetail.phone.generalinfo

sealed interface GeneralInfoAction {
    data class CopyValue(val label: String, val value: String) : GeneralInfoAction
}
