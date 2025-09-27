## 1. **`Mono.fromRunnable(Runnable)`**

- Input: a `Runnable` (no return, just side-effect).
- Behavior: runs the code, emits **onComplete()**, never emits a value.

👉 Use when:

- Code is **void-returning** side-effect (like logging, setting attributes, updating a counter).
- You don’t need a result, just sequencing.

```java
Mono.fromRunnable(() -> api.setAttr("mappedData", form));
// runs side effect, completes with Mono<Void>
```

---

## 2. **`Mono.fromCallable(Callable<T>)`**

- Input: a `Callable` (returns something, may throw).
- Behavior: runs the code, emits `onNext(value)` then `onComplete()`.

👉 Use when:

- Code returns a value.
- Code _might throw checked exceptions_.

```java
Mono.fromCallable(() -> Files.readString(Path.of("config.json")));
// emits String result or propagates IOException as error
```

---

## 3. **`Mono.fromSupplier(Supplier<T>)`**

- Input: a `Supplier<T>` (returns something, no checked exceptions).
- Behavior: runs the supplier, emits `onNext(value)` then `onComplete()`.

👉 Use when:

- Code is **simple, no checked exceptions**.
- Like `Mono.just()`, but lazy (runs at subscription time).

```java
Mono.fromSupplier(() -> UUID.randomUUID().toString());
// lazy, emits a String
```

_(vs `Mono.just(UUID.randomUUID().toString())` → eager, evaluated right away when building the pipeline)_

---

## 4. **`Mono.defer(Supplier<Mono<T>>)`**

- Input: a Supplier that itself builds a `Mono`.
- Behavior: defers **creation of the Mono** until subscription.

👉 Use when:

- You want the _whole Mono pipeline_ to be built lazily.
- Especially useful if your Mono depends on something that changes at runtime.

```java
Mono.defer(() -> Mono.just(System.currentTimeMillis()));
// every subscription gets a fresh timestamp
```

_(vs `Mono.just(System.currentTimeMillis())` → timestamp fixed at pipeline creation)_

---

## 5. **`Mono.just(T)`**

- Input: a direct value.
- Behavior: emits that value immediately, eager.

👉 Use when:

- You already have the value (not expensive, not changing).

```java
Mono.just("fixed string");
// always the same "fixed string"
```

---

## 🏁 Summary cheat sheet

| Operator       | Input               | Emits           | Use case                                      |
| -------------- | ------------------- | --------------- | --------------------------------------------- |
| `fromRunnable` | `Runnable`          | `Void`          | Side effects, no return value                 |
| `fromCallable` | `Callable<T>`       | `T` (checked)   | Returns a value, may throw checked exceptions |
| `fromSupplier` | `Supplier<T>`       | `T` (unchecked) | Returns a value, no checked exceptions        |
| `defer`        | `Supplier<Mono<T>>` | `T`             | Build Mono lazily per subscription            |
| `just`         | `T`                 | `T` (eager)     | Already have the value                        |

---

### 🔹 In your case

```java
Mono.fromRunnable(() -> api.setAttr("mappedData", form))
```

is correct ✅ because:

- `setAttr` is a void method (side effect).
- You don’t need a return value.

---

😂 Joke:
`Mono.fromRunnable`: “I’ll do it but don’t expect a thank you.”
`Mono.fromCallable`: “I’ll do it and give you something back.”
`Mono.defer`: “Ask me later.”
`Mono.just`: “Already done.”
