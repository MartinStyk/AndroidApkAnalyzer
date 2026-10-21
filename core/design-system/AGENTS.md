# core:design-system Modules

## Purpose and Boundary

The app's design system, split so the phone and TV apps each get only their own UI toolkit:

| Module | Package | Built on | Used by |
|---|---|---|---|
| `foundation` | `core.designsystem.foundation` | Compose UI only | `phone` and `tv` |
| `phone` | `core.designsystem.phone` | Material3 | `app` and feature `impl` modules |
| `tv` | `core.designsystem.tv` | TV Material (`androidx.tv:tv-material`) | the `tv` app and TV modules |

`foundation` never depends on Material3 or TV Material. `phone` and `tv` never depend on each other.
Both expose `foundation` as an `api` dependency, so consumers never declare it directly.

Phone features must not import `androidx.compose.material3`, and TV code must not import
`androidx.tv.material3`. Wrap a new component in the matching module first. The `app` module is the
only exception, for shell-level `Scaffold` and theme plumbing.

## foundation

* `theme/` - `AppTheme`, the one entry point phone and TV code read colors and type from, plus
  `ApkAnalyzerColorPalette` with its light and dark values and the `ApkAnalyzerTypographyStyles`
  shape. `foundation` defines the shape only; each device theme provides the values through
  `LocalApkAnalyzerColors` and `LocalApkAnalyzerTypography`, because phone and TV read at different
  distances and size text differently. Reading `AppTheme` outside a device theme throws.
* `icons/` - `ApkAnalyzerIcons`; `icons/app/` registers the Coil `ImageLoader` that renders an
  `AppReference` as its package icon.
* `res/mipmap-*` - the launcher icon both apps reference as `@mipmap/ic_launcher`.

## phone

* `theme/` - `ApkAnalyzerTheme`, which provides the phone values for `AppTheme`, plus the Material3
  color schemes, the phone type scale, and shapes.
* `components/` - reusable UI primitives and composed controls.
* `animation/` - Navigation 3 transition metadata.
* `modifier/` and `lazylist/` - shared interaction, layout, and list behavior.

Search `components/` by composable name before adding or calling a component. The notable naming
exception is `InactiveSearchBar`, which lives in `SearchBarInactive.kt`.

* Feature colors and typography come from `AppTheme`; icons come from `ApkAnalyzerIcons`.
* Interactive status `Chip` usage goes through the component's click and long-click overload so the
  shared component owns semantics and its shape-clipped ripple.
* `Chip` is an interactive selector with a full touch target. `Tag` is a compact, non-interactive
  inline fact on a row. Do not use them interchangeably.

## tv

* `theme/` - `ApkAnalyzerTvTheme`, which provides the TV values for `AppTheme`. TV is dark-only by
  product decision, following Google's TV guidance; do not add a light palette or a theme setting.
  `ApkAnalyzerTvTypography` is pinned explicitly: it keeps the phone scale's weights
  and letter spacing, with title, body, and label sizes raised for reading across a room, plus a
  `monospace` style for fingerprints and hashes.
* `components/` - TV wrappers. Names mirror their phone counterparts (`Text`, `NavigationDrawer`).

* Every interactive component must be reachable and operable with a D-pad alone; touch is not
  required on TV.
* Previews use `@Preview(device = Devices.TV_1080p)` and wrap content in `ApkAnalyzerTvTheme`.

## Shared Component Rules

* Components accept theme-consistent defaults and every component file includes private previews.
* A second copy of a shared shape, control, loading state, or interaction pattern belongs in this
  group rather than in a feature.
