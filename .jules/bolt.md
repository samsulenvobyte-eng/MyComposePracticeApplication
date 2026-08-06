## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-30 - [Compose Drawing Optimization: Avoiding Allocations in Canvas]
**Learning:** Frequent object allocations (like `Path()`, `List<T>`) or `Color.copy()` inside a `Canvas` draw block cause significant GC pressure and frame drops. `Random` calls, even with fixed seeds, also add unnecessary overhead when called 60+ times per second.
**Action:** Pre-generate particle/animation data during initialization. Use `Modifier.drawWithCache` to pre-calculate size-dependent drawing state. Hoist and reuse heavy objects like `Path` with `remember` and `reset()`. Use native `alpha` parameters instead of `Color.copy()`.
