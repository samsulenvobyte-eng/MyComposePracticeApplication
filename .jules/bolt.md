## 2026-03-08 - [Compose Canvas Performance: Text Measurement]
**Learning:** Performing text measurement (`textMeasurer.measure()`) or string formatting (`String.format()`) inside a `Canvas` draw block or `drawBehind` modifier causes significant frame drops during animations. These operations are computationally expensive and run every frame (~60-120 FPS).
**Action:** Always pre-calculate display strings and pre-measure `TextLayoutResult` objects using `remember` with appropriate keys (like data and text style) before the `Canvas` block.

## 2026-03-08 - [Compose Canvas: Drawing Block Allocations]
**Learning:** Allocating objects (like `Path`, `List<Offset>`) and using functional collection operators (`forEachIndexed`, `map`) inside a `Canvas` draw block triggers frequent GC cycles during animations, causing frame stutter.
**Action:** Pre-allocate `Path` objects and primitive `FloatArray`s using `remember` outside the draw block. Use `path.reset()` to reuse paths and standard `for` loops to iterate, avoiding `Iterator` and boxed object allocations.
