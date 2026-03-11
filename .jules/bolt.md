## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Canvas: Path Allocation and Scaling]
**Learning:** Allocating `Path` objects inside a `Canvas` draw block (e.g., via `Path().apply { ... }`) causes excessive GC pressure during animations. Additionally, when scaling a normalized path (0.0 to 1.0) using `scale()`, the default pivot is the canvas center, which breaks alignment if the coordinate system has been translated.
**Action:** Cache `Path` objects using `remember` or `Modifier.drawWithCache`. When scaling normalized paths, always specify `pivot = Offset.Zero` to ensure correct positioning.
