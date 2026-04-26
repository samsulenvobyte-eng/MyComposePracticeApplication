## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-10 - [Compose Canvas Performance: Object Allocation in Draw Loops]
**Learning:** Allocating objects like `Path`, `Brush`, or `Offset` (via `listOf()` or `mapIndexed`) inside a `Canvas` draw block or `drawBehind` modifier triggers frequent Garbage Collection (GC) cycles, leading to visible stutter (jank) during animations. Even `Color.copy()` creates a new object instance every frame.
**Action:** Pre-allocate and `remember` `Path` objects (using `.rewind()` to reuse), cache `Brush` instances, use primitive arrays (`FloatArray`) for coordinates to avoid boxing, and prefer the `alpha` parameter in draw functions over `Color.copy()`.
