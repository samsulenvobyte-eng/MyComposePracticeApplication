## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Canvas Performance: Allocation Overhead]
**Learning:** Allocating objects like `Path`, `Stroke`, or `Brush`, and using collection transformations like `map` or `forEachIndexed` inside high-frequency `Canvas` draw blocks (60-120 FPS) leads to excessive Garbage Collection (GC) pressure and frame skips (jank). Idiomatic Kotlin features like `List<Offset>` also cause boxing overhead.
**Action:** Cache drawing objects with `remember`, reuse `Path` instances via `.rewind()`, use primitive arrays (`FloatArray`) for coordinates, and prefer standard `for` loops to eliminate iterator and object allocations in the draw loop.
