## ⚙️ Summary Table

| Case | Column Type             | Matches       | Operator | Example SQL                           |     |     |
| ---- | ----------------------- | ------------- | -------- | ------------------------------------- | --- | --- |
| 1️⃣   | Scalar (`text`, `enum`) | Any in list   | `IN`     | `status IN ('APPLIED','HIRED')`       |     |     |
| 2️⃣   | Array (`text[]`)        | Any overlap   | `&&`     | `status && ARRAY['APPLIED','HIRED']`  |     |     |
| 2️⃣   | Array (`text[]`)        | All contained | `@>`     | `status @> ARRAY['APPLIED','HIRED']`  |     |     |
| 3️⃣   | JSONB Array             | Any overlap   | `?\|`    | `tags ?\|` `ARRAY['remote','junior']` |     |
| 3️⃣   | JSONB Array             | All contained | `?&`     | `tags ?& ARRAY['remote','junior']`    |     |     |
