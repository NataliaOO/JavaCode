# Практическое задание — Spring Security + JWT + роли + блокировка аккаунта

Этот проект демонстрирует настройку **JWT-аутентификации** в Spring Boot-приложении с:

- кастомными ролями (`USER`, `MODERATOR`, `SUPER_ADMIN`);
- защитой эндпоинтов на уровне ролей;
- блокировкой аккаунтов после нескольких неудачных попыток входа;
- логированием аутентификации и основных событий безопасности;
- (опционально) работой через HTTPS с самоподписанным сертификатом.

---

## Стек технологий

- **Java** 23  
- **Spring Boot** 3.3.x  
- **Spring Security**  
- **Spring Web**  
- **Spring Data JPA**  
- **H2** (in-memory БД для тестов)  
- **JJWT** 0.12.x (работа с JWT)  
- **Lombok**  

---

## Основные возможности

### Модель пользователя и роли

Сущность `User`:

- `id` — идентификатор;
- `username` — логин (уникальный);
- `password` — захэшированный пароль (BCrypt);
- `role` — enum `Role`:
  - `USER`
  - `MODERATOR`
  - `SUPER_ADMIN`
- `accountNonLocked` — признак блокировки аккаунта:
  - `true` — аккаунт не заблокирован;
  - `false` — аккаунт заблокирован;
- `failedLoginAttempts` — счётчик неудачных попыток входа.

Роль хранится как `ENUM` и в `UserDetailsService` преобразуется в `GrantedAuthority` вида:

```text
ROLE_USER, ROLE_MODERATOR, ROLE_SUPER_ADMIN
```

### HTTPS

Проект поддерживает работу через HTTPS (при включении соответствующих настроек в `application.properties`):

1. Создаётся самоподписанный сертификат через `keytool`:

   ```
   keytool -genkeypair      
   -alias jwt-demo      
   -keyalg RSA      
   -keysize 2048      
   -storetype PKCS12      
   -keystore jwt-demo.p12      
   -validity 3650
   ```

2. Файл `jwt-demo.p12` кладётся в `src/main/resources`.

3. В `application.properties` включаются SSL-настройки:

   ```properties
   server.port=8443

   server.ssl.enabled=true
   server.ssl.key-store=classpath:jwt-demo.p12
   server.ssl.key-store-password=...       # пароль из keytool
   server.ssl.key-store-type=PKCS12
   server.ssl.key-alias=jwt-demo
   ```

4. В `SecurityConfig` можно выключить принудительный HTTPS:

   ```
   http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
   ```

Все запросы (особенно логин и передача JWT) при этом идут по защищённому соединению.

---

## Как запустить и проверить

1. Собрать проект:

   ```bash
   mvn clean package
   ```

2. Запустить приложение (по умолчанию на `http://localhost:8080` или `https://localhost:8443` — в зависимости от настроек):

   ```bash
   mvn spring-boot:run
   ```

3. Создать тестовых пользователей (через `CommandLineRunner` или руками вставить в БД).

4. Получить JWT:

   - запрос на `/auth/login` (POST, логин/пароль);
   - в ответ приходит JWT-токен.
   ```
     curl -X POST "http://localhost:8080/auth/login" \
     -H "Content-Type: application/json" \
     -d "{\"username\":\"user\",\"password\":\"password\"}"
   ```

5. Вызвать защищённый эндпоинт:

   - добавить заголовок `Authorization: Bearer <TOKEN>`;
   - проверить, что доступ есть только при валидном токене и нужной роли.
     ```
     curl "http://localhost:8080/api/profile" \
     -H "Authorization: Bearer <TOKEN>"
     ```

6. Протестировать блокировку:

   - несколько раз залогиниться с неверным паролем;
   - убедиться, что аккаунт блокируется;
   - разблокировать его через админский эндпоинт.
    ``` 
    curl -X PATCH "http://localhost:8080/api/admin/users/{ID}/unlock" \
    -H "Authorization: Bearer <TOKEN_ADMIN>"
    ```

---

## Что необходимо реализовать в будущем

**Дополнительные требования (опционально):**

- Реализовать **обновление JWT через Refresh Token**:
  - генерация пары токенов: `access token` (короткоживущий) + `refresh token` (долгоживущий);
  - эндпоинт `/auth/refresh`, который:
    - принимает валидный refresh token;
    - проверяет его подпись и срок действия;
    - при валидности — выдаёт новый access token;
  - возможное хранение refresh-токенов (или их идентификаторов) в БД для возможности отзыва.

---

## Улучшения

1. **Хранить секретный ключ и пароли только в защищённых конфигурациях**  
   Не оставлять секреты в исходном коде. Использовать:
   - переменные окружения,
   - `application-prod.yml` + шифрование,
   - секрет-хранилища (Vault, Kubernetes Secrets и т.п.).

2. **Единый формат ошибок и ответа**  
   Вместо “сырых” строк (`"Bad credentials"`) лучше использовать JSON-обёртку:

   ```json
   {
     "timestamp": "...",
     "status": 401,
     "error": "Unauthorized",
     "message": "Bad credentials",
     "path": "/auth/login"
   }
   ```

   Это удобно для фронтенда и для логирования.

3. **Централизованный обработчик исключений**  
   Настроить `@RestControllerAdvice` с обработкой:
   - ошибок валидации,
   - `AccessDeniedException`,
   - `BadCredentialsException`,
   - бизнес-ошибок.

4. **Юнит- и интеграционные тесты**  
   - протестировать:
     - логику генерации/валидации JWT;
     - блокировку аккаунта;
     - доступ к эндпоинтам с разными ролями (через `MockMvc` + `@WithMockUser`);
   - сделать smoke-тест логина и доступа с реальным JWT.

5. **Аудит и мониторинг**  
   - писать логи попыток логина, блокировок, ошибок доступа;
   - по возможности отправлять метрики (успешные/неуспешные логины, 401/403) в Prometheus/Grafana или аналог.

6. **CORS и CSRF**  
   - если к API обращается браузерный фронтенд — аккуратно настроить CORS;
   - для чисто REST + JWT `csrf` можно отключать, но важно понимать, почему и где это безопасно.
