## ⚙️ WebFlux Middleware Execution Order

| 🧩 **Order**  | ⚔️ **Middleware**        | 🧠**Responsibility**                                                     |
| :-----------: | :----------------------- | :----------------------------------------------------------------------- |
|     **0**     | 🥷 **Ninja**             | Transforms the **ServerWebExchange** into custom **Api** instance class. |
|    **10**     | 🌍 **CorsMdw**           | Validates the incoming request’s **Origin** header.                      |
|    **20**     | 📦 **FormDataParser**    | Parses **multipart/form-data** requests if present.                      |
|    **30**     | 🔍 **QueryParserMdw**    | Parses **query parameters** if present.                                  |
|    **50**     | 🔐 **JwtMdw**            | Validates **JWT token** for protected route groups and subpaths.         |
| **Unordered** | 💤 **Other middlewares** | Default middleware, run **last**.                                        |
