## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Compose Canvas Performance: Allocation & State Reads]
**Learning:** High-frequency `Canvas` draw blocks suffer from "death by a thousand cuts" via small per-frame allocations: `List<Offset>` (boxed primitives), `Path()` objects, and `Iterator` objects from `forEachIndexed`. Additionally, repetitive snapshot state reads (e.g., `progress.value`) inside loops add overhead.
**Action:**
1. Use `FloatArray` instead of `List<Offset>` for coordinates.
2. Cache `Path` objects using `remember` and call `.rewind()` to reuse them.
3. Use standard `for (i in 0 until size)` loops instead of functional operators.
4. Cache `State` values into local variables at the start of the `Canvas` block.
