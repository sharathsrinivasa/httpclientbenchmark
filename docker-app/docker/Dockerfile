FROM openjdk:11-jdk

RUN apt-get update && apt-get install -y wget zip && apt-get remove -y scala-library scala

# https://gist.github.com/Frozenfire92/3627e38dc47ca581d6d024c14c1cf4a9
RUN wget http://scala-lang.org/files/archive/scala-2.12.8.deb
RUN dpkg -i scala-2.12.8.deb
RUN apt-get update
RUN apt-get install scala

RUN mkdir -p /app/testng

RUN mkdir /docker-java-home/lib/jfr
COPY jfr /docker-java-home/lib/jfr

WORKDIR /app

COPY docker-app.zip /app/app.zip
RUN unzip /app/app.zip -d /app
COPY run.scala /app

COPY testng/*.xml /app/testng/

ENTRYPOINT ["scala", "run.scala"]

#ENTRYPOINT ["java", "-jar", "/app/app.jar"]
