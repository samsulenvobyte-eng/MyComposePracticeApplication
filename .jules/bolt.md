## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Animation: Deferring State Reads]
**Learning:** Reading animation state (like `Animatable.value`) directly in a Composable's body causes that Composable to recompose on every frame of the animation. For high-frequency animations (e.g., bar chart entrance), this creates significant CPU overhead and can lead to jank.
**Action:** Pass animating values as lambda providers (e.g., `progress: () -> Float`) and read them inside `Modifier.graphicsLayer { ... }` or a `Canvas { ... }` block to defer the read to the draw phase, skipping recomposition and layout entirely.

## 2026-03-08 - [Sandbox Tooling: Output Truncation]
**Learning:** The sandbox environment truncates tool output (like `read_file` or `cat`) at 1000 characters. For large Android files (>200 lines), this hides critical code structure and leads to groundedness errors during plan reviews.
**Action:** Use `sed -n 'start,endp'` via `run_in_bash_session` to read files in 30-40 line increments. This ensures the full file context is captured in the trace and available for accurate planning.
