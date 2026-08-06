## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Hot-Path Allocations in Canvas]
**Learning:** Animations involving many particles (e.g., `FireworksScreen`) can trigger thousands of object allocations per second if data objects (like spark/particle lists) or `Path` objects are created inside the `Canvas` draw block. This leads to frequent GC pauses and stutter.
**Action:** Pre-generate particle lists during initialization or `LaunchedEffect`. Reuse `Path` objects via `remember` and `Path.reset()`. Use the `alpha` parameter in drawing functions instead of `Color.copy(alpha)`.
