## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Compose Canvas Performance: Brush and Transform]
**Learning:** Optimizing `Brush` allocations by moving them outside the `Canvas` draw block can cause visual regressions if the brush's coordinate system (startY, endY) was previously relative to dynamic drawing bounds.
**Action:** Use `DrawScope.withTransform` to scale a normalized (0 to 1) `Brush` to the desired drawing dimensions. When scaling, remember to adjust `CornerRadius` (divide Y by scale factor) to prevent curvature distortion. Also, ensure all local helper tools are removed before PR submission.
