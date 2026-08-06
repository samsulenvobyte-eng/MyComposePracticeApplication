## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Graphics Optimization: drawWithCache]
**Learning:** Creating complex `Path` objects inside a `Canvas` draw block re-allocates and re-populates the path on every frame, even if the layout size hasn't changed.
**Action:** Use `Modifier.drawWithCache` to cache `Path` objects and other size-dependent drawing state. This ensures they are only re-calculated when the component's size changes, significantly reducing CPU/GPU overhead during animations.
