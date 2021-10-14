## Micronaut 3.1.0 Documentation

- [User Guide](https://docs.micronaut.io/3.1.0/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.1.0/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.1.0/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

## Feature kafka documentation

- [Micronaut Kafka Messaging documentation](https://micronaut-projects.github.io/micronaut-kafka/latest/guide/index.html)

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

Kafka web interface is on `http://localhost:3030/`

see file `docker-compose.yml` for instructions on installing kafka docker container# mn-pricing-kafka

for graal build (you may need to chmod+x mvnw) `./mvnw package`

to build docker image`docker build . -t mn-pricing`

Graal docker build error
```Michaels-iMac:mn-pricing-kafka mherb$ docker build . -t mn-pricing
Sending build context to Docker daemon  66.26MB
Step 1/9 : FROM ghcr.io/graalvm/graalvm-ce:java11-21 as graalvm
 ---> 694935c7310c
Step 2/9 : RUN gu install native-image
 ---> Using cache
 ---> be51ae88af79
Step 3/9 : COPY . /home/app/mn-pricing
 ---> e6e550eb7075
Step 4/9 : WORKDIR /home/app/mn-pricing
 ---> Running in 3fd964fbb5be
Removing intermediate container 3fd964fbb5be
 ---> 44f32cff511f
Step 5/9 : RUN native-image --no-server -cp build/libs/mn-pricing-*-all.jar
 ---> Running in f613955409a2
Warning: Ignoring server-mode native-image argument --no-server.
Error: Please specify class containing the main entry point method. (see --help)
The command '/bin/sh -c native-image --no-server -cp build/libs/mn-pricing-*-all.jar' returned a non-zero code: 1
```