# ⚡ Reactor Core Cheat Sheet: `Mono` & `Flux`

`Mono<T>` and `Flux<T>` are the two **reactive types** from Project Reactor (used in Spring WebFlux).

- **Mono<T>** → a publisher that emits **0 or 1 item**.
- **Flux<T>** → a publisher that emits **0..N items** (possibly infinite).

Both implement the **Reactive Streams Publisher** contract.

---

## 📦 Creation Methods

### Mono

- `Mono.just(value)` → Create with a single value.
- `Mono.empty()` → Create with no value.
- `Mono.error(Throwable)` → Create that immediately fails.
- `Mono.fromCallable(() -> ...)` → Wrap a blocking call.
- `Mono.fromSupplier(() -> ...)` → Wrap a supplier.
- `Mono.fromFuture(future)` → Wrap a `CompletableFuture`.
- `Mono.defer(() -> Mono.just(...))` → Lazy creation.
- `Mono.create(sink -> ...)` → Low-level callback style.

### Flux

- `Flux.just(v1, v2, v3...)` → Emit given values.
- `Flux.empty()` → Complete with no items.
- `Flux.error(Throwable)` → Fail immediately.
- `Flux.fromIterable(iterable)` → Emit elements from collection.
- `Flux.fromStream(stream)` → Emit elements from Java Stream.
- `Flux.range(start, count)` → Emit range of ints.
- `Flux.interval(Duration)` → Emit `Long` ticks.
- `Flux.defer(() -> ...)` → Lazy creation.
- `Flux.generate(...)` → Synchronous state generator.
- `Flux.create(...)` → Asynchronous push-style producer.

---

## 🔄 Transformations

Available for both `Mono` & `Flux` (but behavior differs):

- `.map(fn)` → Transform each value.
- `.flatMap(fn)` → Transform into another publisher (async).
- `.flatMapMany(fn)` (Mono only) → Convert a `Mono` into a `Flux`.
- `.concatMap(fn)` → Like `flatMap`, but preserves order.
- `.switchMap(fn)` → Cancel previous inner publisher when new one arrives.
- `.filter(predicate)` → Only keep items that match.
- `.distinct()` → Deduplicate values.
- `.take(n)` → Take only first n items.
- `.skip(n)` → Skip first n items.
- `.buffer(n)` → Collect items into `List` batches.
- `.window(n)` → Split stream into Flux\<Flux<T>> “windows”.
- `.collectList()` (Flux only) → Gather all into a `List<T>` inside a `Mono`.
- `.collectMap(keyMapper, valueMapper)` (Flux only).

---

## ⏱️ Time-based

- `.delayElement(Duration)` → Delay each element.
- `.delaySubscription(Duration)` → Delay subscription start.
- `.timeout(Duration)` → Fail if no item within given time.
- `.interval(Duration)` (Flux factory) → Generate periodic ticks.
- `.elapsed()` → Pair each value with the elapsed time since previous.
- `.timestamp()` → Pair each value with `System.currentTimeMillis()`.

---

## ♻️ Error Handling

- `.onErrorReturn(value)` → Fallback to default value.
- `.onErrorResume(err -> Publisher)` → Switch to alternate sequence.
- `.onErrorContinue((err, obj) -> ...)` → Skip bad element but continue.
- `.doOnError(err -> ...)` → Side-effect on error.
- `.retry(n)` → Retry `n` times.
- `.retryWhen(Retry)` → Advanced retry with backoff.

---

## 👀 Side-Effects

- `.doOnNext(v -> ...)` → Side-effect when emitting value.
- `.doOnError(err -> ...)` → On error.
- `.doOnComplete(() -> ...)` → On completion.
- `.doOnSubscribe(sub -> ...)` → On subscription.
- `.doOnCancel(() -> ...)` → When cancelled.
- `.doFinally(signal -> ...)` → Always called at the end (success/error/cancel).

---

## 🔀 Combining Publishers

- `.then()` → Ignore values, wait for completion.
- `.thenReturn(value)` → Replace with constant after completion.
- `.zipWith(other)` → Combine with another publisher.
- `Flux.merge(p1, p2...)` → Interleave multiple publishers.
- `Flux.concat(p1, p2...)` → Concatenate sequentially.
- `Flux.combineLatest(...)` → Emit when any source emits, using latest values.
- `Mono.zip(m1, m2...)` → Combine multiple Monos into tuple.

---

## ⚡ Controlling Execution

- `.subscribeOn(Scheduler)` → Define which thread runs upstream work.
- `.publishOn(Scheduler)` → Define which thread runs downstream work.
- `.block()` → (⚠️ don’t use in WebFlux) block until completion.
- `.subscribe(...)` → Trigger execution.

Schedulers available:

- `Schedulers.boundedElastic()` → For blocking I/O.
- `Schedulers.parallel()` → CPU-bound parallelism.
- `Schedulers.single()` → Single-threaded.
- `Schedulers.immediate()` → Current thread.

---

## 🛑 Terminal Operators

- `.subscribe()` → Start execution.
- `.block()` / `.blockOptional()` → Wait (⚠️ not recommended in reactive servers).
- `.toFuture()` → Convert to `CompletableFuture`.

---

## 🔬 Key Differences Between `Mono` & `Flux`

| Feature          | Mono                        | Flux                                |
| ---------------- | --------------------------- | ----------------------------------- |
| Cardinality      | 0..1 values                 | 0..N values                         |
| Collectors       | N/A                         | `collectList()`, `collectMap()`, …  |
| FlatMap variants | `flatMap`, `flatMapMany`    | `flatMap`, `concatMap`, …           |
| Typical usage    | Single DB row, HTTP request | List of rows, message stream, ticks |

---

⚡ TL;DR:

- **Mono** = async Optional.
- **Flux** = async Stream.
- Both share 80% of operators.
- Use `Schedulers.boundedElastic()` for blocking stuff (e.g., file I/O, JDBC).
