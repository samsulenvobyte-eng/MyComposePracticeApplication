## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-05-09 - [Compose Optimization: Lambda Providers & Deferred State Reads]
**Learning:** Passing frequently changing animation state (like `Float` progress) as raw values causes the entire composable tree to recompose on every frame.
**Action:** Pass state as lambda providers (`() -> T`) and read them inside the `Canvas` draw block or `graphicsLayer` to skip recomposition. For `AnimatedContent`, wrap lambda providers with `derivedStateOf` inside `remember` to trigger transitions only when the actual result (e.g., an `Int` count) changes.
