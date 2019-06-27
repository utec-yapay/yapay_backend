# Yapay Backend
This repository contains the Yapay backend made with SpringBoot

## Run project
Tested with Java 1.8.0_202 and PostgreSQL 10.8<br>
#### Requirements:
* Java
* PostgreSQL

Clone repository
```console
$ git clone git@github.com:utec-yapay/yapay_backend.git
$ cd yapay_backend
```

Make sure PostgreSQL is running (there should be some active processes named "postgres")
Create PostgreSQL database and restore from dump
```console
$ createdb yapay -U postgres
$ cd yapay-database
$ psql yapay < YaPay_dump -U postgres
```

Set PostgreSQL password
```console
$ cd ../yapay-spring-boot
$ password=<here-goes-postgres-password>       (don't include whitespaces)
$ sed -i "" "s/postgrespass/$password/" src/main/resources/application.properties
```
If you get an error about failed postgres authentication, it means that the password wasn't set up correctly.
Go to ```yapay-spring-boot/src/main/resources/application.properties``` and change the field ```spring.datasource.password```

Install Maven
```console
$ brew install maven (Mac OS)
$ sudo apt install maven (Ubuntu)
```

Run SpringBoot with Maven
```console
$ mvn spring-boot:run
```

Test
```console
$ curl http://localhost:8080/payments (MacOS)
$ wget http://localhost:8080/payments (Ubuntu)
```
After running this commands, you should get a ```[]```


## API Endpoints
### List payments
#### Request ```GET /payments```
#### Response
List of payments
```js
[
  {
    "id": long int,
    "company": {
        "phone": string with 9 chars,
        "name": string
    },
    "totalAmount": float,
    "confirmed": boolean
  },
  ...
]
```

### Create payment
#### Request: ```POST /payments```
#### Request Body:
```js
{
  "amt": float,              // payment amount
  "cpn": string,             // company name
  "cpp": string with 9 chars // company phone
}
 ```
 #### Response
 ```js
plain JSON Web Token string with a payload of:
{
  "pid": long int,          // payment id
  "amt": float,             // payment amount
  "cpn": string,            // company name
  "cpp" string with 9 chars // company phone
}
and expiration (of 1 minute) in header
 ```
 :warning: Possible error: 400 code if any body parameter is missing or if length of cpp value is different from 9

### Confirm payment
#### Request: ```GET /payments/confirm```
#### Headers
```js
"pid": long int // payment id
```
#### Response
When the payment is confirmed it is updated in the database
```
true  (payment is confirmed and UI received the signal)
false (payment is confirmed but UI didn't get the signal)
```
:warning: Possible error: 400 error code if pid is missing, doesn't exist or is already confirmed

### Get new JWT for existing payment
If your JWT is expired, you're going to need another one
#### Request: ```GET /payments/jwt```
#### Headers
```js
"pid": long int // payment id
```
#### Response
```
plain JSON Web Token string with a payload of:
{
  "pid": long int,          // payment id
  "amt": float,             // payment amount
  "cpn": string,            // company name
  "cpp" string with 9 chars // company phone
}
and expiration (of 1 minute) in header
```
:warning: Possible error: 400 error code if pid is missing or doesn't exist


### SonarCloud
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=yapay_spring_boot&metric=alert_status)](https://sonarcloud.io/dashboard?id=yapay_spring_boot)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=yapay_spring_boot&metric=bugs)](https://sonarcloud.io/dashboard?id=yapay_spring_boot)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=yapay_spring_boot&metric=code_smells)](https://sonarcloud.io/dashboard?id=yapay_spring_boot)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=yapay_spring_boot&metric=coverage)](https://sonarcloud.io/dashboard?id=yapay_spring_boot)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=yapay_spring_boot&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=yapay_spring_boot)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=yapay_spring_boot&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=yapay_spring_boot)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=yapay_spring_boot&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=yapay_spring_boot)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=yapay_spring_boot&metric=security_rating)](https://sonarcloud.io/dashboard?id=yapay_spring_boot)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=yapay_spring_boot&metric=sqale_index)](https://sonarcloud.io/dashboard?id=yapay_spring_boot)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=yapay_spring_boot&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=yapay_spring_boot)

https://sonarcloud.io/dashboard?id=yapay_spring_boot
