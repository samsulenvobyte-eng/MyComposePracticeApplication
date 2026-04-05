## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Jetpack Compose: Deferred State Reading & Collection Stability]
**Learning:** Reading animation state (`mainProgress.value`) at the top-level of a Composable causes the entire function to recompose every frame. Additionally, standard collections like `List<Float>` are treated as unstable by the Compose compiler, which can lead to unnecessary recompositions even if the list content hasn't changed.
**Action:** Pass animation states as lambda providers (`() -> T`) to child components to defer state reading to the draw phase. To handle collection stability, wrap lists in `@Immutable` data classes or use Kotlinx Immutable Collections if available.
