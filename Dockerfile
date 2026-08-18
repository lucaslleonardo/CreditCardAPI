FROM maven:3.9.6-eclipse-temurin-21 AS build
# imagem que tem maven e java21 pra compilar projeto

WORKDIR /app
#define pasta de trabalho dentro do container

COPY pom.xml .
RUN mvn dependency:go-offline
#pega o pom / FEITO ANTES DE PEGAR O CODIGO pra aproveitar cache

COPY src ./src

RUN mvn clean package -DskipTests
#copia codigo fonte e compila gerando um .jar

FROM eclipse-temurin:21-jre
#usa imagem menor

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
#copia o .jar feito anteriormente e transforma na imagem menor

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
#comando que roda quando inicia