## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Compose Canvas Performance: Object Allocation & Iterators]
**Learning:** Allocating data objects (like a `List` of sparks or `Path`) and using `.forEach` (which creates an `Iterator`) inside a `Canvas` draw block during high-frequency animations causes significant heap churn and GC-related jank.
**Action:** Pre-calculate all animation data objects (particles/sparks) during initialization, reuse `Path` objects via `remember` and `.reset()`, and use standard indexed `for` loops instead of `.forEach` to eliminate per-frame allocations.
