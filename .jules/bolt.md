## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-10 - [Compose Performance: GraphicsLayer & DrawWithCache]
**Learning:** Modifiers like `.scale(value)` cause the entire parent composable to recompose on every frame if `value` is an animation state. Additionally, allocating `Path` or `PathMeasure` objects inside a `Canvas` draw block creates massive GC pressure (60-120 allocations/sec).
**Action:** Use `.graphicsLayer { scaleX = value; scaleY = value }` to defer state reads to the draw phase. Use `Modifier.drawWithCache` or `remember` to hoist and reuse `Path` objects, especially when rendering many particles or complex shapes.
