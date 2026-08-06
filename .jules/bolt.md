## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-06-25 - [Compose Canvas Performance: drawWithCache and Scoping]
**Learning:** Using `Modifier.drawWithCache` is highly effective for caching expensive objects like `Path` and `Brush` during animations. However, ensure that all variables calculated in the `drawWithCache` block are correctly accessed within the nested `onDrawBehind` or `onDrawWithContent` blocks. Also, using `Spacer` with `drawWithCache` is a cleaner alternative to `Canvas` when all drawing logic is encapsulated in the modifier.
**Action:** Use `drawWithCache` to hoist allocations and expensive `toPx()` calls out of the draw phase. Always verify that animation state reads happen inside the `onDraw` lambda to ensure redraws without recomposition.
