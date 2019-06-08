# Yapay Backend
This repository contains the Yapay backend made with SpringBoot

## Run project
Requirement: Java <br>
Install Maven and run SpringBoot
```
$ brew install maven (Mac OS)
$ sudo apt install maven (Ubuntu)
```
Run SpringBoot with Maven
```
$ cd yapay_backend/yapay-spring-boot
$ mvn spring-boot:run
```

## API Endpoints
### Create payment
#### Request: ```POST /payments```
#### Request Body: 
```js
{
  "amount":       float,
  "companyName":  string,
  "companyPhone": string
}
 ```
 #### Response
Possible error: 400 or 500 error code if any body parameter is missing
 ```js
{
  "id":      uuid as string,
  "qrData":  base64 encoded png
}
 ```

### Confirm payment
#### Request: ```GET /payments/confirm```
```
"paymentId": uuid as string
```

#### Response
Possible error: 400 or 500 error code if payment id is invalid, missing or already confirmed
```
None
```

### Verify if payment is confirmed
#### Request: ```GET /payments/confirm```
```
"paymentId": uuid as string
```

#### Respose
Possible error: 500 error code if payment id is missing <br>
```
true  (payment is confirmed)
false (payment is not confirmed or invalid)
```

