## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Compose Canvas Performance: Object Allocations & Iterators]
**Learning:** Allocating objects (like `Path` or `List`) and using `forEach` loops on standard Collections inside a `Canvas` draw block creates massive GC pressure and iterator overhead (~60-120 times per second). `Color.copy(alpha = ...)` also creates new `Color` instances.
**Action:** Pre-calculate data structures outside the draw loop, reuse a single `Path` instance with `.reset()`, use indexed `for` loops to avoid iterator allocation, and use the native `alpha` parameter in drawing functions.
