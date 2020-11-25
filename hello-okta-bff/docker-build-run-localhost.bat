docker build -t gennadyyonov/hello-okta-bff .
docker run --env-file ./env/.env.localhost -p 8060:8060 gennadyyonov/hello-okta-bff