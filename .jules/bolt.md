## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.
## 2026-03-08 - [Compose High-Frequency Animation: Path Allocation & State Reading]
**Learning:** Allocating  objects inside a  draw loop for animations (like particles) causes significant GC pressure. Additionally, reading animation state (like ) in the composition phase instead of the draw phase causes the entire parent composable to recompose on every frame.
**Action:** Pre-create normalized paths using  and use  to scale/rotate them in . Use lambda providers () to defer state reading to the draw or graphics phase.
## 2026-03-08 - [Compose High-Frequency Animation: Path Allocation & State Reading]
**Learning:** Allocating `Path` objects inside a `Canvas` draw loop for animations (like particles) causes significant GC pressure. Additionally, reading animation state (like `Animatable.value`) in the composition phase instead of the draw phase causes the entire parent composable to recompose on every frame.
**Action:** Pre-create normalized paths using `remember` and use `withTransform` to scale/rotate them in `Canvas`. Use lambda providers (`() -> T`) to defer state reading to the draw or graphics phase.
