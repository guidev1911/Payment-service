FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/payment-service-0.0.1-SNAPSHOT.jar /app/payment-service.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "payment-service.jar"]
