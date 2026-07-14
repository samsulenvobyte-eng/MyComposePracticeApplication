## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Efficient Particle Rendering with Path Templates]
**Learning:** Allocating new `Path` objects inside a `Canvas` draw loop for every particle on every frame (e.g., 100 particles * 60 FPS = 6000 allocations/sec) leads to massive GC pressure and frame stutters.
**Action:** Use `remember` to pre-allocate normalized `Path` templates (e.g., bounds from -0.5 to 0.5) and a `sharedPath`. Inside the loop, `reset()` the `sharedPath`, `addPath(template)`, and use `withTransform { scale(width, height); ... }` to draw. This reduces allocations to zero in the hot path.
