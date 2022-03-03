# **Backend - Moosic API**
The port provides API to communicate between front-end service, image-processing service and database service.


## **Prerequisite**
- JDK1.8
- Docker
- **moosic_ai_api** container running in Docker on port `9000`
- **Postgres** container running in Docker on port `5432`

## **Getting started**
After cloning this project, go to reload dependency from `pom.xml` to make sure you have satisfied dependency for running.

If your local machine does not have postgres, you can add the service to your own docker by running the command `docker compose up -d`.

The service will be running on port `8000` on your local machine.

## **Run service in docker**
Before running this service, make sure you have satisfied prerequisite services are **running** in **Docker** now.

- Build `.jar` file with following command: `./mvnw package -DskipTests`. `Moosic-API-0.0.1.jar` will appear in `target/` directory
- Build image file with following command: `docker build  -t moosic_backend_api .`
- See built image with command: `docker ps -a`. The image named **moosic_backend_api** appears.
- Run contain from built image with command: `docker run -p 8000:8000 --name moosic_backend_api -d moosic_backend_api`
- See running container with command: `docker ps`
- The service will be running at `localhost:8000` with context path `/api/v1`

