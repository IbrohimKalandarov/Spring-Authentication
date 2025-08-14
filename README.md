# Spring Boot JWT Authentication API

## Loyiha haqida

Assalomu alaykum. Bu loyiha Spring Boot va JWT (JSON Web Token) asosida qurilgan to'liq autentifikatsiya va avtorizatsiya tizimini taqdim
etadi. Loyiha RESTful API arxitekturasidan foydalanadi va O'zbek tili qo'llab-quvvatlanadi.

## Asosiy Xususiyatlar

- **JWT Authentication** - Token asosida autentifikatsiya
- **Role-based Authorization** - Rol asosida ruxsat tizimi
- **Phone Number Verification** - SMS OTP orqali telefon raqami tasdiqlash
- **Password Management** - Parol almashtirish va tiklash
- **User Management** - Admin panel orqali foydalanuvchi boshqaruvi
- **Profile Management** - Shaxsiy profil boshqaruvi

## Maven Dependencies

Loyihada quyidagi asosiy kutubxonalar ishlatilgan:

```xml

<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- JWT -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>

    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>

    <!-- Swagger/OpenAPI -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.0.2</version>
    </dependency>
</dependencies>
```

## Loyihani O'rnatish va Ishga Tushirish

### 1. Talab qilingan Dasturlar

- Java 17 yoki undan yuqori versiya
- Maven 3.6+
- PostgreSQL (ma'lumotlar bazasi)
- IDE (IntelliJ IDEA)

### 2. Loyihani Klonlash

```bash
git clone <repository-url>
cd spring-auth-project
```

### 3. Ma'lumotlar Bazasini Sozlash

`application.properties` faylini sozlang:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://172.17.0.3:5432/database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name= org.postgresql.Driver
# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
# SMS Service Configuration (agar SMS xizmati ishlatilsa)
sms.api.key=your-sms-api-key
sms.api.url=your-sms-service-url
```

### 4. Loyihani Ishga Tushirish

```bash
mvn clean install
mvn spring-boot:run
```

yoki IDE orqali `Application.java` faylni ishga tushiring.

## API Endpoints

### Authentication Endpoints (`/api/auth`)

| Method | Endpoint                                  | Tavsif                                   |
|--------|-------------------------------------------|------------------------------------------|
| POST   | `/api/auth/register`                      | Yangi foydalanuvchi ro'yxatdan o'tkazish |
| POST   | `/api/auth/verify-phone/{code}`           | OTP kod orqali telefon tasdiqlash        |
| POST   | `/api/auth/reset-otp-code/{phoneNumber}`  | OTP kodni qayta yuborish                 |
| POST   | `/api/auth/login`                         | Tizimga kirish                           |
| POST   | `/api/auth/refresh-token`                 | Tokenni yangilash                        |
| POST   | `/api/auth/forgot-password/{phoneNumber}` | Parolni unutgan holda tiklash            |
| POST   | `/api/auth/check-forgot-password`         | Unutilgan parolni tasdiqlash             |
| PUT    | `/api/auth/change-password`               | Parolni o'zgartirish                     |
| POST   | `/api/auth/logout`                        | Tizimdan chiqish                         |

### Admin Endpoints (`/api/admin`)

**Role Required:** `SUPER_ADMIN` yoki `ADMIN`

| Method | Endpoint                 | Tavsif                               |
|--------|--------------------------|--------------------------------------|
| GET    | `/api/admin/user/{id}`   | ID bo'yicha foydalanuvchini qidirish |
| GET    | `/api/admin/users`       | Barcha foydalanuvchilarni ko'rish    |
| GET    | `/api/admin/roles`       | Barcha rollarni ko'rish              |
| POST   | `/api/admin/change-role` | Foydalanuvchi rolini o'zgartirish    |

### Profile Endpoints (`/api/profile`)

| Method | Endpoint                                        | Tavsif                     |
|--------|-------------------------------------------------|----------------------------|
| PATCH  | `/api/profile/me`                               | Profilni yangilash         |
| GET    | `/api/profile/me`                               | O'z profilini ko'rish      |
| POST   | `/api/profile/update-phoneNumber/{phoneNumber}` | Telefon raqamini yangilash |

### Test Endpoints (`/api/test`)

| Method | Endpoint                | Tavsif                                                                                 |
|--------|-------------------------|----------------------------------------------------------------------------------------|
| GET    | `/api/test/blocked`     | Bloklangan url foydalanuvchi Token bilan murojaat qilsa bo'ladi. Tokensiz ruxsat yo'q! |
| GET    | `/api/test/non-blocked` | Bloklanmagan url ya'ni Tokensiz bu yo'lga murojaat qilish mumkin.                      |

## Request/Response Namunalari

### Ro'yxatdan O'tish (Register)

```json
POST /api/auth/register
{
  "firstName": "Ibrohim",
  "lastName": "Kalandarov",
  "phoneNumber": "+998901234567",
  "password": "mySecurePassword123"
}
```

### Kirish (Login)

```json
POST /api/auth/login
{
  "phoneNumber": "+998901234567",
  "password": "mySecurePassword123"
}
```

### Response Format

```json
{
  "success": true,
  "message": "Muvaffaqiyatli bajarildi",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
  }
}
```

## Authentication

APIdan foydalanish uchun JWT token kerak. Tokenni Headerda quyidagicha yuboring:

```
Authorization: Bearer <your-jwt-token>
```

## User Roles

- **USER** - Oddiy foydalanuvchi
- **ADMIN** - Administrator
- **SUPER_ADMIN** - Super Administrator

## Error Handling

API xatolari quyidagi formatda qaytariladi:

```json
{
  "success": false,
  "message": "Xatolik xabari",
  "errors": [
    "Batafsil xatolik ma'lumotlari"
  ]
}
```

## Validation Rules

- **phoneNumber**: O'zbekiston telefon raqami formati (+998XXXXXXXXX)
- **password**: Kamida 6 ta belgi
- **OTP kod**: 6 xonali raqam

## Swagger UI

API hujjatlari va test qilish uchun:

```
http://localhost:8080/swagger-ui.html
```

## Muammolarni Hal Qilish (Troubleshooting)

### Keng Uchraydigan Muammolar:

1. **Database Connection Error**
    - Ma'lumotlar bazasi sozlamalarini tekshiring
    - Database server ishlab turganini tasdiqlang

2. **JWT Token Invalid**
    - Token muddatini tekshiring
    - Secret key to'g'riligini tasdiqlang

3. **SMS OTP Not Working**
    - SMS service konfiguratsiyasini tekshiring
    - API key va URL to'g'riligini tasdiqlang

4. **Permission Denied (403)**
    - Foydalanuvchi rolini tekshiring
    - Endpoint uchun kerakli ruxsat borligini tasdiqlang

### Loglarni Ko'rish

```bash
# Application loglarini ko'rish
tail -f logs/application.log

# Ma'lumotlar bazasi so'rovlarini ko'rish
# application.properties da spring.jpa.show-sql=true qiling
```



### Code Style

- Java naming conventionsni kuzating
- Lombok annotationslardan foydalaning
- Validation annotationslarini qo'llang

## Qo'llab-quvvatlash

Savollar va muammolar uchun:

- GitHub: https://github.com/IbrohimKalandarov/Spring-Authentication
- Email: ibrohim1kalandarov@gmail.com
- Telegram: [@Ibrohim_kalandarov](https://t.me/Ibrohim_kalandarov)

---

**Oxirgi yangilanish:** 2025-yil iyul
**Versiya:** 1.0
