
# Lab 3 — CosmoCats Products API

Лабораторна робота №3 з курсу **Web-Java (Spring Boot)**  
**Тема:** REST-API для каталогу товарів з використанням **PostgreSQL**, **Liquibase**, **Docker** та **feature-toggle**.

---

## Студент

- **ПІБ:** Бурлій Андрій Євгенійович  
- **Група:** ІО-32  
- **Репозиторій:** `lab1.2-backend-CosmoCats`, гілка `lab3`

---

## Що реалізовано в цій роботі

1. **REST-API для продуктів CosmoCats**
   - `GET /api/v1/products` — отримати список продуктів.
   - `GET /api/v1/products/{id}` — отримати продукт за ID.
   - `POST /api/v1/products` — створити продукт.
   - `PUT /api/v1/products/{id}` — оновити продукт.
   - `DELETE /api/v1/products/{id}` — видалити продукт.

2. **Збереження даних у PostgreSQL**
   - Конфігурація через `application.yml`.
   - Підключення до контейнера `postgres` з Docker Compose.
   - JPA / Hibernate для роботи з ентіті.

3. **Міграції схеми через Liquibase**
   - `db/changelog/db.changelog-master.yml` — головний changelog.
   - `0001-initial-schema.yml` — створення таблиць `categories`, `products`, `orders`, `order_lines` та послідовностей.
   - `0002-make-category-id-nullable.yml` — послаблення NOT NULL для `products.category_id`.

4. **Feature-toggle `cosmoCats`**
   - Фічу контролює властивість  
     `features.cosmoCats.enabled`.
   - Якщо значення `false` → усі запити до `/api/v1/products/**` повертають **403 Forbidden** з повідомленням  
     _"Feature 'cosmoCats' is disabled"_.
   - У звичайному профілі (development) фіча **увімкнена**.

5. **Обробка помилок**
   - Валідація вхідних даних.
   - 400 Bad Request — наприклад, якщо продукт з таким ім’ям вже існує.
   - 404 Not Found — продукт не знайдено.
   - 500 Internal Server Error — неочікувана помилка.
   - Відповіді у форматі `ProblemDetail`.

6. **Тестування**
   - Unit/Integration тести для сервісу та контролера.
   - Testcontainers + PostgreSQL для інтеграційних тестів.
   - Jacoco звіт по покриттю.

---

## Стек технологій

- **Java 21**
- **Spring Boot 3.3.5**
  - spring-boot-starter-web  
  - spring-boot-starter-validation  
  - spring-boot-starter-data-jpa  
  - spring-boot-starter-aop
- **PostgreSQL + Testcontainers**
- **Liquibase**
- **Docker / Docker Compose**
- **Jacoco** (coverage)
- **WireMock** (для тестів зовнішніх викликів)
- **Gradle** (через `gradlew`)

---

## Як запустити аплікацію локально

### 1. Попередні вимоги

- Встановлено:
  - **JDK 21**
  - **Docker Desktop** (або Docker Engine + Docker Compose)
  - Git (для клонування репозиторію)

### 2. Клонувати репозиторій та перейти в гілку

```bash
git clone https://github.com/AndriiBurlii/lab1.2-backend-CosmoCats.git
cd lab1.2-backend-CosmoCats
git checkout lab3
```

### 3. Підняти базу даних PostgreSQL через Docker

У корені проєкту (де лежить `docker-compose.yml`):

```bash
docker compose up -d
```

Це створить контейнер, наприклад:

- **сервіс:** `lab3-cosmocats-db`
- **port:** `5432`
- **База:** `cosmocats`
- **User / password:** `cosmo / cosmo`

### 4. Запустити Spring Boot застосунок

```bash
./gradlew bootRun
```

Після старту аплікація буде слухати:

- `http://localhost:8080`

---

## Перевірка API через Postman

У репозиторії є готова Postman-колекція:

- `cosmocats-lab3.postman_collection.json`

### Імпорт колекції

1. Відкрити Postman.
2. Натиснути **Import** → обрати файл `cosmocats-lab3.postman_collection.json`.
3. Запустити запити з колекції **CosmoCats Lab3 – Products**.

### Основні приклади запитів

#### Створити продукт

`POST /api/v1/products`  
Body → `raw` → `JSON`:

```json
{
  "name": "Space Milk",
  "price": 12.50,
  "category": "DAIRY"
}
```

Можливі відповіді:

- `201 Created` — продукт успішно створено.
- `400 Bad Request` — продукт з таким ім’ям вже існує.

#### Отримати список продуктів

`GET /api/v1/products`

#### Отримати продукт за ID

`GET /api/v1/products/{id}`

#### Оновити продукт

`PUT /api/v1/products/{id}`

#### Видалити продукт

`DELETE /api/v1/products/{id}`

---

## Feature toggle `cosmoCats`

Фіча контролюється властивістю:

```yaml
features:
  cosmoCats:
    enabled: true
```

- Якщо `true` — API працює у звичайному режимі.
- Якщо `false` — будь-який виклик `/api/v1/products/**` повертає:

```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "Feature 'cosmoCats' is disabled",
  "path": "/api/v1/products"
}
```

У тестовому профілі (`application-test.yml`) фіча також **увімкнена**, щоб сценарії тестування проходили коректно.

---

## Міграції бази даних (Liquibase)

Головний файл:

- `src/main/resources/db/changelog/db.changelog-master.yml`

Підключені чейнджсети:

1. `0001-initial-schema.yml` — створення таблиць та послідовностей.
2. `0002-make-category-id-nullable.yml` — дозвіл `NULL` для `products.category_id`.

Міграції запускаються автоматично при старті Spring Boot застосунку.

---

## Тести та звіт по покриттю

Запуск тестів:

```bash
./gradlew test
```

Генерація Jacoco-звіту:

```bash
./gradlew jacocoTestReport
```

Після виконання звіт буде доступний за шляхом:

- `build/reports/jacoco/test/html/index.html`

---

## Як завершити роботу

Зупинити Spring Boot застосунок — `Ctrl + C` у консолі з `bootRun`.

Зупинити та видалити контейнер PostgreSQL:

```bash
docker compose down
```

---

## Висновки

У ході лабораторної роботи №3 було розроблено повноцінний REST-сервіс для управління товарами CosmoCats з використанням бази даних PostgreSQL, системи міграцій Liquibase, контейнеризації через Docker та механізму feature-toggle.  
Проєкт готовий до подальшого розширення (замовлення, інтеграції, авторизація тощо) та може слугувати основою для наступних лабораторних робіт.
