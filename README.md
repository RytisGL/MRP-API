# MRP-API
Material requirements planning API
# User Management controller Documentation

This documentation provides an overview of the User Management controller, including endpoints for user registration, login, retrieval, updating, and deletion.


## Authorization

All endpoints except /login and /register require a Bearer Token for authorization.

### Register User
**Endpoint:** `POST /users/register`

Registers a new user.

- **URL:** `http://localhost:8080/users/register`
- **Method:** `POST`
- **User authority:** `Anonymous`

**Body:**
```json
{
    "firstname": "firstname",
    "lastname": "lastname",
    "email": "email@email.com",
    "password": "password1A"
}
```

**Example Request:**
```bash
request: 'http://localhost:8080/users/register' 
data: '{
    "firstname": "firstname",
    "lastname": "lastname",
    "email": "email@email.com",
    "password": "password1A"
}'
```

**Success Response:**
- **Status Code:** `201 CREATED`
- **Body:**
  ```json
  {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXJsZXlAZXhhbXBsZS5jb20iLCJpYXQiOjE3MTcyNzYyODIsImV4cCI6MTcxNzI5NDI4Mn0.2yPMhEwwZWnZV-yK3Iw7oD9Gcel7ep8qdEyL88vPgVg"
  }
  ```

### Login User
**Endpoint:** `POST /users/login`

Logs in a user.

- **URL:** `http://localhost:8080/users/login`
- **Method:** `POST`
- **User authority:** `Anonymous`

**Body:**
```json
{
    "email": "email@email.com",
    "password": "password1A"
}
```

**Example Request:**
```bash
request: 'http://localhost:8080/users/login' 
data: '{
    "email": "email@email.com",
    "password": "password1A"
}'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYXJsZXlAZXhhbXBsZS5jb20iLCJpYXQiOjE3MTcyNzc1NjIsImV4cCI6MTcxNzI5NTU2Mn0.jMuCDyBdmjskAcuFnwJCZbEwu8_AXQfetEyPFL9zHfA"
  }
  ```

### Get All Users
**Endpoint:** `GET /users`

Retrieves all users.

- **URL:** `http://localhost:8080/users`
- **Method:** `GET`
- **User authority:** `Admin`
- **Authorization:** Bearer Token

**Example Request:**
```bash
request: 'http://localhost:8080/users' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  [
      {
          "id": 1,
          "email": "admin@mail.com",
          "role": "ADMIN"
      },
      {
          "id": 2,
          "email": "Eliza.Olson@hotmail.com",
          "role": "USER"
      },
      {
          "id": 52,
          "email": "carley@example.com",
          "role": "USER"
      }
  ]
  ```

### Get User by Email
**Endpoint:** `GET /users?email=`

Retrieves a user by email.

- **URL:** `http://localhost:8080/users?email=`
- **Method:** `GET`
- **User authority:** `Admin`
- **Authorization:** Bearer Token
- **Query Params:** `email`

**Example Request:**
```bash
request: 'http://localhost:8080/users?email=adolph@example.com' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  [
      {
          "id": 1202,
          "email": "adolph@example.com",
          "role": "USER"
      }
  ]
  ```

### Change User Authority by ID
**Endpoint:** `GET /authority?role=`

Changes a user's authority.

- **URL:** `http://localhost:8080/users/authority?role=admin`
- **Method:** `PATCH`
- **User authority:** `Admin`
- **Authorization:** Bearer Token

**Query Params:**
- `role=admin`

**Example Request:**
```bash
request: PATCH 'http://localhost:8080/users/authority?role=admin' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "id": 52,
      "email": "carley@example.com",
      "role": "ADMIN"
  }
  ```

### Delete User by ID
**Endpoint:** `GET /users/{id}`

Deletes a user by ID.

- **URL:** `http://localhost:8080/users/{id}`
- **Method:** `DELETE`
- **User authority:** `Admin`
- **Authorization:** Bearer Token

**Example Request:**
 ```bash
request: DELETE 'http://localhost:8080/users/52'
header: 'Authorization: Bearer <token>'
```
**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "id": 52,
      "email": "carley@example.com",
      "role": "ADMIN"
  }
  ```

# Orders controller's Documentation

This document provides an overview of the endpoints available in the Orders controller. Each endpoint includes the necessary information for authorization, request structure, and example responses.

## Authorization

All endpoints require a Bearer Token for authorization.

### Get Customer Orders
**Endpoint:** `GET /orders`

Retrieves customer orders.

- **URL:** `http://localhost:8080/orders`
- **Method:** `GET`
- **User authority:** `Admin, Manager`
- **Authorization:** Bearer Token

