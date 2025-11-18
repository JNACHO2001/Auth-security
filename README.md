## 🚀 Visión General del Proyecto

Este proyecto es una aplicación Spring Boot que implementa un esquema básico de autenticación y autorización para una API REST. Provee endpoints para autenticación (login y registro) y está diseñada para operar en modo sin estado (stateless) usando tokens JWT para autenticar peticiones.

El código está organizado con un modelo de usuario JPA, un repositorio para persistencia, servicios de autenticación y una configuración de seguridad centrada en JWT.

## 🔒 Arquitectura de Seguridad

- Versión de Spring Boot: 3.5.7 (declarado en `pom.xml`). La dependencia `spring-boot-starter-security` se gestiona por el parent y corresponde a la versión incluida en ese release.
- Mecanismo de autenticación: JWT (JSON Web Tokens). Se genera un token firmado con HS256 en `springSecuriry.jwt.JwtService` y el proyecto incluye un filtro `JwtAuthFilter` que extrae el token del encabezado `Authorization: Bearer <token>`.
- Proveedor de usuarios: implementación basada en base de datos con Spring Data JPA. `User` implementa `UserDetails` y `UserRepository` extiende `JpaRepository`. El `AuthenticationProvider` es un `DaoAuthenticationProvider` configurado en `springSecuriry.config.applicationConfig` que usa un `UserDetailsService` que carga usuarios mediante `UserRepository`.

### Detalles importantes encontrados
- Generación de token: `JwtService.getToken(UserDetails)` crea un JWT con subject = username, sin claims adicionales por defecto, firmado con HS256 y con expiración configurada en el código.
- Persistencia: configuración para MySQL en `application.properties` (URL: `jdbc:mysql://localhost/auth_prueba`, usuario `root`, contraseña `root`). JPA está configurado con `hibernate.ddl-auto=update`.

## 🛠️ Tecnologías Clave

- spring-boot-starter-web
- spring-boot-starter-security
- spring-boot-starter-data-jpa
- com.mysql:mysql-connector-j
- io.jsonwebtoken: jjwt-api, jjwt-impl, jjwt-jackson (v0.12.6)
- spring-boot-devtools (runtime, opcional)
- spring-boot-starter-test / spring-security-test (test)

Todos los artefactos y versiones aparecen en el `pom.xml` del proyecto.

## ⚙️ Configuración de Seguridad (SecurityFilterChain)

Archivo principal: `springSecuriry.config.SecurityConfig`

Resumen de configuración encontrada:

- CSRF: deshabilitado (.csrf().disable()). Comentario en código indica que no es necesario al usar JWT.
- Rutas públicas: `requestMatchers("/api/auth/**").permitAll()` — todo lo bajo `/api/auth/` (login/register) es accesible sin autenticación.
- Reglas por defecto: `anyRequest().authenticated()` — todas las demás rutas requieren autenticación.
- Sesiones: `SessionCreationPolicy.STATELESS` — la aplicación no mantiene sesiones HTTP y espera autenticación en cada petición mediante token.
- Filtros: `JwtAuthFilter` se añade antes de `UsernamePasswordAuthenticationFilter`.
- AuthenticationProvider: se registra el `AuthenticationProvider` (un `DaoAuthenticationProvider`) para la autenticación basada en `UserDetailsService` y `PasswordEncoder`.

Rutas protegidas (según la configuración actual)

- `/api/auth/**` — accesible públicamente.
- Todas las demás rutas (`/**` salvo las anteriores) — requieren que la petición esté autenticada.

Nota: el proyecto no define reglas basadas en roles/authorities explícitas en `SecurityConfig` (por ejemplo `/api/admin/** requires ROLE_ADMIN`), sólo requiere autenticación general. Los roles existen (enum `Role { ADMIN, USER }`) y `User.getAuthorities()` devuelve la autoridad exactamente con el nombre del enum (por ejemplo `USER` o `ADMIN`). Si se desea usar `hasRole("USER")`/`hasRole("ADMIN")`, conviene normalizar a la convención `ROLE_` o usar `hasAuthority(...)`.

Configuraciones personalizadas encontradas

