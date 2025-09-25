# Sıfırdan bir freeDOS makinem olduğunu varsayalım.
# Bu projeyi bu makineye nasıl kurardım?
# Bu üreteceğim image'ı maven:3.9-11 21jdk imageından başlat.

#Single Stage Dockerfile
FROM maven:3.9.11-eclipse-temurin-21
WORKDIR /app
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -DskipTests clean package
# Güvenlik Adımları
#RUN addgroup -g 1000 spring && adduser -D -u 1000 -G spring spring
#USER spring
#
EXPOSE 8761
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar target/*.jar"]