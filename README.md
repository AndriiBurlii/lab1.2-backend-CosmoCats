
# Lab 1.2 — CosmoCats (Spring Boot + Gradle)

- API versioning: `/api/v1/...`
- Service layer has an interface (`ProductService`) and an implementation (`DefaultProductService`).
- Simplified error handling (no `ProblemDetails`), see `GlobalExceptionHandler`.
- Jacoco coverage gate: **>= 50%**.

## Run locally

```bash
./gradlew bootRun
```

## Tests + coverage

```bash
./gradlew clean test jacocoTestReport jacocoTestCoverageVerification
```

HTML reports:
- Unit tests: `build/reports/tests/test/index.html`
- Coverage: `build/reports/jacoco/test/html/index.html`

## GitHub Actions
- See `.github/workflows/ci.yml`
- Ensure you commit the Gradle wrapper to run CI:
```bash
./gradlew wrapper
git add gradlew gradlew.bat gradle/wrapper/*
```

Coverage: ~70% (Jacoco gate 50% passed) ?
