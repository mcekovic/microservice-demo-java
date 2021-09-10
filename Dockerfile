FROM adoptopenjdk:16-jre-hotspot as builder
WORKDIR application
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM adoptopenjdk:16-jre-hotspot
EXPOSE 8080 9999
VOLUME /application/logs
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

ENTRYPOINT ["java",\
 "-Dspring.profiles.active=docker", "-Xms256m", "-Xmx256m",\
 "-XX:+ExplicitGCInvokesConcurrent", "-Xlog:gc=info:file=logs/gc.log:time,tags:filecount=10,filesize=10m",\
 "-Dcom.sun.management.jmxremote", "-Dcom.sun.management.jmxremote.port=9999", "-Dcom.sun.management.jmxremote.rmi.port=9999", "-Djava.rmi.server.hostname=0.0.0.0", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false",\
 "org.springframework.boot.loader.JarLauncher"]