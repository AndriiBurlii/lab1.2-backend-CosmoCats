### 🚀 Lab 1.2 — *CosmoCats Intergalactic Marketplace*

#### 🎯 Мета роботи
- Протестувати функціонал інтергалактичного ринку та підготувати код до рівня **production-ready**.  
- Реалізувати **юніт-тестування сервісного рівня** та **інтеграційні тести контролерів**.  
- Досягти **мінімального coverage ≥50% (фактично ~70%)**.  
- Налаштувати **GitHub Actions CI** для автоматичної перевірки тестів і покриття.  
- Використати **WireMock** для імітації зовнішніх HTTP-сервісів.

---

#### 🧩 Основні компоненти
| Рівень | Опис |
|:--|:--|
| **API** | `/api/v1/products` — REST CRUD із валідацією даних |
| **Service Layer** | `ProductService` + `DefaultProductService` (mocked у тестах) |
| **Error Handling** | глобальний `@ControllerAdvice` через `ProblemDetails` |
| **Validation Tests** | позитивні та негативні кейси (MockMvc) |
| **External Stub** | WireMock (`ExternalRateClientWireMockTest`) |

---

#### 🧪 Результати тестування
✅ **12 тестів** успішно пройдено  
📊 **Coverage (Jacoco): ~70%**  
🟢 **CI Pipeline:** успішний (`build passed`)

---

#### 🖼️ Coverage Report
![Jacoco Coverage Report](https://raw.githubusercontent.com/AndriiBurlii/lab1.2-backend-CosmoCats/lab1.2/coverage.png)


---

#### 🧰 Використані технології
Spring Boot • Gradle • JUnit 5 • Mockito • WireMock • Jacoco • GitHub Actions

---

#### 👨‍💻 Автор
**Андрій Бурлій (ІО-32)**  

