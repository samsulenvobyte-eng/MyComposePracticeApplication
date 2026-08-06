## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Compose Canvas Performance: Object Allocations in Loops]
**Learning:** Allocating objects like `Path`, `List<Offset>` (which boxes `Offset`), or `Iterator` (via `forEach`) inside a `Canvas` draw block on every frame creates significant memory pressure and GC churn during animations.
**Action:** Cache `Path` objects using `remember` and `reset()` them. Use helper lambdas or primitive arrays to avoid `List` allocations for coordinates. Prefer indexed `for` loops over `forEach` to avoid iterator allocations in high-frequency draw paths.
