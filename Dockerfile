FROM  eclipse-temurin:21.0.1_12-jre-alpine
ARG VERSION
ADD ./build/libs/gvs-service-${VERSION}.jar /gvs-service-${VERSION}.jar
ENV VERSION "$VERSION"
ENTRYPOINT java -jar gvs-service-${VERSION}.jar