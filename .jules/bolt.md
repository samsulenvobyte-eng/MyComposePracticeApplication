## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Compose Canvas Performance: Path Allocations]
**Learning:** Instantiating `Path` and `PathMeasure` objects inside a `Canvas` draw block during animations leads to massive object allocation and GC pressure (60+ times per second). This is especially noticeable with complex shapes or multiple particles.
**Action:** Cache base `Path` and `PathMeasure` objects using `remember`. For animated path segments, use a single `remember { Path() }` instance and call `reset()` before every `getSegment()` call. For particles with varying sizes, use a single normalized `Path` and scale it using `DrawScope.scale` or `withTransform` in the draw block.
