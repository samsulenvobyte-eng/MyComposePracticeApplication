## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-06-03 - [Compose: remember and @Composable property getters]
**Learning:** Calling @Composable property getters (like `MaterialTheme.colorScheme.primary`) directly inside a `remember` lambda causes a compilation error because the lambda is not a @Composable context.
**Action:** Always read the theme property in the outer @Composable scope and pass it as a key to `remember` or store it in a local variable used inside the lambda.
