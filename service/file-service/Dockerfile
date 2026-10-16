FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8003
# 创建挂载点目录
RUN mkdir -p /data/upload
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
