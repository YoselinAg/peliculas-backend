# Películas Backend

API REST para administrar un catálogo de películas y actores. Permite crear, consultar, editar y eliminar fichas, buscar en el catálogo y gestionar el reparto de cada película. Es el microservicio de lógica de negocio del trabajo inicial de **Frameworks Backend y Microservicios** de la Universidad de Alcalá.

## Arquitectura

```text
Postman o frontend → API REST de Spring Boot → Servicio → Spring Data JPA → MySQL
```

- Los **controladores** reciben las peticiones HTTP y validan los datos de entrada.
- El **servicio** aplica las reglas del catálogo, comprueba que los actores del reparto existan y ejecuta las escrituras dentro de transacciones.
- Los **repositorios y entidades JPA** consultan y guardan los datos. La tabla `pelicula_actor` relaciona películas y actores de muchos a muchos.
- Los **DTOs** definen los campos de entrada y salida de la API. Al guardar una película se envían IDs de actores. La respuesta contiene sus fichas dentro de `actores`.

El proyecto usa Java 17 como versión objetivo, Spring Boot 4.0.10, Maven Wrapper, Jakarta Validation y MySQL. El frontend puede consumir la API como un microservicio independiente.

## Arranque local

1. Instala un JDK 17 o posterior e inicia MySQL. Si todavía no has creado la base de datos y sus tablas, ejecuta desde la raíz del proyecto el [script SQL](database/schema.sql) con un usuario de MySQL que tenga permisos para crearlas. Sustituye `TU_USUARIO` por su nombre.

   ```bash
   mysql -h 127.0.0.1 -P 3306 -u TU_USUARIO -p < database/schema.sql
   ```

2. Configura las credenciales en la terminal. Este ejemplo usa zsh y oculta la contraseña mientras la escribes.

   ```zsh
   read 'DB_USER?Usuario de MySQL: '
   read -s 'DB_PASSWORD?Contraseña de MySQL: '
   echo
   export DB_USER DB_PASSWORD
   ```

   La URL JDBC predeterminada es `jdbc:mysql://localhost:3306/peliculas_db`. Si MySQL usa otro servidor o puerto, exporta `DB_URL` con la URL correspondiente antes de arrancar. Hibernate valida el esquema en el arranque.

3. Arranca la aplicación desde la misma terminal.

   ```bash
   ./mvnw spring-boot:run
   ```

   La API estará disponible en `http://localhost:8080`. Puedes comprobar el arranque con `GET http://localhost:8080/api/actores`. Debe devolver `200 OK` y una lista, que estará vacía si todavía no hay actores.

## Endpoints

En Postman, usa **Body → raw → JSON** para `POST` y `PUT`, y **Body → none** para `GET` y `DELETE`. Sustituye `{id}` por el identificador real del recurso.

| Método | Ruta | Operación | Body | Éxito |
| --- | --- | --- | --- | --- |
| GET | `/api/actores` | Listar o buscar actores | — | `200 OK` |
| GET | `/api/actores/{id}` | Consultar un actor | — | `200 OK` |
| POST | `/api/actores` | Crear un actor | JSON de actor | `201 Created` |
| PUT | `/api/actores/{id}` | Editar un actor | JSON completo de actor | `200 OK` |
| DELETE | `/api/actores/{id}` | Borrar un actor | — | `204 No Content` |
| GET | `/api/peliculas` | Listar o buscar películas | — | `200 OK` |
| GET | `/api/peliculas/{id}` | Consultar una película y su reparto | — | `200 OK` |
| POST | `/api/peliculas` | Crear una película | JSON de película | `201 Created` |
| PUT | `/api/peliculas/{id}` | Editar una película | JSON completo de película | `200 OK` |
| DELETE | `/api/peliculas/{id}` | Borrar una película | — | `204 No Content` |

Las altas devuelven la ficha creada y una cabecera `Location` con su ruta. Los datos inválidos y los IDs de reparto inexistentes devuelven `400 Bad Request`. Consultar, editar o borrar un recurso inexistente devuelve `404 Not Found`.

### Filtros de búsqueda

Los filtros buscan por coincidencia parcial sin distinguir mayúsculas de minúsculas. Si envías varios filtros, deben cumplirse todos. Sin filtros obtienes el listado completo. Si una búsqueda no encuentra resultados, devuelve `200 OK` con `[]`.

| Ruta | Parámetro | Ejemplo de valor en Postman |
| --- | --- | --- |
| `/api/actores` | `nombre` | Fisher |
| `/api/actores` | `paisNacimiento` | Estados Unidos |
| `/api/peliculas` | `titulo` | esperanza |
| `/api/peliculas` | `genero` | ficción |
| `/api/peliculas` | `actor` | Fisher |

Introduce los parámetros en la pestaña **Params** de Postman. Por ejemplo, `GET /api/peliculas?actor=Fisher` busca películas con algún integrante del reparto cuyo nombre contiene «Fisher».

### JSON de actor

Usa este cuerpo para `POST /api/actores` o `PUT /api/actores/{id}`. Los tres campos son obligatorios y la fecha va en formato `AAAA-MM-DD`.

```json
{
  "nombre": "Carrie Fisher",
  "fechaNacimiento": "1956-10-21",
  "paisNacimiento": "Estados Unidos"
}
```

### JSON de película

Usa este cuerpo para `POST /api/peliculas` o `PUT /api/peliculas/{id}`. Sustituye el `1` por el ID de un actor que exista. La duración se expresa en minutos. La sinopsis y la portada son opcionales. La portada se guarda como texto, normalmente una URL o ruta de imagen.

```json
{
  "titulo": "Una nueva esperanza",
  "anio": 1977,
  "duracion": 121,
  "pais": "Estados Unidos",
  "director": "George Lucas",
  "genero": "Ciencia ficción",
  "sinopsis": "Una aventura espacial.",
  "portada": "https://example.com/portada.jpg",
  "actorIds": [1]
}
```

Al editar una película, envía el reparto completo en `actorIds`. Una lista vacía o la omisión de ese campo deja el reparto vacío. En la respuesta, `actores` contiene los datos de los integrantes del reparto.

## Prueba en Postman

1. Crea el actor de ejemplo y usa el ID de la respuesta para crear la película.
2. Consulta las dos fichas por ID, prueba los dos listados y ejecuta las cinco búsquedas de la tabla. Comprueba que la búsqueda por actor devuelve la película asociada.
3. Edita el nombre del actor y el título de la película con `PUT`. Consulta de nuevo la película y verifica que aparecen los dos cambios.
4. Envía un `PUT` con duración cero o con un ID de actor inexistente. Comprueba el estado `400` y consulta la ficha para verificar que conserva los datos anteriores.
5. Borra la película de prueba y verifica que su ficha devuelve `404` y que el actor sigue disponible. Después borra el actor y verifica que su ficha también devuelve `404`. Para probar el orden inverso, borra primero un actor de prueba vinculado a otra película y comprueba que la película sigue disponible con el reparto actualizado.

## Autoría

**Yoselin Andrango**

Máster Universitario en Desarrollo Ágil de Software para la Web · Universidad de Alcalá

Frameworks Backend y Microservicios · Curso 2026–2027
