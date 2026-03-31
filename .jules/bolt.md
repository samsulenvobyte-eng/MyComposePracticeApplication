## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Compose Canvas: Avoiding Color and Brush Allocations]
**Learning:** Using `color.copy(alpha = ...)` or creating `Brush` instances inside a `Canvas` draw loop creates a new object for every particle on every frame (e.g., 150 particles * 60 FPS = 9,000 objects/sec). Jetpack Compose's `drawRect`, `drawCircle`, and `drawPath` functions provide an `alpha` parameter that applies transparency during the draw phase without allocating new `Color` objects.
**Action:** Pre-calculate `Brush` objects and use the native `alpha` parameter in `DrawScope` functions instead of modifying colors in-place.