- Filtro JWT (`JwtAuthFilter`) implementado extendiendo `OncePerRequestFilter`. Actualmente el filtro extrae el token del header `Authorization` y, si no existe, continúa la cadena. En la implementación actual el filtro no valida ni establece una `Authentication` en el `SecurityContext` — por lo tanto, aunque se extraiga el token, no se está usando para autenticar la petición dentro del `SecurityContext`.
- `JwtService` firma tokens con HS256 usando una clave definida en el código (`SECRET_KEY`) y utiliza `Decoders.BASE64.decode(SECRET_KEY)`. Recomendación: almacenar la clave fuera del código (variables de entorno o vault) y asegurar que la cadena esté codificada en Base64 si se usa ese decodificador.

Limitaciones/observaciones detectadas (relevantes para producción)

- `JwtAuthFilter` no valida ni aplica el token al `SecurityContext`. Para que el JWT proteja efectivamente las rutas, el filtro debe:
  - validar la firma y expiración del token (utilizando `JwtService` o `io.jsonwebtoken`),
  - extraer el username, cargar el `UserDetails` y construir una `UsernamePasswordAuthenticationToken` con sus autoridades,
  - asignarla al `SecurityContextHolder`.
- La expiración del token en `JwtService` está configurada como `System.currentTimeMillis() + 1000 * 60 * 24`, es decir, 24 minutos (24 * 60 * 1000 ms). Confirme si la intención era 24 horas; en ese caso debe usarse `1000 * 60 * 60 * 24`.
- `SECRET_KEY` está embebida en código fuente y tratada mediante `Decoders.BASE64.decode(...)`. Es recomendable usar variables de entorno y, si la clave no es Base64, ajustar la codificación/decodificación.
- El endpoint de registro en `AuthController.register` actualmente devuelve un mensaje estático (`"creado "`) y no invoca `authservice.register(...)`. El servicio `AuthService` tiene el método `register` implementado y debería ser llamado por el controlador.

## Prerrequisitos y Ejecución Local

Requisitos mínimos:

- Java 17+
- Maven (o usar el wrapper `./mvnw` incluido)
- MySQL (opcional para persistencia; la configuración por defecto apunta a `jdbc:mysql://localhost/auth_prueba`)

Pasos para ejecutar localmente:

1. Clonar el repositorio:

```bash
git clone <repo-url>
cd demo
```

2. Ajustar configuración de base de datos si es necesario en `src/main/resources/application.properties` (usuario/contraseña/URL).

3. Construir la aplicación:

```bash
./mvnw clean package
```

4. Ejecutar:

```bash
./mvnw spring-boot:run
# o
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

Observación: las credenciales por defecto en `application.properties` son `root`/`root` y la base de datos `auth_prueba` se crea si no existe (según URL con `createDatabaseIfNotExist=true`).

## 📚 Endpoints Clave

Los controladores principales observados ofrecen los siguientes endpoints:

- POST /api/auth/login
  - Descripción: autentica a un usuario con `username` y `password`. Devuelve un `AuthResponse` con el token JWT si la autenticación es exitosa.
  - Request body: `LoginRequest` (contiene `username`, `password`).

- POST /api/auth/register
  - Descripción: endpoint de registro. Actualmente el controlador devuelve un mensaje estático y no llama al servicio de registro; recomendamos invocar `AuthService.register(RegisterRequest)` para persistir usuarios.
  - Request body: `RegisterRequest` (contiene `username`, `password`, `email`, `ciudad`).

Otros artefactos relevantes:

- `AuthService.login(LoginRequest)` — usa `AuthenticationManager` para autenticar, carga el usuario desde `UserRepository` y genera un token mediante `JwtService`.
- `User` — entidad JPA que implementa `UserDetails` y expone la autoridad basada en el enum `Role`.

## Buenas prácticas y próximos pasos recomendados

1. Externalizar `SECRET_KEY` (variables de entorno, `application.yaml` no versionado o vault).
2. Completar `JwtAuthFilter` para validar tokens y establecer la `Authentication` en `SecurityContextHolder`.
3. Corregir la unidad temporal del `setExpiration(...)` si la intención era 24 horas.
4. Hacer que `AuthController.register` invoque `authservice.register(...)` y devuelva un DTO adecuado (por ejemplo, con id, username y rol, sin la contraseña).
5. Considerar usar prefijo `ROLE_` en las autoridades o utilizar `hasAuthority(...)` explícitamente al declarar reglas por rol.
6. Evitar hardcodear credenciales de base de datos en archivos versionados; usar perfiles y variables de entorno para entornos distintos.

