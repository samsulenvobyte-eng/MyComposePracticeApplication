## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Performance: Deferring State Reads]
**Learning:** Reading animation state (e.g., `Animatable.value`) during the composition phase of a screen or parent component triggers a full recomposition on every frame (~60-120 FPS). This is a major bottleneck for complex layouts.
**Action:** Use lambda-based providers for high-frequency states and defer their reads to the layout or draw phases using `Modifier.graphicsLayer { ... }`, lambda-based `Modifier.offset { ... }`, or by passing the lambda to child components that only read it inside `Canvas` or `AnimatedContent`.
