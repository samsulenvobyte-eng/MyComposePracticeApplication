## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-05-10 - [Compose Canvas Performance: Object Allocations]
**Learning:** Allocating objects like `Path`, `List`, or using `Color.copy` and `Random` inside a `Canvas` draw block or high-frequency animation loop causes significant garbage collection pressure and frame drops.
**Action:** Pre-calculate data structures (like particle lists) during initialization, use `Modifier.drawWithCache` for static/semi-static content, and reuse objects like `Path` using `.rewind()` or `.reset()` combined with `remember`. Use the `alpha` parameter in drawing functions instead of `Color.copy(alpha = ...)`.
