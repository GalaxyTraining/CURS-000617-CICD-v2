# CI/CD

> Documento en construcción: se completa al cerrar la validación del pipeline.

## Build local

El pipeline compila con **JDK 21**. Si tu JDK por defecto es otro (p. ej. 25), fija `JAVA_HOME`
solo para la sesión antes de usar el wrapper; Lombok 1.18.30 no soporta JDK posteriores a 21 de forma fiable.

```powershell
$env:JAVA_HOME = 'D:\Program Files\Java\jdk-21'   # ajusta la ruta a tu instalación
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
.\mvnw.cmd -B -ntp clean verify
```

Las pruebas (`@SpringBootTest`) necesitan un MySQL accesible. Para reproducir el job `build`:

```powershell
docker run -d --name ci-mysql -e MYSQL_DATABASE=project_manager -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 mysql:8.4
$env:SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3306/project_manager'
$env:SPRING_DATASOURCE_USERNAME = 'root'
$env:SPRING_DATASOURCE_PASSWORD = 'root'
$env:SPRING_JPA_HIBERNATE_DDL_AUTO = 'create-drop'
.\mvnw.cmd -B -ntp clean verify
docker rm -f -v ci-mysql
```
