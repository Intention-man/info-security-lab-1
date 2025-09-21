# Информационная безопасность. Лабораторная работа 1

## Обзор Проекта

Это учебный проект, демонстрирующий реализацию безопасного REST API с использованием современных практик защиты веб-приложений. Проект построен на Java 17 с использованием Spring Boot 3.3.7 и включает в себя систему аутентификации на основе JWT-токенов, а также комплексную защиту от распространенных веб-уязвимостей.

### Технологический стек
- **Java 17**
- **Spring Boot 3.3.7** (Web, Security, Data JPA)
- **PostgreSQL** - база данных
- **JWT (JSON Web Tokens)** - для аутентификации
- **Maven** - система сборки
- **GitHub Actions** - CI/CD pipeline с автоматическими проверками безопасности


## API

#### 1. Регистрация пользователя
```http
POST /api/auth/registration
Content-Type: application/json

{
    "username": "newuser",
    "password": "securePassword123"
}
```

Ответ при успешной регистрации:

```http
HTTP Status: 200 OK

{
"token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

Ответ при ошибке (пользователь уже существует):

```http
HTTP Status: 409 Conflict
```


#### 2. Авторизация

```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "existinguser",
    "password": "securePassword123"
}
```
Ответ при успешной авторизации:

```http
HTTP Status: 200 OK

{
"token": "eyJhbGciOiJIUzUxMiJ9..."
}
```
Ответ при ошибке (неверный логин или пароль):

```http
HTTP Status: 404 Not Found
```

#### 3. Получение списка всех пользователей


```http
GET /api/data/all-users
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

Ответ при успешном запросе:

```http
HTTP Status: 200 OK

[
    {
        "username": "user1"
    },
    {
        "username": "user2"
    }
]
```
Ответ без токена или с невалидным токеном:

```http
HTTP Status: 403 Forbidden
```


## Реализация мер защиты

### 1. Защита от SQL-инъекций (SQLi)

**Технология:** Spring Data JPA с Hibernate ORM

**Реализация:**
- Используются параметризованные запросы (Prepared Statements) через JpaRepository
- Все параметры автоматически экранируются Hibernate перед подстановкой в SQL
- Полностью исключена конкатенация строк при формировании запросов
- Пример: метод `findByUsername(String username)` генерирует безопасный запрос `SELECT * FROM user WHERE username = ?`

### 2. Защита от Cross-Site Scripting (XSS)

**Технология:** Кастомный Jackson сериализатор с HTML-экранированием

**Реализация:**
- Создан `HtmlSanitizerSerializer` для автоматического экранирования HTML-тегов
- Применяется к полям с пользовательским вводом через `@JsonSerialize`
- Использует Spring's `HtmlUtils.htmlEscape()` для преобразования опасных символов
- Пример: `<script>alert('XSS')</script>` → `&lt;script&gt;alert(&#39;XSS&#39;)&lt;/script&gt;`

### 3. Защита от Broken Authentication

**Технология:** JWT-токены + BCrypt хеширование

**Реализация:**

**Пароли:**
- Хешируются с помощью BCrypt (cost factor 10)
- Никогда не хранятся в открытом виде
- Безопасное сравнение через `BCryptPasswordEncoder.matches()`

**JWT-токены:**
- Алгоритм: HS512 (HMAC-SHA512)
- Срок действия: 24 часа
- Секретный ключ в конфигурации (не в коде)
- `JwtAuthenticationFilter` проверяет токен при каждом запросе к защищенным эндпоинтам

**Дополнительно:**
- Stateless архитектура (без серверных сессий)
- CSRF отключен в пользу JWT-валидации


### 4. Отчёты SAST/SCA

![img.png](sast_report.png)

![img_1.png](sca_report.png)
