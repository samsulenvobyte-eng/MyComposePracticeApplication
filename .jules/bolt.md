## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Canvas Performance: Path and Object Reuse]
**Learning:** Allocating `Path`, `Stroke`, `Brush`, or boxed collections (like `List<Offset>`) inside a `Canvas` draw block or `drawBehind` modifier triggers frequent Garbage Collection (GC) during high-frequency animations, leading to jank.
**Action:** Use `remember` to cache these objects. For `Path`, use `.rewind()` inside the draw block to reset and reuse the same instance. For coordinates, use primitive arrays (e.g., `FloatArray`) instead of lists of objects to avoid boxing.
