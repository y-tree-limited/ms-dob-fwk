FROM 986794016656.dkr.ecr.eu-west-2.amazonaws.com/baseimage:aws-corretto-17.0.4.9-1_deb_13_09_2022

RUN set -eux \
  && apt-get update \
  && apt-get install -y --no-install-recommends

ARG APP_NAME
ENV PORT 8080
EXPOSE 8080
COPY build/libs/${APP_NAME}*.jar /opt/app.jar
WORKDIR /opt
CMD ["java", "-jar", "app.jar"]
