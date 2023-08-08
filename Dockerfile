FROM openjdk:17-oracle
ARG JAR_FILE=build/libs/EcomMarketApp-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} ecom_market.jar
ENTRYPOINT ["java","-jar","ecom_market.jar"]