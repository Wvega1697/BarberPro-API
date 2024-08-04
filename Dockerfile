# Usar una imagen base ligera de OpenJDK 21
FROM openjdk:21-jdk-slim

# Configurar el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR en el contenedor
COPY target/BarberPro-API-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre la aplicación Spring Boot
EXPOSE 1697

# Definir el comando de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

#docker build -t barberpro-api:latest .
#docker run -p 1697:1697 barberpro-api:latest