**Optional filters**

- **Request Body:**
  ```json
  {
      "customer": "Customer #1",
        "product": "Product #1",
        "status": "In progress"
  }

**Example Request:**
  ```bash
request: GET 'http://localhost:8080/orders' 
optional data: '{
    "customer": "Customer #1",
      "product": "Product #1",
      "status": "In progress"
}'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  [
      {
          "customer": "Customer #1",
          "product": "Product #1",
          "status": "In progress",
          "id": 25,
          "orderDate": "2024-06-03"
      },
  ]
  ```

### Get Customer Order by ID
**Endpoint:** `GET /orders/{id}`

Retrieves a customer order by its ID.

- **URL:** `http://localhost:8080/orders/{id}`
- **Method:** `GET`
- **User authority:** `Admin, Manager`
- **Authorization:** Bearer Token

**Example Request:**
  ```bash
request: 'http://localhost:8080/orders/25'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "customer": "Customer #1",
      "product": "Product #1",
      "status": "In progress",
      "id": 25,
      "orderDate": "2024-06-03"
  }
  ```

### Get Customer Order's Jobs
**Endpoint:** `GET /orders/{id}/jobs`

Retrieves jobs associated with a customer order.

- **URL:** `http://localhost:8080/orders/{id}/jobs`
- **Method:** `GET`
- **User authority:** `Admin, Manager`
- **Authorization:** Bearer Token

**Example Request:**
  ```bash
request: 'http://localhost:8080/orders/25/jobs'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "customer": "Customer #1",
      "product": "Product #1",
      "status": "In progress",
      "id": 25,
      "orderDate": "2024-06-03",
      "jobs": [
          {
              "type": "Manufacturing",
              "details": "Manufacturing parts #1 Product #1",
              "status": "Complete",
              "startDate": "2024-06-03",
              "updatedAt": "2024-06-03T09:54:51",
              "id": 52
          },
      ]
  }
  ```

### Get Customer Order's Templates
**Endpoint:** `GET /orders/templates`

Retrieves available order templates.

- **URL:** `http://localhost:8080/orders/templates`
- **Method:** `GET`
- **User authority:** `Admin, Manager`
- **Authorization:** Bearer Token

**Example Request:**
  ```bash
request: 'http://localhost:8080/orders/templates'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  [
      {
          "id": 15,
          "product": "Product #4"
      }
  ]
  ```

### Get Customer Order's Template by ID
**Endpoint:** `GET /orders/templates/{id}`

Retrieves a specific order template by ID.

- **URL:** `http://localhost:8080/orders/templates/{id}`
- **Method:** `GET`
- **User authority:** `Admin, Manager`
- **Authorization:** Bearer Token

**Example Request:**
  ```bash
request: 'http://localhost:8080/orders/templates/15'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "id": 15,
      "product": "Product #4",
      "jobs": [
          {
              "type": "Manufacturing",
              "details": "Manufacturing parts #1 Product #4",
              "status": "In progress",
              "requisitions": [
                  {
                      "quantity": 200,
                      "status": "In progress"
                  }
              ]
          },
      ]
  }
  ```

### Create Customer Order
**Endpoint:** `POST /orders`

Creates a new customer order.

- **URL:** `http://localhost:8080/orders`
- **Method:** `POST`
- **User authority:** `Admin, Manager`
- **Authorization:** Bearer Token

**Request Body:**
  ```json
  {
      "customer": "Customer test",
      "product": "Product test",
      "status": "In progress"
  }
  ```

**Example Request:**
  ```bash
request: 'http://localhost:8080/orders' 
data: '{
      "customer": "Customer test",
      "product": "Product test",
      "status": "In progress"
  }'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `201 Created`
- **Body:**
  ```json
  {
      "customer": "Customer test",
      "product": "Product test",
      "status": "In progress",
      "id": 32,
      "orderDate": "2024-06-03"
  }
  ```

### Create Job by Customer Order ID
**Endpoint:** `POST /orders/{id}/jobs`

Creates a job associated with a specific customer order.

- **URL:** `http://localhost:8080/orders/{id}/jobs`
- **Method:** `POST`
- **User authority:** `Admin, Manager`
- **Authorization:** Bearer Token

**Request Body:**
  ```json
  {
      "type" : "Type test",
      "details" : "Details test",
      "status" : "In progress"
  }
  ```

