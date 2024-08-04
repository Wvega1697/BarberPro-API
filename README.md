# Williams Adolfo Vega Montenegro - williamsv1697@gmail.com

![BarberiaPro Logo compressed](https://github.com/user-attachments/assets/db11b57e-17bd-420e-a0ea-2e025f88698f)
> Imagen generada con IA

# BarberPro-API
Esta es una aplicación Spring Boot que proporciona una API REST para la gestión de servicios de barbería.

## Requisitos Previos

- Java 21
- Maven
- Docker (opcional)

## Infraestructura del Proyecto

- Base de datos: Firebase
- Versión de Java: 21
- Framework: Spring Boot 3.3.2
- Herramienta de construcción: Maven

## Estructura de Archivos

El proyecto sigue la siguiente estructura de archivos:

```
barberia-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── wvega/
│   │   │           └── barberproapi/
│   │   │               ├── controller/
│   │   │               │   └── ProductController.java
│   │   │               │   └── StatsController.java
│   │   │               ├── repository/
│   │   │               │   └── FireBaseInitializer.java
│   │   │               ├── model/
│   │   │               │   └── ListData.java
│   │   │               │   └── ProductDto.java
│   │   │               │   └── StatDto.java
│   │   │               ├── service/
│   │   │               │   ├── ProductService.java
│   │   │               │   ├── StatsService.java
│   │   │               ├── utils/
│   │   │               │   ├── Constants.java
│   │   │               │   └── ProductUtils.java
│   │   │               │   └── ResponseWS.java
│   │   │               └── BarberProApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   └── test/
│       └── java/
│           └── com/
│               └── wvega/
│                   └── barberproapi/
│                       └── service/
│                           └── BarberProApiApplicationTests.java
├── pom.xml
└── README.md
```

