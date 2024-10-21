# Spring Boot Testing

## Tech Stack
- **Spring Boot**
- **JUnit** for unit and integration testing
- **Mockito** for mocking dependencies
- **AssertJ** for fluent assertions
- **Kafka** for messaging
- **PostgreSQL** for database integration
- **MongoDB** for NoSQL database integration
- **Docker** and **Docker Compose** for service orchestration

## Test Coverage
This repository contains multiple Spring Boot projects and:
- Over **250 unit and integration tests** covering various Spring Boot components.
- Tests for **native queries** using both **JDBC** and **R2DBC**.
- Thorough testing of security, Kafka configurations, and reactive components.
- **It is not intended for production use but can be used as a reference for developing robust test suites in real projects.**

      Native Query Tests for Jdbc and R2dbc
      Service Method Unit Tests/ Itegration Tests
      WebMvc Tests
      WebFlux Test
      Kafka Configuration Tests
      Security Configurations Tests
      JPA Auditing Tests
#### Using common testing utilities,

    Mockito
    JUnit, AssertJ
    mockMvc, Webclient
    Mockito Argument Matchers
    Bean Mocking, Object Mocking
    Injecting Authentication
    Multipart Controllers
    StepVerifier, ArgumentCaptor
    Testing Reactive metnods and Controllers

    And more

## Known Issues
- Some tests may take longer when running with large datasets in the PostgreSQL service.
- Reactive WebFlux tests may require tuning depending on the machine's processing power.

---

## Setup Instructions

#### Prerequisites
- Java 17+
- Maven/Gradle
- Docker and Docker Compose

#### Run the Tests 

Use the docker-compose.yml file to set up the necessary infrastructure for testing:

    version: '3.8'

    services:
      postgres:
        container_name: postgres
        image: postgres
        environment:
          POSTGRES_USER: username
          POSTGRES_PASSWORD: password
        volumes:
          - postgres:/var/lib/postgresql/data  
        ports:
          - "5432:5432"
        restart: unless-stopped
        networks:
          - TestingNetwork

      mongodb:
        image: mongo:latest
        container_name: mongodb
        ports:
          - "27017:27017"
        environment:
          MONGO_INITDB_DATABASE: demo
        volumes:
          - mongo-data:/data/db
        networks:
          - TestingNetwork

      zookeeper:
        image: confluentinc/cp-zookeeper:latest
        container_name: ReelsOrbit-zookeeper
        environment:
          ZOOKEEPER_CLIENT_PORT: 2181
          ZOOKEEPER_TICK_TIME: 2000
        networks:
          - TestingNetwork

      kafka:
        image: confluentinc/cp-kafka:latest
        container_name: ReelsOrbit-kafka
        ports:
          - "9092:9092"
        depends_on:
          - zookeeper
        environment:
          KAFKA_BROKER_ID: 1
          KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
          KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_INTERNAL:PLAINTEXT
          KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092,PLAINTEXT_INTERNAL://ReelsOrbit-kafka:29092
          KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
          KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
          KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
        networks:
          - TestingNetwork

      mail-dev:
        container_name: ReelsOrbit-mail
        image: maildev/maildev
        ports:
          - "1080:1080"
          - "1025:1025"

    volumes:
      mongo-data:
      postgres:

    networks:
      TestingNetwork:
        driver: bridge
