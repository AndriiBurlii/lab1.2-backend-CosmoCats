# Lab 4 — CosmoCats Security

Лабораторна робота №4 з курсу **Web-Java (Spring Boot)**
**Тема:** Захист REST-API за допомогою **Spring Security**. Реалізація **OAuth2 Resource Server** (JWT), **API Key Authentication**, **Method Level Security** та тестування безпеки за допомогою **WireMock**.

---

## Студент

- **ПІБ:** Бурлій Андрій Євгенійович
- **Група:** ІО-32
- **Репозиторій:** `lab1.2-backend-CosmoCats`, гілка `lab4`

---

## Що реалізовано в цій роботі

1. **Spring Security Configuration**
    - Налаштовано `SecurityFilterChain` для захисту ендпоінтів.
    - Підключено **OAuth2 Resource Server** для валідації JWT токенів (Bearer Auth).
    - Токени перевіряються через JWK Set URI (використовується **WireMock** для емуляції Auth Server).

2. **Гібридна аутентифікація**
    - **Варіант 1: JWT (OAuth2).** Стандартний підхід. Підтримка ролей через кастомний `JwtAuthConverter` (парсинг `realm_access.roles` з Keycloak-style токенів).
    - **Варіант 2: API Key.** Реалізовано кастомний фільтр `AuthenticationFilter`, який перевіряє заголовок `X-API-KEY` для machine-to-machine комунікації.

3. **Method Level Security (@PreAuthorize)**
    - Розмежування прав доступу на рівні методів контролера:
        - `GET /products` — доступно всім авторизованим користувачам (`USER`, `ADMIN`).
        - `POST`, `PUT`, `DELETE` — доступно тільки користувачам з роллю `ADMIN`.
    - При спробі доступу без прав повертається **403 Forbidden**.

4. **No-Auth Profile**
    - Реалізовано профіль `no-auth` для локальної розробки.
    - При запуску з цим профілем Security вимикається, і всі ендпоінти доступні анонімно.

5. **Infrastructure & Testing**
    - **WireMock:** У Docker Compose додано контейнер WireMock (порт `8081`), який віддає публічні ключі (JWKS) для валідації підпису токенів.
    - **PostgreSQL:** Порт бази даних змінено на `5433` (зовнішній), щоб уникнути конфліктів з локальним Postgres.
    - **Integration Tests:**
        - Тести з `@WithMockUser` для перевірки логіки контролера.
        - Тести з генерацією реальних підписаних JWT (RSA) та перевіркою через WireMock.

---

## Стек технологій

- **Java 21**
- **Spring Boot 3.3.5**
    - `spring-boot-starter-security`
    - `spring-boot-starter-oauth2-resource-server`
    - `spring-boot-starter-web`
    - `spring-boot-starter-data-jpa`
- **PostgreSQL** (Docker)
- **Liquibase**
- **WireMock** (Mock Auth Server)
- **Docker / Docker Compose**
- **Junit 5 / MockMvc**

---

## Як запустити аплікацію локально

### 1. Підняти інфраструктуру (Postgres + WireMock)

У корені проєкту:

```bash
docker compose up -d
```

Це запустить:
- **PostgreSQL:** порт `5433` (юзер/пароль: `cosmo/cosmo`).
- **WireMock:** порт `8081` (емулює сервер авторизації, віддає JWKS ключі).

> **Важливо:** Перед запуском переконайтеся, що порти 5433 та 8081 вільні.

### 2. Запустити Spring Boot застосунок

Стандартний запуск (з увімкненим Security):

```bash
./gradlew bootRun
```

Запуск без безпеки (Dev mode):
```bash
./gradlew bootRun --args='--spring.profiles.active=dev,no-auth'
```

Аплікація буде доступна за адресою: http://localhost:8080.

## Перевірка безпеки (Postman)

### 1. Доступ через Bearer Token (JWT)

Для тестування потрібно згенерувати JWT токен (наприклад, на [jwt.io](https://jwt.io)), підписаний приватним ключем, парним до того, що завантажений у WireMock.

**Вимоги до токена:**
- Alg: `RS256`
- Header: `kid: "cosmo-key-1"` (має співпадати з конфігом WireMock)
- Payload: має містити ролі:
  ```json
  {
    "realm_access": {
      "roles": ["ADMIN"]
    }
  }
  ```

**Запит:**
`GET /api/v1/products`
Auth Type: `Bearer Token` -> Вставити токен.  
Result: `200 OK`

### 2. Доступ через API Key

Альтернативний спосіб входу (використовується секрет з `application.yml`).

**Запит:**
`GET /api/v1/products`  
Auth Type: `No Auth`  
Headers:
- Key: `X-API-KEY`
- Value: `cosmo-secret-key-123`

Result: `200 OK`

### 3. Перевірка обмеження прав (Forbidden)

Спробуйте виконати `DELETE /api/v1/products/{id}` з токеном, який має тільки роль `USER`.
Result: `403 Forbidden`.

---

## Тести

У проєкті реалізовано декілька рівнів тестування безпеки:

1. **`ProductControllerIT`**: Використовує анотацію `@WithMockUser(roles = "ADMIN")` для імітації автентифікованого користувача без підняття реального контексту безпеки. Перевіряє бізнес-логіку під захистом.
2. **`SecurityIntegrationTest`**: Full-stack тест. Піднімає WireMock, генерує пару ключів RSA на льоту, підписує токен і робить реальний запит до API. Перевіряє, чи Spring Security коректно валідує підпис токена через JWKS endpoint.
3. **`AuthenticationTest`**: Перевіряє роботу фільтрів (API Key vs JWT) та реакцію на відсутність авторизації (401 Unauthorized).

Запуск тестів:

```bash
./gradlew test
```

## Висновки

У лабораторній роботі №4 було успішно інтегровано **Spring Security** у проєкт CosmoCats. Реалізовано сучасний підхід до захисту API через **OAuth2 Resource Server** з підтримкою JWT, а також додано механізм **API Key** для технічних інтеграцій. Використання **WireMock** та **Docker** дозволило створити ізольоване середовище для розробки та тестування процесів аутентифікації без залежності від зовнішніх провайдерів (на кшталт Keycloak/Auth0).