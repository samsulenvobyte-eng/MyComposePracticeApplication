## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-06-22 - [Compose State: Avoiding Snapshot Overhead]
**Learning:** For high-frequency particle systems where redraws are already driven by a single "frame tick" state (like `timeNanos`), using `mutableStateListOf` for the particle collection is an anti-pattern. It introduces unnecessary Snapshot system overhead on every modification.
**Action:** Use a plain `ArrayList` for the particle collection and rely on the frame tick state to trigger recomposition/redraw of the `Canvas`. Ensure all modifications happen on the Main thread.
