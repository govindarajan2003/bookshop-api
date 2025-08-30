# BookShop — Spring Boot REST API (PostgreSQL + Docker + H2 tests)

[![Java](https://img.shields.io/badge/Java-21-red)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![Build](https://img.shields.io/badge/Build-Maven-blue)](https://maven.apache.org/)

A clean, production-style REST API for managing **Books** and **Authors**.  
It uses PostgreSQL in development (via Docker Compose) and H2 for tests, with a simple mapping layer and pagination for books.

> **Frontend UI lives in a separate repository.**  
> 👉 **BookShop UI:** https://github.com/govindarajan2003/bookshop-ui
> After this API is running, follow that repo’s README to set up the React app.

---

## ✨ Features

* **Authors CRUD**: `POST /authors`, `GET /authors`, `GET /authors/{id}`, `PUT /authors/{id}`, `PATCH /authors/{id}`, `DELETE /authors/{id}`
* **Books CRUD** with **upsert by ISBN** and **pagination**:
  * `PUT /books/{isbn}` (create or update)
  * `GET /books?page=&size=`
  * `GET /books/{isbn}`
  * `PATCH /books/{isbn}`
  * `DELETE /books/{isbn}`
* **DTO ↔ Entity mapping** via ModelMapper
* **CORS enabled for `http://localhost:*`** (handy for local frontends)
* **Profiles**:
  * `dev` → PostgreSQL, `ddl-auto=update`
  * `test` → H2 (in-memory), `ddl-auto=create-drop`
  * default → PostgreSQL, `ddl-auto=validate` (safe for prod)
* **Docker Compose** for a one-command Postgres

---

## 🗂 Project layout (key parts)

```
.
├─ docker-compose.yml              # Postgres 17 (port 5544)
├─ .env.example                    # Sample environment variables
├─ src
│  ├─ main
│  │  ├─ java/com/govind/bookshop/…  # Controllers, services, mappers, entities
│  │  └─ resources
│  │     ├─ application.properties     # env-driven defaults, ddl=validate
│  │     └─ application-dev.properties # ddl=update
│  └─ test
│     ├─ java/com/govind/bookshop/…    # Integration tests
│     └─ resources/application-test.properties
└─ pom.xml
```

---

## 🚀 Quick start

### 0) Requirements

* JDK **21**
* Maven **3.9+**
* Docker & Docker Compose

### 1) Start PostgreSQL

```bash
# from project root
cp .env.example .env   # optional (adjust if you want)
docker compose up -d
# Postgres is now on localhost:5544 with DB 'bookshop'
```

### 2) Run the API (dev profile)

> The `dev` profile auto-creates/updates tables (`ddl-auto=update`).

**macOS/Linux:**
```bash
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

**Windows PowerShell:**
```powershell
$env:SPRING_PROFILES_ACTIVE = "dev"
mvn spring-boot:run
```

The API will start on **http://localhost:8080**.

### 3) (Optional) Build a runnable JAR

```bash
mvn -DskipTests package
java -jar target/bookShop-0.0.1-SNAPSHOT.jar
```

### 4) Frontend (separate repo)

Set up the React UI here: **https://github.com/govindarajan2003/bookshop-ui**  
It points to `http://localhost:8080` by default (or set the API base URL in the UI’s Settings).

---

## 🔌 Configuration (env vars)

| Variable                     | Default                                     | Used by             |
| ---------------------------- | ------------------------------------------- | ------------------- |
| `SPRING_DATASOURCE_URL`      | `jdbc:postgresql://localhost:5544/bookshop` | DB URL              |
| `SPRING_DATASOURCE_USERNAME` | `postgres`                                  | DB user             |
| `SPRING_DATASOURCE_PASSWORD` | `password`                                  | DB password         |
| `SPRING_PROFILES_ACTIVE`     | *(none)*                                    | `dev`/`test`/`prod` |
| `TZ`                         | `Asia/Kolkata`                              | JVM timezone        |

> In **dev**, schema is created/updated.  
> In **default**, schema is **validated** (no auto changes).  
> In **test**, H2 is used (in-memory) with create-drop.

---

## 📚 Domain model

* **Author**: `id`, `name`, `age`
* **Book**: `isbn` (PK), `title`, `author` (ManyToOne)

DTOs mirror the entity fields. When linking a book to an existing author, you can send just the author `id`.

---

## 🧪 Running tests

```bash
mvn -Dspring.profiles.active=test test
```

---

## 🧠 REST API Reference

### Authors

**Create**
```
POST /authors
Content-Type: application/json

{
  "name": "Neil Gaiman",
  "age": 45
}
```
**201 Created**
```json
{ "id": 1, "name": "Neil Gaiman", "age": 45 }
```

**List**
```
GET /authors
```
**200 OK**
```json
[
  {"id":1,"name":"Neil Gaiman","age":45},
  {"id":2,"name":"Terry Pratchett","age":66}
]
```

**Get by ID**
```
GET /authors/1
```
**200 OK / 404**

**Update (replace)**
```
PUT /authors/1
Content-Type: application/json

{ "name":"Neil Gaiman", "age":46 }
```
**200 OK**

**Patch (partial)**
```
PATCH /authors/1
Content-Type: application/json

{ "age": 47 }
```
**200 OK**

**Delete**
```
DELETE /authors/1
```
**204 No Content**

---

### Books

> **Upsert by ISBN:** `PUT /books/{isbn}` creates the book if it doesn’t exist, or updates it if it does.

**Create/Update**
```
PUT /books/978-0060558123
Content-Type: application/json

{
  "isbn": "978-0060558123",
  "title": "American Gods",
  "author": { "id": 1 }
}
```
**201 Created** (new) or **200 OK** (updated)

**List (paginated)**
```
GET /books?page=0&size=10
```
**200 OK**
```json
{
  "content": [
    {
      "isbn": "978-0060558123",
      "title": "American Gods",
      "author": { "id":1, "name":"Neil Gaiman", "age":47 }
    }
  ],
  "number": 0,
  "size": 10,
  "totalPages": 1,
  "totalElements": 1
}
```

**Get by ISBN**
```
GET /books/978-0060558123
```
**200 OK / 404**

**Patch (partial)**
```
PATCH /books/978-0060558123
Content-Type: application/json

{ "title": "American Gods (Author's Preferred Text)" }
```
**200 OK**

> To change the author via patch:
> ```json
> { "author": { "id": 2 } }
> ```

**Delete**
```
DELETE /books/978-0060558123
```
**204 No Content**

---

## 🌐 CORS

`CorsConfig` allows any `http://localhost:*` origin with standard methods.  
For production, restrict this to your real UI origin(s).

---

## 🐳 Docker Compose details

* Image: `postgres:17-alpine`
* Port: `5544 -> 5432`
* DB: `bookshop`
* User: `postgres` / `password`
* Volume: `pgdata` (persists data)

Common operations:
```bash
docker compose up -d
docker compose logs -f
docker compose down
docker compose down -v   # stop + delete data
```

---

## 🛠 Troubleshooting

* **`FATAL: database "bookshop" does not exist`**  
  Start Compose (creates DB): `docker compose up -d`.  
  If you changed the DB name, update `SPRING_DATASOURCE_URL`.

* **`Schema-validation: missing table [authors]`**  
  Run with the `dev` profile so Hibernate creates/updates schema:
  ```bash
  SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
  ```
  (Default profile uses `ddl-auto=validate` on purpose.)

* **CORS issues**  
  Ensure your UI runs on `http://localhost:<port>`; otherwise add that origin in `CorsConfig`.

---

## 🧾 License

MIT — do what you like, attribution appreciated.

---

## 🗺️ Roadmap

* Flyway/Liquibase migrations instead of `ddl-auto`
* OpenAPI/Swagger UI
* CI pipeline (GitHub Actions)
* Containerized Spring Boot app image

---

### ❤️ Credits

Built to showcase Spring Boot backend craftsmanship with clean layers (controller → service → repository → mapping), strong tests, and a friendly local dev experience.

