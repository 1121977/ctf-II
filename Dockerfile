FROM openjdk:15
MAINTAINER CTF
COPY build/libs/CTFService-1.0-SNAPSHOT-all.jar app.jar
RUN mkdir src
RUN mkdir src/main
RUN mkdir src/main/resources
RUN mkdir src/main/resources/web
RUN mkdir src/main/resources/web/static
RUN mkdir src/main/resources/web/static/css
RUN mkdir src/main/resources/web/static/js
COPY src/main/resources/web src/main/resources/web/
COPY src/main/resources/web/static src/main/resources/web/static/
COPY src/main/resources/web/static/css src/main/resources/web/static/css/
COPY src/main/resources/web/static/js src/main/resources/web/static/js
ENTRYPOINT ["java","-jar","/app.jar", "-csvInput", "crew.csv"]
