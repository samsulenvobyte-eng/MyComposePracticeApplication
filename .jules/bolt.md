## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-04-18 - [Compose Canvas Performance: Path and Iterator Allocations]
**Learning:** Allocating `Path` objects and using `forEach` (which creates an `Iterator`) inside a `Canvas` draw block or `drawBehind` modifier causes significant garbage collection overhead during animations. Additionally, creating large `listOf` or `Pair` structures every frame for coordinate data is a major bottleneck.
**Action:** Extract static coordinate data to top-level constants. Cache `Path` and `Brush` objects using `remember` and reuse them (using `.rewind()` or `.reset()`). Use manual indexed `for` loops instead of `forEach` inside draw blocks.
