FROM adoptopenjdk:11-jre-hotspot

WORKDIR /app

COPY target/ms_employeeAPI-0.0.1-SNAPSHOT.jar /app/springboot-restful-ms_employeeAPI.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "springboot-restful-ms_employeeAPI.jar"]