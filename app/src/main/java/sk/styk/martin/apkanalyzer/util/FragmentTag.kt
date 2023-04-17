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
    Manifest("Manifest"),
}

fun String.toFragmentTag(): FragmentTag? {
    return FragmentTag.values().firstOrNull { it.tag == this }
}
