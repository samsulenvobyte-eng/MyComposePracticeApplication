## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-10 - [Compose Canvas Performance: Primitive Array Allocation]
**Learning:** While using `FloatArray` instead of `List<Offset>` eliminates object boxing, allocating the array itself inside a `Canvas` draw block still causes per-frame heap allocations.
**Action:** `remember` the `FloatArray` instance outside the `Canvas` block (keyed by data size) and populate it within the draw block to achieve zero-allocation frame rendering for coordinate tracking.
