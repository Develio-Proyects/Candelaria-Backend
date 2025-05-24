# Para construir la imagen con los últimos cambios, antes construir el proyecto con el comando "./gradlew bootJar"
FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]