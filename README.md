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
    git clone https://github.com/username/repository.git
    ```

2. Navigate into the directory:

    ```bash
    cd repository
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
    
### Importing Postman Collection

1. Open Postman.

2. In the top left corner, click `File > Import`.

3. Navigate to the `Postman Collections/` directory of this repository.

4. Select the `.json` file containing the Postman collection and click `Open`.

5. The collection should now be available in your Postman app.

## Built With

- [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
- [Maven](https://maven.apache.org/) - Dependency Management
- [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) - Used to write the application
- [Postman](https://www.getpostman.com/) - API Development Environment

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

