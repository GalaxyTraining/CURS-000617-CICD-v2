# CLAUDE.md

Contexto base del repositorio `api-be-project-manager-v1`. Léelo antes de crear o modificar código.

## 1. Descripción

- **Propósito:** API REST para la gestión de proyectos. <!-- TODO: completar con las entidades principales (p. ej. Project, Task, Member) y qué hace cada una -->
- **Estilo arquitectónico:** **arquitectura en capas** (controller → service → repository). Es intencional: este proyecto es material de curso. **No migres a Clean Architecture ni hexagonal** salvo que se pida explícitamente.
- **Paquete base:** `pe.edu.galaxy.training.java.arq.layered.api_be_project_manager`
- **Idioma:** código en **inglés**; documentación y explicaciones en **español**.

## 2. Stack

| Área | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Framework | **Spring Boot 4.0.x** (Spring Framework 7, Jakarta EE 11, Hibernate 7, Jackson 3) |
| Build | Maven (wrapper incluido) |
| Persistencia | Spring Data JPA + **MySQL** (`mysql-connector-j`) |
| Mapeo | ModelMapper |
| Boilerplate | Lombok |
| Validación | Jakarta Bean Validation |
| Pruebas | `spring-boot-starter-*-test` (JUnit 5, AssertJ, Mockito) |

> **Spring Boot 4 cambió varias cosas respecto a Boot 3.** No asumas APIs de Boot 3:
> - `@MockBean` ya no existe: usa `@MockitoBean` (`org.springframework.test.context.bean.override.mockito`).
> - Las anotaciones de test slice cambiaron de paquete y vienen de los starters `*-test` por tecnología
>   (p. ej. `@WebMvcTest` desde `spring-boot-starter-webmvc-test`). Verifica el import compilando.
> - Jackson 3 usa el paquete `tools.jackson.*` (las anotaciones siguen en `com.fasterxml.jackson.annotation`).
> Si dudas de una API, compila antes de dar algo por bueno.

## 3. Comandos

```powershell
.\mvnw.cmd clean verify          # Windows: compilar y ejecutar todas las pruebas
.\mvnw.cmd spring-boot:run       # Levantar la API
.\mvnw.cmd test -Dtest=NombreTest
```
En Linux/macOS usa `./mvnw`. No edites nada dentro de `target/`.

## 4. Estructura de paquetes

| Paquete | Contenido | Reglas |
|---|---|---|
| `controller` | `@RestController` | Solo HTTP: recibe DTO, valida con `@Valid`, llama al service, devuelve DTO. Sin lógica de negocio ni acceso a repositorios. |
| `service` | Interfaz + implementación (`XxxService` / `XxxServiceImpl`) | Lógica de negocio y transacciones (`@Transactional`; `readOnly = true` en consultas). Trabaja con entidades y devuelve DTOs. |
| `repository` | `JpaRepository<Entidad, Id>` | Solo acceso a datos. Consultas derivadas o `@Query`; nada de lógica. |
| `entity` | Entidades JPA | Nunca se exponen en la API. |
| `dto` | Request/Response | Records cuando sea posible; validaciones de formato aquí. |
| `mapper` | Conversión entidad ↔ DTO (ModelMapper) | Toda conversión pasa por aquí, no en controllers. |
| `exception` | Excepciones propias + `@RestControllerAdvice` | Única capa que traduce excepciones a códigos HTTP. |
| `config` | `@Configuration` (beans como `ModelMapper`) | |
| `commons`, `util` | Utilidades compartidas | No meter lógica de negocio aquí. |

**Regla de dependencia:** `controller → service → repository`. Un controller nunca usa un repository; un repository nunca conoce DTOs.

## 5. Convenciones

- **Inyección por constructor** (`@RequiredArgsConstructor` + campos `private final`). Prohibido `@Autowired` en campos.
- **Lombok en entidades:** `@Getter`, `@Setter`, `@NoArgsConstructor`. **No uses `@Data` ni `@EqualsAndHashCode` por defecto** en entidades JPA (rompe con relaciones lazy y colecciones).
- **Relaciones JPA:** `fetch = LAZY` por defecto; evita N+1 con `@EntityGraph` o `join fetch` cuando se lean colecciones.
- **ModelMapper:** cuidado con el mapeo implícito por nombre; si un campo no mapea solo, configura el `TypeMap` en `config` en vez de mapear a mano en el service.
- `Optional` solo como retorno. Sin `null` como "no encontrado": lanza una excepción propia (`ResourceNotFoundException` o equivalente).
- Nombres: `ProjectController`, `ProjectService`/`ProjectServiceImpl`, `ProjectRepository`, `ProjectEntity` o `Project` (respeta lo que ya exista en el código).

## 6. API REST

- Versionado por URL: `/api/v1/...`; recursos en plural y kebab-case.
- Códigos: `201` + `Location` al crear, `204` sin contenido, `400` validación, `404` no existe, `409` conflicto, `422` regla de negocio.
- Errores en formato **Problem Details (RFC 9457)** desde `exception`. Nunca devolver stack traces.

## 7. Pruebas

| Capa | Tipo | Herramienta |
|---|---|---|
| service | Unitarias | JUnit 5 + Mockito (mock del repository) |
| repository | Slice | `@DataJpaTest` |
| controller | Slice | `@WebMvcTest` + `@MockitoBean` del service |

- Nombres `should<Resultado>When<Condición>`, estructura Given / When / Then.
- Todo cambio de lógica en un service incluye su prueba en el mismo cambio.

## 8. Cómo debe trabajar Claude en este repo

- Antes de crear código, identifica la capa (sección 4) y respeta la regla de dependencia.
- Antes de modificar algo, lee la clase y sus vecinas: sigue el estilo que ya existe en el proyecto.
- **No añadas dependencias al `pom.xml` ni cambies versiones** sin indicarlo y justificarlo.
- **No cambies la configuración de base de datos** (`application.properties`/`.yml`, `ddl-auto`) sin avisar.
- Nunca escribas credenciales en archivos versionados; usa variables de entorno.
- Ejecuta `.\mvnw.cmd clean verify` antes de dar una tarea por terminada.
- Si una petición obliga a romper estas reglas, detente y explica el conflicto antes de implementar.
