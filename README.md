# Лабораторна робота 2 — Котики під контролем (Feature Toggle + Spring AOP)

**Автор:** Бурлій Андрій, ІО‑32  
**База:** ЛР‑1.2 (гілка `lab2-feature-toggle` у цьому ж репозиторії)

---

## ✅ Що реалізовано
- Feature Toggle через властивості `feature.<name>.enabled`.
- Кастомна анотація `@FeatureFlag("cosmoCats")`.
- AOP‑аспект `FeatureToggleAspect` з `@Around`:
  - якщо фіча увімкнена → виконується метод;
  - якщо вимкнена → кидається `FeatureNotAvailableException`.
- Захищено метод `DefaultProductService#list()`.
- `GlobalExceptionHandler` → 403 Forbidden при вимкненій фічі.
- Прапорці читаються з `Environment` (працює і з YAML, і з CLI).

---

## 📂 Ключові файли
```
src/main/java/com/cosmocats/service/FeatureFlag.java
src/main/java/com/cosmocats/service/FeatureToggleAspect.java
src/main/java/com/cosmocats/service/FeatureToggleService.java
src/main/java/com/cosmocats/service/FeatureNotAvailableException.java
src/main/java/com/cosmocats/service/DefaultProductService.java
src/main/java/com/cosmocats/exception/GlobalExceptionHandler.java
src/main/resources/application.yml
```

### `application.yml`
```yaml
feature:
  cosmoCats:
    enabled: true
  kittyProducts:
    enabled: false
```

> У ресурсах має бути **лише один** конфіг: `application.yml` (без `application.yaml`).

---

## ▶️ Запуск
```bash
./gradlew bootRun
# або явно
./gradlew bootRun --args="--feature.cosmoCats.enabled=true"
./gradlew bootRun --args="--feature.cosmoCats.enabled=false"
```

## 🔌 Перевірка
```
GET /api/v1/products
```
- `enabled=true` → `200 OK`
- `enabled=false` → `403 Forbidden`

### Скріни
![200 OK]
![403 Forbidden]

---

## 🧪 Тести та CI
```bash
./gradlew clean test
```
У CI тести запускаються з прапорцем:
```bash
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification -Dfeature.cosmoCats.enabled=true
```

---

## 🛠 Деталі реалізації
- `@FeatureFlag("cosmoCats")` встановлено на `DefaultProductService#list()`.
- `FeatureToggleService` читає `feature.<name>.enabled` із середовища.
- `GlobalExceptionHandler` мапить `FeatureNotAvailableException` на 403.
- У `DefaultProductService#create(...)`:
  - перевірка дубліката, кидання `IllegalArgumentException`;
  - захист від `null` після `save(...)`.

---

## 🌿 Гілка та PR
```bash
git checkout -b lab2-feature-toggle
git add .
git commit -m "LAB-2: Feature Toggle via Spring AOP (+403 handler)"
git push -u origin lab2-feature-toggle
# PR: lab2-feature-toggle → main
```
