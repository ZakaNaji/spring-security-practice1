# Security Playground (Thymeleaf UI)

This is a minimalist Spring Boot project with Thymeleaf pages designed specifically to practice **Spring Security** step-by-step.

- Everything is **open** by default (see `SecurityConfig`).
- Forms include CSRF fields already.
- Pages exist for: login, register, email verify, forgot/reset password, profile, change password, sessions, MFA setup/verify, admin dashboard/users, OAuth2 consent, access denied, logout.

## Run
```bash
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

Then visit http://localhost:8080/