**Example Request:**
  ```bash
request: 'http://localhost:8080/orders/{id}/jobs' 
data: '{
      "type" : "Type test",
      "details" : "Details test",
      "status" : "In progress"
  }'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `201 Created`
- **Body:**
  ```json
  {
      "type": "Type test",
      "details": "Details test",
      "status": "In progress",
      "startDate": "2024-06-07",
      "updatedAt": "2024-06-07T02:37:24",
      "id": 99
  }
  ```

### Create Template from Customer Order
**Endpoint:** `POST /orders/{id}/templates`

Creates a template from a specific customer order.

- **URL:** `http://localhost:8080/orders/{id}/templates`
- **Method:** `POST`
- **User authority:** `Admin, Manager`
- **Authorization:** Bearer Token

**Example Request:**
  ```bash
request: POST 'http://localhost:8080/orders/{id}/templates'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `201 Created`
- **Body:**
  ```json
  {
      "id": 16,
      "product": "Product #1"
  }
  ```

### Create Customer Order from Template
**Endpoint:** `POST /orders/templates/{templateId}`

Creates a customer order from a specific template.

- **URL:** `http://localhost:8080/orders/templates/{templateId}`
- **Method:** `POST`
- **User authority:** `Admin, Manager`
- **Authorization:** Bearer Token

**Request Body:**
  ```json
  {
      "customer" : "Customer test",
      "status" : "In progress"
  }
  ```

**Example Request:**
  ```bash
request: 'http://localhost:8080/orders/templates/{templateId}' 
data: '{
      "customer" : "Customer test",
      "status" : "In progress"
  }'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `201 Created`
- **Body:**
  ```json
  {
      "customer": "Customer test",
      "product": "Product #4",
      "status": "In progress",
      "id": 33,
      "orderDate": "2024-06-03"
  }
  ```

### Update Customer Order
**Endpoint:** `PATCH /orders/{id}`

Updates a specific customer order.

- **URL:** `http://localhost:8080/orders/{id}`
- **Method:** `PATCH`
- **User authority:** `Admin, Manager`
- **Authorization:** Bearer Token

**Request Body:**
  ```json
  {
      "customer": "Customer order patch",
      "product": "Product patch",
      "status": "In progress"
  }
  ```

**Example Request:**
  ```bash
request: PATCH 'http://localhost:8080/orders/25' 
data: '{
      "customer": "Customer order patch",
      "product": "Product patch",
      "status": "In progress"
  }'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "customer": "Customer order patch",
      "product": "Product patch",
      "status": "In progress",
      "id": 25,
      "orderDate": "2024-06-03"
  }
  ```

### Delete Customer Order by ID
**Endpoint:** `DELETE /orders/{id}`

Deletes a specific customer order by its ID.

- **URL:** `http://localhost:8080/orders/{id}`
- **Method:** `DELETE`
- **User authority:** `Admin`
- **Authorization:** Bearer Token

**Example Request:**
  ```bash
request: DELETE 'http://localhost:8080/orders/36'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "customer": "Customer test",
      "product": "Product test",
      "status": "In progress",
      "id": 36,
      "orderDate": "2024-06-03"
  }
  ```

### Delete Customer Order Template by ID
**Endpoint:** `DELETE /orders/templates/{templateId}`

Deletes a specific customer order template by its ID.

- **URL:** `http://localhost:8080/orders/templates/{templateId}`
- **Method:** `DELETE`
- **User authority:** `Admin`
- **Authorization:** Bearer Token

**Example Request:**
  ```bash
request: DELETE 'http://localhost:8080/orders/templates/15'
header: 'Authorization: Bearer <token>'
  ```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "id": 15,
      "product": "Product #4"
  }
  ```

# Job controller's Documentation

This document provides an overview of the endpoints available in the Job controller. Each endpoint includes the necessary information for authorization, request structure, and example responses.

## Authorization

All endpoints within the Job controller require authentication via a Bearer Token.

## Get Jobs

**Endpoint:** `GET /jobs`

Retrieves all available jobs with optional filters. Available status retrieves jobs that are not blocked by other processes.

- **URL:** `http://localhost:8080/jobs`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** Bearer Token

**Optional filters**
- `status=available`, `status=blocked`
- **Request Body:**
  ```json
  {
      "type": "Manufacturing",
        "details": "Manufacturing parts #1 Product #1",
        "status": "Complete"
  }
  ```


**Example Request:**
```bash
request: 'http://localhost:8080/jobs' optional: '?status=' 
optional data: '{
    "type": "Manufacturing",
      "details": "Manufacturing parts #1 Product #1",
      "status": "Complete"
}'
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
```json
[
    {
        "type": "Manufacturing",
        "details": "Manufacturing parts #1 Product #1",
        "status": "Complete",
        "startDate": "2024-06-03",
        "updatedAt": "2024-06-03T09:54:51",
        "id": 52
    },
]
```

