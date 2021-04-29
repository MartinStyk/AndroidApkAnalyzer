package sk.styk.martin.apkanalyzer.util

enum class FragmentTag(val tag: String) {
    AppList("AppList"),
    LocalStatistics("LocalStatistics"),
    LocalPermissions("LocalPermissions"),
    About("About"),
    Settings("Settings"),
    Premium("Premium"),
    AppDetailParent("AppDetailParent"),
    PermissionDetail("PermissionDetail"),
    Manifest("Manifest");

    fun isTag(stringTag: String?) = tag == stringTag

    companion object {
        fun fromString(stringTag: String?): FragmentTag? {
            return values().firstOrNull { it.isTag(stringTag) }
        }
    }
}