FROM alpine

RUN apk update && \
    apk upgrade
RUN apk add openjdk8

VOLUME /tmp
ARG DEPENDENCY=target/dependency

COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

CMD crond && java -cp /app:app/lib/* org.kpmp.Application
