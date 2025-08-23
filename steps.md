# 🛡️ Spring Security Playground — Step‑by‑Step Exercises

This project provides a Thymeleaf front‑end scaffold with Spring Boot, intentionally left wide open at first. The goal is to progressively secure it, from basics to advanced.

---

## ✅ Pre‑Step 0: Sanity Check

* Run the app, confirm all pages are reachable without login.
* Verify CSRF tokens are rendered in forms.

---

## 1) Basic Authentication & Login Page

**Goal:** Protect `/account/**` and `/admin/**`; keep `/`, `/login`, `/register` public.

* Configure `SecurityFilterChain` to require authentication for private areas.
* Add `.formLogin().loginPage("/login")`.
* Define in‑memory users: `user` (ROLE\_USER), `admin` (ROLE\_ADMIN).
* ✅ Acceptance: Profile/Admin redirect unauthenticated users → login; logout works; CSRF enforced.

---

## 2) Password Encoding + User Store

**Goal:** Move from in‑memory → persistent store.

* Use `PasswordEncoder` (BCrypt).
* Implement `UserDetailsService` backed by JPA/JDBC.
* Implement registration: hash password, save user disabled (pending verification).
* ✅ Acceptance: Can register and login with persisted accounts.

---

## 3) Email Verification

**Goal:** Accounts must confirm email.

* On register: generate token (log to console or store in DB).
* `/verify-email` sets `enabled=true`.
* Prevent login until verified.
* ✅ Acceptance: Unverified → login blocked; verified → login succeeds.

---

## 4) Roles & Method Security

**Goal:** Apply fine‑grained auth.

* Enable `@EnableMethodSecurity`.
* Use `@PreAuthorize` for admin controllers.
* Add domain check: only profile owner can edit.
* ✅ Acceptance: Unauthorized → `/access-denied`.

---

## 5) CSRF, CORS, Sessions

**Goal:** Harden stateful security.

* Keep CSRF enabled.
* Configure cookie flags (SameSite, Secure).
* Add session fixation protection & concurrency control.
* Build `/account/sessions` page listing sessions; implement revoke.
* ✅ Acceptance: CSRF blocks invalid POST; session revocation works.

---

## 6) Password Reset & Change

**Goal:** Secure credential lifecycle.

* Forgot password: issue token (TTL), reset page.
* Reset invalidates old token.
* Change password requires current password, invalidates other sessions.
* ✅ Acceptance: Password resets/changing work securely.

---

## 7) Multi‑Factor Authentication (MFA)

**Goal:** Add TOTP.

* Generate secret, QR code, verify setup.
* Store `mfaEnabled` and secret.
* Modify login flow: if MFA enabled, redirect to `/mfa/verify`.
* ✅ Acceptance: MFA required for enabled accounts.

---

## 8) OAuth2 Login + Consent

**Goal:** Add social login.

* Configure Google/GitHub provider.
* Map OAuth2 user to local account.
* Customize consent page.
* ✅ Acceptance: External login works; new users mapped.

---

## 9) API + JWT Resource Server

**Goal:** Hybrid auth.

* Create `/api/**` endpoints secured by JWT.
* Configure RSA keys, stateless resource server.
* Keep web app session‑based.
* ✅ Acceptance: Browser → session; API → Bearer token.

---

## 10) Security Hardening

**Goal:** Production‑grade defenses.

* Add headers: HSTS, CSP, Referrer‑Policy, Permissions‑Policy.
* Escape all user data in Thymeleaf.
* Implement lockout/backoff after failed logins.
* Rate limiting filter.
* ✅ Acceptance: Headers visible, XSS attempts fail.

---

## 11) Events & Custom Extensions

**Goal:** Extend Spring Security.

* Listen to `AuthenticationSuccessEvent`, `FailureBadCredentialsEvent`.
* Implement custom `AuthenticationProvider` (e.g. magic link).
* Insert a custom filter.
* ✅ Acceptance: Events logged; provider and filter invoked.

---

## 12) Testing Security

**Goal:** Build confidence.

* Use `spring-security-test` with `@WithMockUser`, `with(csrf())`.
* Test both allowed and forbidden access.
* ✅ Acceptance: Tests red/green cycles catch misconfigurations.

---

## Workflow

1. Do exercise 1, test, commit.
2. Share solution for review.
3. Iterate, then advance to the next step.

This path will take you from **basic auth → enterprise‑level features** with a single evolving project.
