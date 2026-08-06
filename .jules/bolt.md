## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Path Optimization: reset vs rewind]
**Learning:** The `androidx.compose.ui.graphics.Path` interface in Jetpack Compose only supports `.reset()`. The `.rewind()` method (which preserves internal data structures for faster reuse) is part of the platform-specific `android.graphics.Path` and is NOT available on the multiplatform Compose `Path` interface.
**Action:** Use `Path.reset()` when reusing `Path` objects in high-frequency `Canvas` draw blocks.
