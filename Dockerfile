FROM clojure:lein-2.11.2-bullseye-slim

RUN mkdir -p /app
WORKDIR /app

COPY project.clj .
RUN lein deps

COPY src src
COPY resources resources

RUN lein uberjar

EXPOSE 8000

CMD ["java", "-jar", "/app/target/uberjar/science-pub-api-0.1.0-SNAPSHOT-standalone.jar"]