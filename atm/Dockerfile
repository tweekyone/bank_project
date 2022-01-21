FROM tomcat:9-jre11

ENV APP_DATABASE_URL jdbc:postgresql://localhost:5432/bank
ENV APP_DATABASE_USER user
ENV APP_DATABASE_PASSWORD user

ENV JAVA_OPTS "$JAVA_OPTS -Ddatabase.url=${APP_DATABASE_URL} -Ddatabase.username=${APP_DATABASE_USER} -Ddatabase.password=${APP_DATABASE_PASSWORD}"

COPY build/libs/atm.war /usr/local/tomcat/webapps
