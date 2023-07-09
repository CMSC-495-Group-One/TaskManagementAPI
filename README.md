# Task Management API

This is a repository for the Spring Boot project. The project uses Spring Boot with Java 11 and integrates with Postman for API testing.

## Prerequisites

- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- Maven
- [Postman](https://www.postman.com/downloads/)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Running the Application Locally

1. Clone the repository:

    ```bash
    git clone https://github.com/ConnorDBurge/TaskManagementAPI.git
    ```

2. Navigate into the directory:

    ```bash
    cd TaskManagementAPI
    ```

3. Run the Spring Boot application:

    ```bash
    mvn spring-boot:run
    ```
    
    Or if you prefer to package the application as a `.jar` file and then run it, you can use the following commands:
    
    ```bash
    mvn clean package
    java -jar target/myproject-0.0.1-SNAPSHOT.jar
    ```
## Setting Up Postman

1. Download and install Postman from [Postman's official site](https://www.postman.com/downloads/).
2. Launch Postman and sign in or sign up.

### Importing Postman Collection

1. In Postman, click on the "Import" button.
2. Click on "Link".
3. Paste the following link into the textbox: 
   [Postman Collection](https://github.com/CMSC-495-Group-One/TaskManagementAPI/blob/main/Postman%20Collections/postman-collection.json)
4. Click on "Continue" and then "Import" to confirm.

### Running Our Collection

1. After importing, you can see our collection in the "Collections" sidebar.
2. Click on the collection name to expand it and see the different API requests.

### Authentication

**Note:** By including the `Authorization` header with the `Bearer <authentication_token>` value, you simulate an authenticated request in Postman. The backend will then validate the token and allow access to the authenticated endpoint.

### Obtaining an authentication token:

1. Send a `POST` request to the backend `/v1/auth/signin` endpoint with valid credentials.
2. A script stores the `accessToken` as an environment variable in Postman and is used in all other HTTP calls.

This is for your convenience. You won’t need to copy and paste it anywhere.

### Testing the authenticated endpoints:

1. Make the request to the authenticated endpoint (eg. `/v1/users`, `/v1/tasks`) in Postman.
2. The backend will validate the authentication token included in the `Authorization` header and process the request accordingly. The response will contain the JSON body if the token was valid. 

- If you receive a status `401`, the API is properly working. You just need a new token. See **Obtaining an authentication token** section.
- If you receive a status `500`, there was an internal server error. Attempt to create a new user using the `/v1/auth/signup` endpoint. If you receive a status `2xx`, you can sign in using step 1 in **Obtaining an authentication token** section. Otherwise, there is a bug.

## Endpoints
<!-- ENDPOINTS_START -->
<!-- ENDPOINTS_END -->

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
- [Maven](https://maven.apache.org/) - Dependency Management
- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) - Used to write the application
- [Postman](https://www.getpostman.com/) - API Development Environment

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

