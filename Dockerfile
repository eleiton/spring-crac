FROM bellsoft/liberica-runtime-container:jdk-21-crac-musl as builder
WORKDIR /app
ADD . /app/spring-crac
RUN cd spring-crac && ./mvnw -Dmaven.test.skip=true clean package

FROM bellsoft/liberica-runtime-container:jre-21-crac-slim-musl
WORKDIR /app
COPY --from=builder /app/spring-crac/target/spring-crac-demo-1.0-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-Dspring.context.checkpoint=onRefresh", "-Dspring.crac.enabled=true", "-XX:CRaCCheckpointTo=/checkpoint", "-jar", "spring-crac-demo-1.0-SNAPSHOT.jar"]
