FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-alpine

LABEL github="https://github.com/anjeyy/grpc-sample"
LABEL maintainer="anjeyy"

WORKDIR /app

# https://github.com/mhart/alpine-node/issues/48#issuecomment-370171836
RUN addgroup -g 1000 -S anjeyy && \
    adduser -u 1000 -S anjeyy -G anjeyy && \
    chmod 777 -R /tmp && \
    chown -R anjeyy:anjeyy /app

USER anjeyy

COPY target/grpc-server*.jar /app/grpc-server.jar

CMD java -jar /app/grpc-server.jar
