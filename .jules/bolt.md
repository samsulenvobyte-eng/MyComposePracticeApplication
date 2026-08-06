## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-09 - [Compose Canvas Performance: Path & Brush Caching]
**Learning:** Re-creating complex Path objects (e.g., country outlines with many points) and Brush/Gradient objects inside a Canvas draw block causes unnecessary allocations every frame. This pressure on the garbage collector can lead to stuttering during animations.
**Action:** Use Modifier.drawWithCache to pre-calculate and cache Path, Brush, and other drawing objects. This ensures they are only recreated when the size changes or relevant state variables change, keeping the draw phase allocation-free.
