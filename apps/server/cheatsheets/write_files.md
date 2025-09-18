# 📄 Writing Text to Files in Java

Java offers several APIs for writing text to files. Each has its pros/cons depending on performance, style, and level of control.

---

## 1️⃣ `Files.writeString` (Java 11+)

✅ Modern, concise, and safe.
⚠️ Not available before Java 11.

```java
Files.writeString(
    loggerFile,
    json + System.lineSeparator(),
    StandardOpenOption.CREATE,
    StandardOpenOption.TRUNCATE_EXISTING
);
```

- **Pros**: Very short, built into `java.nio.file`.
- **Cons**: Requires Java 11+, less flexible for multiple writes.

---

## 2️⃣ `Files.write`

Works with **byte arrays** or **list of strings**.

```java
// bytes
Files.write(
    loggerFile,
    (json + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
    StandardOpenOption.CREATE,
    StandardOpenOption.TRUNCATE_EXISTING
);

// list of lines
Files.write(
    loggerFile,
    List.of(json),
    StandardOpenOption.CREATE,
    StandardOpenOption.TRUNCATE_EXISTING
);
```

- **Pros**: Good for lists of lines or binary-safe writing.
- **Cons**: More verbose than `writeString`.

---

## 3️⃣ `FileWriter`

Old-school way (character stream).

```java
try (FileWriter writer = new FileWriter(loggerFile.toFile(), false)) { // false = overwrite
    writer.write(json + System.lineSeparator());
}
```

- **Pros**: Simple, classic.
- **Cons**: Uses system default charset unless specified; not ideal for large writes.

---

## 4️⃣ `BufferedWriter`

Efficient when writing **many lines**.

```java
try (BufferedWriter bw = Files.newBufferedWriter(
        loggerFile,
        StandardCharsets.UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING)) {
    bw.write(json);
    bw.newLine();
}
```

- **Pros**: Fast for repeated writes, charset control.
- **Cons**: Slightly more verbose.

---

## 5️⃣ `PrintWriter`

Adds `print`, `println`, and `printf` convenience.

```java
try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
        loggerFile,
        StandardCharsets.UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING))) {
    pw.println(json);
}
```

- **Pros**: Super convenient for formatted text.
- **Cons**: Buffered under the hood, not the fastest for huge logs.

---

## 6️⃣ `FileOutputStream`

Lowest-level, byte-based writing.

```java
try (FileOutputStream fos = new FileOutputStream(loggerFile.toFile(), false)) {
    fos.write((json + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
}
```

- **Pros**: Maximum control, works with any bytes (binary or text).
- **Cons**: Manual charset handling, less convenient for text.

---

# 🏁 Which to Use?

| API                 | Best For 🚀                              |
| ------------------- | ---------------------------------------- |
| `Files.writeString` | Quick single writes (Java 11+)           |
| `Files.write`       | Writing lists of lines or raw bytes      |
| `FileWriter`        | Simple, backward-compatible text writing |
| `BufferedWriter`    | Many lines, efficient text writing       |
| `PrintWriter`       | When you need `println` / `printf` style |
| `FileOutputStream`  | Binary or low-level control              |

---

👨‍💻 **Tip**: For logging, `BufferedWriter` or `Files.writeString` are usually the sweet spot.
