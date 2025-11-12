# Практическое задание — Spring Security OAuth2 (GitHub)

Демонстрационное приложение на Spring Boot, 
показывающее интеграцию **Spring Security** с **OAuth 2.0** (провайдер — GitHub), назначение ролей пользователям, защиту эндпоинтов, обработку ошибок и отзыв токенов при выходе.

## Стек технологий

- Java 17+  
- Spring Boot (Web, Security, OAuth2 Client, Data JPA)  
- GitHub OAuth2 (Authorization Code flow)  
- H2 in-memory database  
- Thymeleaf (для простых HTML-страниц)  
- SLF4J / Logback (логирование)

---

## Настройка и запуск

### 1. Регистрация OAuth App на GitHub

1. Зайти на GitHub → **Settings → Developer settings → OAuth Apps → New OAuth App**.
2. Указать:
   - **Homepage URL**:  
     `http://localhost:8080`
   - **Authorization callback URL**:  
     `http://localhost:8080/login/oauth2/code/github`
3. Сохранить **Client ID** и сгенерировать **Client Secret**.

### 2. Переменные окружения

Перед запуском приложения задать:

- `GITHUB_CLIENT_ID`
- `GITHUB_CLIENT_SECRET`

  Пример для IntelliJ IDEA:
  Run/Debug Configuration → Environment variables.

### 3. База данных

Используется H2 in-memory:

Консоль: `http://localhost:8080/h2-console`  
JDBC URL: `jdbc:h2:mem:testdb`

### 4. Сборка и запуск

```
mvn clean package
mvn spring-boot:run
```

После запуска:

- `http://localhost:8080/` — главная, ссылка «Login with GitHub».
- `http://localhost:8080/user` — профиль (после логина).
- `http://localhost:8080/admin` — админ-страница (только для `ROLE_ADMIN`).
- `http://localhost:8080/logout` — выход с отзывом токена.

---

## Возможные улучшения

- Поддержка нескольких провайдеров (Google, GitLab и т.д.).
- Хранение refresh token и обновление access token.
- Более гибкая модель ролей (отдельная таблица ролей, связь Many-to-Many).
- Бизнес-логика привязки ролей (настройка в БД, а не «жёстко» по логину).
- Логи и аудит в отдельной таблице (история логинов/выходов).
