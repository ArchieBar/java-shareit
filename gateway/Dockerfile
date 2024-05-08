FROM amazoncorretto:11
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081
WORKDIR /app
COPY ./target/shareit-gateway-0.0.1-SNAPSHOT.jar /app
CMD ["java", "-jar", "/app/shareit-gateway-0.0.1-SNAPSHOT.jar"]