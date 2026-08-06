## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-04-23 - [Compose Canvas Performance: Object Allocation in Draw Loop]
**Learning:** Allocating `Path` objects, creating `Offset`/`Size` instances, and using `forEach` (which allocates an `Iterator`) inside a `Canvas` draw block causes significant heap churn and garbage collection pressure, especially with many particles (150+).
**Action:** Pre-allocate and `remember` normalized paths outside the `Canvas`. Use indexed `for` loops (`for (i in indices)`) to avoid iterator allocations. Use `withTransform` to handle translation/rotation and reuse cached paths via scaling.
