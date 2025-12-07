## Introduction

This application is a Spring Boot–based API for fetching the mortgage rate and checking the mortgage feasibility.

---

## Key Features

* REST API: Exposes endpoints to :
* 1. Fetch mortgage interest rates.
* 2. Check mortgage feasibility based on income, home value, loan value, and maturity period and calculate monthly mortgage payments based on the formula M = P[ r(1+r)^n ] / [ (1+r)^n – 1] where M is the payment, P is the principal loan amount, r is the monthly interest rate (annual rate/12), and n is the total number of payments (loan term in years * 12).

* Security: Secured with a static bearer token

* CSV Integration: Reads mortgage rates from a CSV file at startup.

* Validation & Exception Handling: Input validation with @Valid and centralized exception mapping via
  GlobalExceptionHandler.

* Testing: Unit and integration tests with JUnit 5 and Mockito.

---

## Design Decisions

* Layered architecture: Controller → Service → Repository, ensuring separation of concerns.

* CSV parsing utility: Encapsulated in CsvUtil for testability and reusability.

* Static token: Simplified, in production, OAuth2 should be used.

---

## Structure Overview

* Controller Layer: Handles REST endpoints (MortgageController).

* Service Layer: Business logic (MortgageServiceImpl).

* Repository Layer: Fetching data (CsvMortgageRateRepository).

* Utilities: CsvUtil, MonthlyPaymentCalculationUtil for parsing and calculting payment.

* Security: BearerTokenFilter for request authentication.

* Exception Handling: GlobalExceptionHandler for consistent error responses.


---

# How to Run
---

## Requirements

* Java 17
* Maven

---

## Build & Run

1. Build:
   mvn clean package

2. Run:

* mvn spring-boot:run
  or
* java -jar target/mortgage-api-1.0.0.jar

---

## Configuration

Default security uses a static bearer token:

* Authorization: Bearer <your_token>

---

## Running in IDE

1. Import the project as Maven
2. Run the main class: MortgageApplication.
3. Use application.properties in src/main/resources/ for local config.

---

## API Endpoints

1. Get All Mortgage Rates
    * GET /api/interest-rates
        * Example:
          curl --location 'http://localhost:8080/api/interest-rates' \
          --header 'Authorization: Bearer <your-token>' \
          --header 'Accept: application/json'
        * Response:
          [
          {
          "maturityPeriod": 1,
          "interestRate": 15.1,
          "lastUpdate": "2025-11-01T11:30:00"
          },
          {
          "maturityPeriod": 2,
          "interestRate": 12.12,
          "lastUpdate": "2025-12-01T21:30:00"
          },
          {
          "maturityPeriod": 5,
          "interestRate": 5.1,
          "lastUpdate": "2025-12-01T13:30:00"
          }
          ]

---

2. Check Mortgage
    * POST /api/mortgage-check
      Content-Type: application/json
      Body:
      {
      "income": ,
      "maturityPeriod": ,
      "loanValue": ,
      "homeValue":
      }
        * Example:
          curl --location 'http://localhost:8080/api/mortgage-check' \
          --header 'Authorization: Bearer secret-token' \
          --header 'Accept: application/json' \
          --header 'Content-Type: application/json' \
          --data '{
          "income": 101,
          "maturityPeriod": 10,
          "loanValue": 200,
          "homeValue": 300
          }'
        * Response:
          {
          "feasible": true,
          "monthlyCosts": 2.14
          }

---

# How to Run Tests

This project uses **JUnit** and **Mockito** for unit testing.

---

## What tests cover

* Controller endpoints: MortgageController tests via MockMvc (validation, authorization behavior, exception mapping).
* Service tests: MortgageServiceImpl (happy/edge/failure scenarios).
* Utilities: MonthlyPaymentCalculationUtilTest, CsvUtil (CSV parsing success and failure).
* Exception handler: GlobalExceptionHandler mapping for custom exceptions and validation errors.
* Security layer: BearerTokenFilter tests that valid and invalid tokens behave correctly.

Test resources

* Put test-mortgage-rates.csv under src/test/resources with appropriate headers:
* Optionally add an empty.csv for empty-CSV test scenarios.

---

## Running Tests

bash
mvn clean test