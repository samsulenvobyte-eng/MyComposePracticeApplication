## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-04-06 - [Deferring State Reads in Jetpack Compose Animations]
**Learning:** Reading animation state directly in a `@Composable` function body causes the entire component and its parent to recompose on every frame. Deferring state reads to the **Draw** or **Layout** phases using lambdas or block-based modifiers (`graphicsLayer`, `Canvas`) eliminates these unnecessary recompositions.
**Action:** Always pass high-frequency animation states as lambda providers `() -> T` and read them only where needed (e.g., inside `Canvas` or `graphicsLayer` blocks). Prefer `graphicsLayer` over conditional composition for simple visibility/scale animations.
