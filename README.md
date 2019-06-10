# Yapay Backend
This repository contains the Yapay backend made with SpringBoot

## Run project
Tested with Java 1.8.0_202 and PostgreSQL 10.8<br>
#### Requirements:
* Java
* Postgresql

Clone repository
```
$ git clone git@github.com:utec-yapay/yapay_backend.git
$ cd yapay_backend
```

Create PostgreSQL database and restore from dump 
```
$ createdb yapay -U postgres
$ cd yapay-database
$ psql yapay < YaPay_dump -U postgres
```

Install Maven
```
$ brew install maven (Mac OS)
$ sudo apt install maven (Ubuntu)
```

Run SpringBoot with Maven
```
$ cd ../yapay-spring-boot
$ mvn spring-boot:run
```

## API Endpoints
This endpoints don't apply to current commit. To get this results...
```
$ git checkout c1f6004418ee7fbb10f22c618f1e2c74934090a4
```


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
plain JSON Web Token with a payload of:
{
  "pid": long int,          // payment id
  "amt": float,             // payment amount
  "cpn": string,            // company name
  "cpp" string with 9 chars // company phone
}
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

