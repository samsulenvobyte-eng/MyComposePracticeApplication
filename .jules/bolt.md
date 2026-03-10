## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Compose Canvas Performance: Path Allocation]
**Learning:** Creating `Path` or `PathMeasure` objects inside a `Canvas` draw block triggers frequent Garbage Collection (GC) cycles during animations (60-120 FPS), leading to dropped frames (jank). Even seemingly small allocations add up when multiplied by many particles in an effect like confetti.
**Action:** Hoist `Path` and `PathMeasure` creation into a `remember` block outside the `Canvas`. For dynamic shapes, use normalized paths (bounds -0.5 to 0.5) and use `scale` transformations within the `Canvas` to draw them.
