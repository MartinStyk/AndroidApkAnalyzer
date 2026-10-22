# core:app-permissions Module

## Purpose and Boundary

Aggregates permissions requested across all installed apps into the deduplicated, sorted,
human-readable device-wide list consumed by permission browsing. The package is
`sk.styk.martin.apkanalyzer.core.apppermissions`.

This differs from `core:apps/permissions`, which models permissions declared or used by one analyzed
app.

## Per-App Permission Detail

`detail/` holds what both app-detail UIs need to show one app's permissions: `PermissionItemMapper`
(requested and defined items, duplicates merged, sorted by label), `PermissionDescriptionProvider`
(curated descriptions, then the platform description, then the declaring package), `GrantState`,
`PermissionScope`, and the labels and explanations for protection levels, extra rules, and grant
states. Grant explanations depend on both protection level and grant state; APK files have no grant
state.

## Permission Semantics

The repository derives its data from the installed-app flow and shares the result lazily with one
replayed value. All collectors must observe one cached computation, refreshed when installed-app data
changes.

Permission label resolution order is:

1. Curated localized labels for well-known Android permissions.
2. The declaring package's platform-provided label.
3. A humanized simple name derived from the raw permission identifier.

Vendor and removed permissions can fail platform lookup; preserve the readable fallback rather than
dropping those permissions. Keep raw identifiers available to callers even when a friendly label
exists.
