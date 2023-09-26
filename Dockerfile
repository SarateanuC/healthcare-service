FROM amazoncorretto:17
WORKDIR /app
COPY build/libs/*.jar healthcare-service.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","healthcare-service.jar"]
