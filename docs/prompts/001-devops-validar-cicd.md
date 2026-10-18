---
description: Valida y prueba como especialista DevOps los scripts de CI/CD (GitHub Actions, Dockerfile) antes de usarlos
argument-hint: [ruta del workflow, por defecto .github/workflows/ci-cd.yml]
---

Actúa como **ingeniero DevOps senior** especialista en **GitHub Actions**, Docker y pipelines para
Java 21 / Spring Boot 4 con Maven. Tu tarea es **probar y validar** los scripts de CI/CD generados
para este repositorio antes de confiar en ellos. Respeta @CLAUDE.md.

## Alcance
- Workflow: `$ARGUMENTS` (si está vacío, `.github/workflows/ci-cd.yml`).
- `Dockerfile` y `.dockerignore` de la raíz.
- Los scripts fueron generados por IA: **no asumas que funcionan**. Toda afirmación ("compila",
  "la imagen arranca", "el job pasa") debe estar respaldada por un comando ejecutado y su salida.

## Forma de trabajo
Ejecuta UN paso a la vez. Al terminar cada paso:
- muestra los comandos ejecutados y un resumen de su salida;
- lista los hallazgos con severidad (**alta / media / baja**), archivo y línea;
- propone la corrección, pero **no modifiques archivos sin mi "aplicar"**;
- **detente y espera mi "continuar"** antes del siguiente paso.
Si una herramienta no está instalada, dime cómo instalarla o usa su imagen Docker; no te la saltes en silencio.
Estoy en **Windows con PowerShell y Docker Desktop**: da los comandos para ese entorno.

## Pasos

### Paso 0 — Inventario (sin ejecutar nada)
1. Lee el workflow, el `Dockerfile`, el `.dockerignore`, el `pom.xml` y `src/main/resources/application.*`.
2. Explica en una tabla qué hace cada job y cada step, cuándo se dispara (PR, push a `main`, tag `v*.*.*`,
   manual) y qué publica.
3. Contrasta el pipeline con el proyecto real y marca incoherencias, como mínimo:
   - nombre de la rama por defecto (`main` o `master`) frente a `on:`;
   - variables `SPRING_DATASOURCE_*` y `SPRING_JPA_*` del workflow frente a las propiedades reales;
   - base de datos que necesitan las pruebas (MySQL del servicio, H2, ninguna);
   - nombre del JAR que genera Maven (ojo con espacios en `artifactId`) frente al `COPY` del `Dockerfile`;
   - puerto de la aplicación frente al `EXPOSE`;
   - versiones de las actions usadas: ¿existen? (verifícalo, no lo supongas).

### Paso 1 — Validación estática
- `actionlint` sobre el workflow (sintaxis, expresiones `${{ }}`, contexts, shellcheck de los `run:`).
- `hadolint` sobre el `Dockerfile`.
- Revisa el `.dockerignore`: que excluya `target/`, `.git/` e IDEs, y que **no** excluya nada que el
  `Dockerfile` copia (`.mvn`, `mvnw`, `pom.xml`, `src`).

### Paso 2 — Pruebas locales del build (lo mismo que hará el job `build`)
1. Levanta un MySQL igual al del servicio del workflow (`docker run` con la misma imagen, base y credenciales).
2. Ejecuta `.\mvnw.cmd -B -ntp clean verify` con las mismas variables de entorno que define el workflow.
3. Si falla, distingue entre un fallo **del pipeline** (configuración) y un fallo **del código**
   (prueba rota); solo los primeros son tu responsabilidad en esta tarea.

### Paso 3 — Pruebas locales de la imagen (lo mismo que hará el job `image`)
1. `docker build` del `Dockerfile` y reporta tamaño de la imagen y tiempo de build.
2. Arranca el contenedor conectado al MySQL del paso 2 y comprueba que la API responde
   (un endpoint real o `actuator/health` si existe).
3. Verifica que el proceso **no** corre como root y que no quedan el JDK, el código fuente ni `target/`
   en la imagen final.
4. Repite el build sin cambios y confirma que las capas de dependencias se reutilizan (caché).

### Paso 4 — Ejecución del workflow en local (opcional)
- Si `act` está disponible, ejecuta el job `build` con un evento `pull_request` simulado.
- `act` no reproduce todo (servicios, caché `type=gha`, `GITHUB_TOKEN`, publicación en GHCR):
  indica qué no se pudo probar y que se validará en el paso 6.

### Paso 5 — Seguridad y buenas prácticas
Revisa y reporta:
- `permissions:` mínimos por job (`packages: write` solo donde se publica).
- Actions de terceros fijadas por **SHA** en lugar de tag (indica el SHA concreto a usar, verificado).
- Que en un `pull_request` **nunca** se haga login ni push a registros.
- Ausencia de secretos o contraseñas reales en archivos versionados (las de CI deben ser efímeras).
- `timeout-minutes`, `concurrency` y caché de Maven bien configurados.
- Propuestas opcionales, cada una justificada y sin aplicarla: escaneo de la imagen (p. ej. Trivy),
  Dependabot para actions y Maven, cobertura con JaCoCo.

### Paso 6 — Prueba real en GitHub
Guíame para validar el pipeline sin riesgo:
1. Rama `ci/validate-pipeline` y PR contra `main`: deben correr `build` e `image` **sin publicar**.
2. Qué revisar en la pestaña Actions (duración, caché de Maven, artefacto `test-reports`, resumen).
3. Tras el merge: comprobar la imagen en GHCR con sus etiquetas (`latest`, `sha-xxxxxxx`).
4. Tag `v0.1.0`: comprobar las etiquetas `0.1.0` y `0.1`.
5. Cómo hacer pública la imagen o dar acceso al paquete si hace falta.

### Paso 7 — Cierre
- Tabla final: comprobación → resultado (✅ / ❌ / ⚠️ no probado) → evidencia.
- Lista de cambios aplicados (archivo y motivo) y de pendientes.
- Un `docs/cicd.md` breve: cómo funciona el pipeline, cómo publicar una versión y cómo diagnosticar
  un fallo típico (permiso de `mvnw`, MySQL no listo, login en GHCR).

## Restricciones
- No hagas `git push`, no crees tags ni publiques imágenes: eso lo hago yo en el paso 6.
- No cambies código de la aplicación para que pase el pipeline; si una prueba falla por el código, repórtalo.
- No añadas dependencias al `pom.xml` ni actions nuevas sin justificarlo y esperar mi aprobación.
- No inventes versiones de actions ni de imágenes: verifica que existen.
- Elimina los contenedores y volúmenes de prueba al terminar cada paso.
