docker build --build-arg JAR_FILE=build/libs/*.jar -t gennadyyonov/hello-okta-api .
docker run --env-file ./env/.env.localhost -p 8070:8070 gennadyyonov/hello-okta-api