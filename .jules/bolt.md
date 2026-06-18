## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.
## 2026-03-08 - [Compose Canvas Performance: In-place Mutation & Frame Tick]
**Learning:** Using functional patterns like `list.map { it.copy(...) }` in high-frequency animation loops (~60-120 FPS) triggers massive GC pressure and unnecessary recompositions. Reading state in the Draw phase (inside Canvas) allows skipping Recomposition and Layout phases.
**Action:** Use a mutable `class` for particles, update an `ArrayList` in-place within `withFrameNanos`, and trigger redraws by reading a `mutableLongStateOf` "frame tick" inside the `Canvas` block.
