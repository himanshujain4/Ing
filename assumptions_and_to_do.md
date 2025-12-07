# Assumptions made during implementation.
---

## Data and Calculation

* Monthly payment calculation is based on the formula provided on wikipedia. This formula is accepted for the assessment purpose.
* https://en.wikipedia.org/wiki/Mortgage_calculator
*  M = P[ r(1+r)^n ] / [ (1+r)^n – 1] where M is the payment, P is the principal loan amount, r is the monthly interest rate (annual rate/12), and n is the total number of payments (loan term in years * 12).
* List of mortgage rate doesn't contain very large number of rows. If so, then we need to limit(add pagination etc) while fetching the list of all the interest rate from API.
* Mortgage Rate CSV has following header:
  Maturity Period,Interest Rate,Last Update

## Security

* Static bearer token acceptable for exercise.
* Production would require OAuth2 + vault-stored secrets.

---

# To Do

List of to-do's for production env.

---

## High Priority

* Replace static bearer token with OAuth2 token.
* Use Auth server for token verification
* Store secrets in a secure vault (eg. Azure key vault).
* Persist mortgage rates in a db instead of CSV.

---

## Medium Priority

* Add end-to-end integration tests.
* Add metrics and logging for monitoring and observability.
* Provide Swagger documentation.

---

## Low Priority

* Add CI/CD pipeline automation.
