## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Canvas Performance: Allocation & Iteration]
**Learning:** Allocating `Path` objects, `Offset` collections, or using iterator-based loops (`forEachIndexed`, `mapIndexed`) inside a `Canvas` draw block creates heavy GC pressure during high-frequency animations (60-120 FPS). Boxing value classes like `Offset` into a `List` also causes heap allocations.
**Action:** Pre-allocate and `remember` `Path` objects (reuse with `.reset()`) and use primitive `FloatArray`s for coordinates. Replace higher-order collection functions with manual indexed `for` loops.
