## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Canvas Performance: Object Allocation]
**Learning:** Allocating objects like `Path` or `List` inside a `Canvas` draw block triggers frequent Garbage Collection (GC), leading to frame drops (jank) in high-frequency animations. Reusing `Path` objects and using the native `alpha` parameter in drawing functions significantly reduces heap pressure.
**Action:** Pre-calculate data (like particle lists) and cache drawing objects (like `Path`) using `remember` outside the `Canvas` block. Use `.rewind()` on cached `Path` objects to reuse them. Prefer the `alpha` parameter in `drawCircle`, `drawPath`, etc., over `Color.copy()`.
