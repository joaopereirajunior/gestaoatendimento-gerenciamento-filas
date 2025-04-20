FROM maven:3.9.8-amazoncorretto-17-al2023 AS build

WORKDIR /app

COPY . /app

RUN mvn clean package -DskipTests

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar gestaoAtendimentoFila.jar

EXPOSE 8081

CMD ["java", "-jar", "gestaoAtendimentoFila.jar"]