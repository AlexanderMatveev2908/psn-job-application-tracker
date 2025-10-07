## ⚖️ Comparison

| Type             | Method       | Takes input? | Returns | Example                      | Analogy                |
| ---------------- | ------------ | ------------ | ------- | ---------------------------- | ---------------------- |
| `Function<T, R>` | `.apply(T)`  | ✅ Yes       | `R`     | `val -> val.toUpperCase()`   | Filter / Transformer   |
| `Supplier<T>`    | `.get()`     | ❌ No        | `T`     | `() -> "default"`            | Generator / Lazy value |
| `Consumer<T>`    | `.accept(T)` | ✅ Yes       | void    | `s -> System.out.println(s)` | Side effect            |
| `Predicate<T>`   | `.test(T)`   | ✅ Yes       | boolean | `s -> s.isEmpty()`           | Condition checker      |
