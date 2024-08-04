# Williams Adolfo Vega Montenegro - williamsv1697@gmail.com

## Infraestructura del Proyecto

- Base de datos: Firebase
- Versión de Java: 21
- Framework: Spring Boot 3.3.2
- Herramienta de construcción: Maven

![BarberiaPro Logo compressed](https://github.com/user-attachments/assets/db11b57e-17bd-420e-a0ea-2e025f88698f)
> Imagen generada con IA

# BarberPro-API
Esta es una aplicación Spring Boot que proporciona una API REST para la gestión de servicios de barbería.

## Requisitos Previos

- Java 21
- Maven
- Docker (opcional)

## Ejecutar el Proyecto

### Usando Maven

1. Clonar el repositorio:
   ```
   git clone https://github.com/yourusername/product-api.
   ```

2. Ir al directorio del proyecto:
   ```
   cd BarberPro-API
   ```

3. Hacer un "Build" al proyecto:
   ```
   mvn clean install
   ```

4. Ejecutar el proyecto:
   ```
   mvn spring-boot:run
   ```

Puedes acceder como administrador usando la URL 'http://localhost:1697/api/stats'

(User: admin | Password: admin) 

### Usando Docker

1. Construir la imagen de Docker:
   ```
   build -t barberpro-api:latest .
   ```

2. Ejecutar el contenedor:
   ```
   docker run -p 1697:1697 barberpro-api:latest
   ```
Puedes acceder como administrador usando la URL 'http://localhost:1697/api/stats'

(User: admin | Password: admin)

## API Endpoints

### Productos

| Método | Endpoint | Descripción |
| ------ | -------- | ----------- |
| POST   | /api/products | Crear un nuevo producto |
| PUT    | /api/products/{id} | Actualizar un producto existente por ID |
| DELETE | /api/products/{id} | Eliminar un producto por ID |
| GET    | /api/products | Obtener todos los productos, opcionalmente con paginación y ordenamiento |
| GET    | /api/products/{id} | Obtener un producto por ID |
| POST   | /api/products/search | Buscar productos por múltiples campos |

### Estadísticas

| Método | Endpoint | Descripción |
| ------ | -------- | ----------- |
| GET    | /api/stats | Obtener todas las estadísticas, opcionalmente con paginación y ordenamiento |
| GET    | /api/stats/process/{id} | Obtener estadísticas filtradas por ID de proceso |
| GET    | /api/stats/method/{name} | Obtener estadísticas filtradas por nombre de método |
| GET    | /api/stats/status/{type} | Obtener estadísticas filtradas por tipo de estado |
| GET    | /api/stats/status | Obtener un conteo de estadísticas agrupadas por estado |
| GET    | /api/stats/categories | Obtener conteos de productos agrupados por categoría |
| GET    | /api/stats/averages | Obtener el tiempo promedio de ejecución agrupado por nombre de método |


## Estructura de Archivos

El proyecto utiliza la siguiente estructura de archivos:

```
barberia-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── wvega/
│   │   │           └── barberproapi/
│   │   │               ├── controller/
│   │   │               │   ├── ProductController.java
│   │   │               │   └── StatsController.java
│   │   │               ├── repository/
│   │   │               │   └── FireBaseInitializer.java
│   │   │               ├── security/
│   │   │               │   └── SecurityConfig.java
│   │   │               ├── model/
│   │   │               │   ├── ListData.java
│   │   │               │   ├── ProductDto.java
│   │   │               │   └── StatDto.java
│   │   │               ├── service/
│   │   │               │   ├── ProductService.java
│   │   │               │   └── StatsService.java
│   │   │               ├── utils/
│   │   │               │   ├── Constants.java
│   │   │               │   ├── ProductUtils.java
│   │   │               │   └── ResponseWS.java
│   │   │               └── BarberProApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── wvega/
│                   └── barberproapi/
│                       ├── service/
│                       │   ├── ProductServiceTest.java
│                       │   └── StatsServiceTest.java
│                       └── BarberProApiApplicationTests.java
├── pom.xml
├── Dockerfile
└── README.md
```
