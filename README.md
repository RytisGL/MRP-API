# Material Requirements Planning (MRP) API Documentation

## Overview

The Material Requirements Planning (MRP) API provides a comprehensive solution for managing materials, jobs, and orders within a manufacturing or production environment. This API supports user creation and authorization, order and job creation, job dependencies, stock management, and detailed change records for jobs and requisitions.

## Features

### [User Management](#user-management)
- **User Creation**: Create new users to access the MRP system.
- **User Authorization**: Manage user roles and permissions to ensure secure access to API functionalities.

### [Order Management](#order-management)
- **Order Creation**: Create and manage orders to streamline your production process.
- **Job Creation**: Assign jobs to orders and manage their lifecycle.

### [Job Management](#job-management)
- **Job Dependencies**: Structure jobs that block each other to provide a clear workflow. Define the sequence in which jobs need to be completed to avoid bottlenecks and ensure smooth operations.

### [Stock Management](#stock-management)
- **Requisitions**: Assign requisitions to jobs, manage their lifecycle, and ensure that all necessary materials are available when needed.
- **Stock Management**: Handle basic stock functionalities including tracking inventory levels and managing stock movements.
- **Purchase Orders**: Create and manage purchase orders for acquiring additional stock.

### [Change Records](#change-records)
- **Job Change Records**: Keep detailed records of changes made to jobs, providing a clear audit trail.
- **Requisition Change Records**: Maintain records of changes made to requisitions to ensure traceability and accountability.

## User Management

This documentation provides an overview of the User Management controller, including endpoints for user registration, login, retrieval, updating, and deletion.

## Authorization

All endpoints except login and register require a Bearer Token for authorization. Login and register enpoints requires user to be annonymous.

### Register User
**Endpoint:** `POST /users/register`

Registers a new user.

- **URL:** `http://localhost:8080/users/register`
- **Method:** `POST`
- **User authority:** `Anonymous`

**Validations**
- **firstname:** `Not blank`, `Size 2-50`
- **lastname:** `Not blank`, `Size 5-50`
- **email:** `Not blank`, `Size 5-50`, `Email`, `Unique`
- **lastname:** `Not blank`, `Size 8-20`,`Containts Uppercase letter`,`Containts Number`


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

**Validations**

- **email:** `Not blank`, `Size 5-50`
- **lastname:** `Not blank`, `Size 8-20`

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
- **Authorization:** `Bearer Token`

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
- **Authorization:** `Bearer Token`
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
- **Authorization:** `Bearer Token`

**Query Params:**
- `role=admin`

**Validations**

- **available roles:** `ADMIN`, `MANAGER`, `USER`

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
- **Authorization:** `Bearer Token`

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
# Order Managment

This document provides an overview of the endpoints available in the Orders controller. Each endpoint includes the necessary information for authorization, request structure, and example responses.

## Authorization

All endpoints require a Bearer Token for authorization.

### Get Customer Orders
**Endpoint:** `GET /orders`

Retrieves customer orders.

- **URL:** `http://localhost:8080/orders`
- **Method:** `GET`
- **User authority:** `Admin, Manager`
- **Authorization:** `Bearer Token`

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
- **Authorization:** `Bearer Token`

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
- **Authorization:** `Bearer Token`

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
- **Authorization:** `Bearer Token`

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
- **Authorization:** `Bearer Token`

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
- **Authorization:** `Bearer Token`


**Validations**
- **customer:** `Not blank`, `Size 5-50`
- **product:** `Not blank`, `Size 5-50`
- **status:** `Not blank`, `Size 5-50`


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
- **Authorization:** `Bearer Token`

**Validations**
- **type:** `Not blank`, `Size 5-50`
- **details:** `Not blank`, `Size 5-50`
- **status:** `Not blank`, `Size 5-50`

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
- **Authorization:** `Bearer Token`

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
- **Authorization:** `Bearer Token`

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
- **Authorization:** `Bearer Token`

**Validations**
- **customer:** `Not blank`, `Size 5-50`
- **product:** `Not blank`, `Size 5-50`
- **status:** `Not blank`, `Size 5-50`

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
- **Authorization:** `Bearer Token`

**Validations**
- **id entity can't have:** `associated jobs`


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
- **Authorization:** `Bearer Token`

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

# Job Managment

This document provides an overview of the endpoints available in the Job controller. Each endpoint includes the necessary information for authorization, request structure, and example responses.

## Authorization

All endpoints within the Job controller require authentication via a Bearer Token.

## Get Jobs

**Endpoint:** `GET /jobs`

Retrieves all available jobs with optional filters. Available status retrieves jobs that are not blocked by other processes.

- **URL:** `http://localhost:8080/jobs`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Optional filters**
- `status=available`, `status=blocked`

**Request Body:**
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
### Get Job by ID
**Endpoint:** `GET /jobs/{id}`

Retrieves a job by its ID.

- **URL:** `http://localhost:8080/jobs/{id}`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Example Request:**
```bash
request: 'http://localhost:8080/jobs/56' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "type": "Manufacturing",
      "details": "Manufacturing parts #3 Product #2",
      "status": "In progress",
      "startDate": "2024-06-03",
      "updatedAt": "2024-06-03T09:14:21",
      "id": 56
  }
  ```

### Get Job Blockers
**Endpoint:** `GET /jobs/{id}/blockers`

Retrieves blockers for a job.

- **URL:** `http://localhost:8080/jobs/{id}/blockers`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Example Request:**
```bash
request: 'http://localhost:8080/jobs/{id}/blockers' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  [
      {
          "type": "Painting",
          "details": "Painting product #4",
          "status": "In progress",
          "startDate": "2024-06-03",
          "updatedAt": "2024-06-03T05:54:29",
          "id": 95
      }
  ]
  ```

### Get Job Records by Job ID
**Endpoint:** `GET /jobs/{id}/records`

Retrieves records for a specific job.

- **URL:** `http://localhost:8080/jobs/{id}/records`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Example Request:**
```bash
request: 'http://localhost:8080/jobs/52/records' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  [
      {
          "status": "Complete",
          "id": 1,
          "userEmail": "nicole@example.com",
          "createdAt": "2024-06-03T10:54:51.02578"
      }
  ]
  ```

### Get Requisition Records by Job ID
**Endpoint:** `GET /jobs/{id}/requisitions/records`

Retrieves requisition records for a job.

- **URL:** `http://localhost:8080/jobs/{id}/requisitions/records`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Example Request:**
```bash
request: 'http://localhost:8080/jobs/{id}/requisitions/records' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  [
      {
          "quantity": 200,
          "status": "Complete",
          "user": {
              "id": 202,
              "email": "nicole@example.com",
              "role": "ADMIN"
          },
          "id": 5,
          "dateCreated": "2024-06-03T21:25:07"
      },
      {
          "quantity": 200,
          "status": "Complete",
          "user": {
              "id": 202,
              "email": "nicole@example.com",
              "role": "ADMIN"
          },
          "id": 6,
          "dateCreated": "2024-06-03T21:29:36"
      }
  ]
  ```

### Create Requisition by Job and Stock ID
**Endpoint:** `POST /{jobId}/requisitions/{stockId}`

Creates a new requisition for a job and stock ID.

- **URL:** `http://localhost:8080/jobs/{jobId}/requisitions/{stockId}`
- **Method:** `POST`
- **User authority:** `Admin, Manager`
- **Authorization:** `Bearer Token`


**Validations**

- **quantity:** `Not null`, `Max 999999999`, `Min 1`
- **status:** `Not blank`, `Size 5-50`

**Body:**
  ```json
  {
      "quantity" : 200,
      "status" : "In progress"
  }
  ```

**Example Request:**
```bash
request: POST 'http://localhost:8080/jobs/{jobId}/requisitions/{stockId}' 
data: '{
    "quantity" : 200,
    "status" : "In progress"
}'
header: 'Authorization: Bearer <token>' 
```

**Success Response:**
- **Status Code:** `201 CREATED`
- **Body:**
  ```json
  [
      {
          "quantity": 200,
          "status": "In progress",
          "id": 52,
          "createdAt": "2024-06-03T18:22:59"
      }
  ]
  ```

### Create Job Blockers by Job ID
**Endpoint:** `POST /jobs/{id}/blockers?ids=`

Creates new blockers of existing jobs for a job. Returns a list of blockers.

- **URL:** `http://localhost:8080/jobs/{id}/blockers?ids=`
- **Method:** `POST`
- **Authorization:** `Bearer Token`
- **User authority:** `Admin, Manager`
- **Query Params:** `ids`

**Validations**

- **ids can't have:** `Duplicates`, `Id of entity they are blocking`, `Non existant ids`

**Example Request:**
```bash
request: POST 'http://localhost:8080/jobs/{id}/blockers?ids=' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `201 CREATED`
- **Body:**
  ```json
  [
      {
          "type": "Test type",
          "details": "Test details",
          "status": "In progress",
          "startDate": "2024-06-03",
          "updatedAt": "2024-06-03T18:28:10",
          "id": 96
      }
  ]
  ```

### Update Job Status by ID
**Endpoint:** `PATCH /jobs/{id}`

Updates the status of a job by its ID, status change to complete removes existing job blockers, any status change produces job record.

- **URL:** `http://localhost:8080/jobs/{id}`
- **Method:** `PATCH`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Validations**

- **status:** `Not blank`, `Size 5-50`
- **id entity if status is changed to complete, can't have:** `Associated job blockers that are not complete`, `Associated requisitions that are not complete`

**Body:**
  ```json
  {
      "status" : "Complete"
  }
  ```

**Example Request:**
```bash
request: PATCH 'http://localhost:8080/jobs/98'  
data: '{
    "status" : "Complete"

}'
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "type": "Type test",
      "details": "Details test",
      "status": "Complete",
      "startDate": "2024-06-07",
      "updatedAt": "2024-06-07T02:10:50",
      "id": 98
  }
  ```

### Delete Job by ID
**Endpoint:** `DELETE /jobs/{id}`

Deletes a job by its ID.

- **URL:** `http://localhost:8080/jobs/{id}`
- **Method:** `DELETE`
- **User authority:** `Admin, Manager`
- **Authorization:** `Bearer Token`

**Validations**

- **id entity can't have:** `Associated requisitions`, `Associated job records`

**Example Request:**
```bash
request: DELETE 'http://localhost:8080/jobs/100' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "type": "Type test",
      "details": "Details test",
      "status": "In progress",
      "startDate": "2024-06-03",
      "updatedAt": "2024-06-03T07:03:52",
      "id": 100
  }
  ```

# Stock Managment

This document provides an overview of the endpoints available in the Stock controller. Each endpoint includes the necessary information for authorization, request structure, and example responses.

## Authorization

All endpoints within the Stock controller require authentication via a Bearer Token.

### Get Stock
**Endpoint:** `GET /stock`

Retrieves a list of all stock items.

- **URL:** `http://localhost:8080/stock`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`
 
**Optional filters**
- **Request Body:**
  ```json
  {
      "name": "Stock #1",
        "quantity": 900,
        "unitOfMeasurement": "unit"
  }
  ```

**Example Request:**
```bash
request: GET 'http://localhost:8080/stock' 
optional data: '{
        "name": "Stock #1",
        "quantity": 900,
        "unitOfMeasurement": "unit"
}'
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  [
      {
          "name": "Stock #1",
          "quantity": 900,
          "unitOfMeasurement": "unit",
          "id": 4
      },
      {
          "name": "Stock #2",
          "quantity": 2300,
          "unitOfMeasurement": "unit",
          "id": 5
      },
      {
          "name": "Stock #3",
          "quantity": 50,
          "unitOfMeasurement": "unit",
          "id": 6
      }
  ]
  ```

### Get Stock by ID
**Endpoint:** `GET /stock/{id}`

Retrieves a specific stock item by its ID.

- **URL:** `http://localhost:8080/stock/{id}`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Example Request:**
```bash
request: GET 'http://localhost:8080/stock/11' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "name": "Name test",
      "quantity": 200,
      "unitOfMeasurement": "unit",
      "id": 11
  }
  ```

### Get Requisitions

**Endpoint:** `GET /stock/requisitions`

Fetches requisitions, can be filtered by their status.

- **URL:** `http://localhost:8080/stock/requisitions`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Optional filters**
- **Query Params:** `status`

**Example Request:**

```bash
request: 'http://localhost:8080/stock/requisitions' optional: '?status=' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**

- **Status Code:** `200 OK`
- **Body:**

  ```json
  [
      {
          "quantity": 120,
          "status": "In progress",
          "id": 29,
          "createdAt": "2024-06-03T09:40:15"
      },
      {
          "quantity": 50,
          "status": "In progress",
          "id": 30,
          "createdAt": "2024-06-03T09:40:30"
      },
     
  ]
  ```

### Get Stock Records by Stock ID

**Endpoint:** `GET /stock/{id}/records`

Fetches stock records by stock id.

- **URL:** `http://localhost:8080/stock/{id}/records`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Example Request:**

```bash
request: 'http://localhost:8080/stock/{id}/records' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**

- **Status Code:** `200 OK`
- **Body:**

  ```json
  [
      {
          "quantity": 200,
          "status": "Complete",
          "user": {
              "id": 202,
              "email": "nicole@example.com",
              "role": "ADMIN"
          },
          "id": 6,
          "dateCreated": "2024-06-03T21:29:36"
      }
  ]
  ```


### Get Purchase Orders by Stock ID

**Endpoint:** `GET /stock/{id}/porders`

Fetches purchase orders by a specific stock ID.

- **URL:** `http://localhost:8080/stock/{id}/porders`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Example Request:**

```bash
request: 'http://localhost:8080/stock/{id}/porders' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**

- **Status Code:** `200 OK`
- **Body:**

  ```json
  [
      {
          "quantity": 2000,
          "deliveryDate": "2025-01-01",
          "status": "In progress",
          "id": 1
      }
  ]
  ```

### Get Purchase Orders
**Endpoint:** `GET /stock/porders`

Retrieves a list of purchase orders.

- **URL:** `http://localhost:8080/stock/porders`
- **Method:** `GET`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Example Request:**
```bash
request: 'http://localhost:8080/stock/porders' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  [
      {
          "quantity": 2000,
          "deliveryDate": "2025-01-01",
          "status": "In progress",
          "id": 1
      }
  ]
  ```

### Complete Requisition
**Endpoint:** `PUT /stock/requisitions/{id}`

Completes requisition, removes if requisition has enough associated stock to complete or marks it out of stock, while producing inventory usage record.

- **URL:** `http://localhost:8080/stock/requisitions/{id}`
- **Method:** `PUT`
- **User authority:** `Admin, Manager, User`
- **Authorization:** `Bearer Token`

**Example Request:**
```bash
request: PUT 'http://localhost:8080/stock/requisitions/50' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "quantity": 50,
      "status": "Complete"
  }
  ```

### Create Purchase Order by Stock ID
**Endpoint:** `POST /stock/{id}/porders`

Creates a new purchase order for a specific stock item.

- **URL:** `http://localhost:8080/stock/{id}/porders`
- **Method:** `POST`
- **User authority:** `Admin, Manager`
- **Authorization:** `Bearer Token`


**Validations**
- **quatity:** `Max - 999999999`, `Min - 1`
- **deliveryDate:** `Future date`
- **status:** `Not blank`, `Size 5-50`

**Body:**
  ```json
  {
      "quantity": 200,
      "deliveryDate": "2025-01-01",
      "status": "In progress"
  }
  ```

**Example Request:**
```bash
request: POST 'http://localhost:8080/stock/{id}/porders' 
data: '{
    "quantity": 200,
    "deliveryDate": "2025-01-01",
    "status": "In progress"
}'
header: 'Authorization: Bearer <token>' 
```

**Success Response:**
- **Status Code:** `201 CREATED`
- **Body:**
  ```json
  {
      "quantity": 200,
      "deliveryDate": "2025-01-01",
      "status": "In progress",
      "id": 2
  }
  ```

### Update Stock from Purchase Order
**Endpoint:** `PATCH /stock/porders/{id}`

Updates the stock information based on a purchase order.

- **URL:** `http://localhost:8080/stock/porders/{id}`
- **Method:** `PATCH`
- **User authority:** `Admin, Manager`
- **Authorization:** `Bearer Token`

**Example Request:**
```bash
request: PATCH 'http://localhost:8080/stock/porders/{id}' 
data: '{
    "name": "Name test",
    "quantity": 400,
    "unitOfMeasurement": "unit"
}'
header: 'Authorization: Bearer <token>' 
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "name": "Name test",
      "quantity": 400,
      "unitOfMeasurement": "unit",
      "id": 11
  }
  ```

### Create Stock
**Endpoint:** `POST /stock`

Creates a new stock item.

- **URL:** `http://localhost:8080/stock`
- **Method:** `POST`
- **User authority:** `Admin, Manager`
- **Authorization:** `Bearer Token`

**Validations**
- **name:** `Not blank`, `Size 5-50`
- **quantity:** `Max - 999999999`, `Min - 1`
- **unitOfmeasurement:** `Not blank`, `Size 2-50`

**Body:**
  ```json
  {
      "name": "Name test",
      "quantity": 200,
      "unitOfMeasurement": "unit"
  }
  ```

**Example Request:**
```bash
request: POST 'http://localhost:8080/stock' 
data: '{
    "name": "Name test",
    "quantity": 200,
    "unitOfMeasurement": "unit"
}'
header: 'Authorization: Bearer <token>' 
```

**Success Response:**
- **Status Code:** `201 CREATED`
- **Body:**
  ```json
  {
      "name": "Name test",
      "quantity": 200,
      "unitOfMeasurement": "unit",
      "id": 11
  }
  ```
### Delete Stock by ID
**Endpoint:** `DELETE /stock/{id}`

Deletes a stock entry by its ID.

- **URL:** `http://localhost:8080/stock/{id}`
- **Method:** `DELETE`
- **User authority:** `Admin`
- **Authorization:** `Bearer Token`

**Validations**
- **id entity can't have:** `Associated purchase orders`, `Associated purchase requisitions`, `Associated inventory records`


**Example Request:**
```bash
request: DELETE 'http://localhost:8080/stock/12' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "name": "Name test",
      "quantity": 200,
      "unitOfMeasurement": "unit",
      "id": 12
  }
  ```

### Delete Purchase Order by ID
**Endpoint:** `DELETE /stock/porder/{id}`

Deletes a purchase order by its ID.

- **URL:** `http://localhost:8080/stock/porder/{id}`
- **Method:** `DELETE`
- **User authority:** `Admin`
- **Authorization:** `Bearer Token`

**Example Request:**
```bash
request: DELETE 'http://localhost:8080/stock/porder/5' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "quantity": 200,
      "deliveryDate": "2025-01-01",
      "status": "Complete",
      "id": 5
  }
  ```

### Delete Requisition by ID
**Endpoint:** `DELETE /stock/requisitions/{id}`

Deletes a requisition by its ID.

- **URL:** `http://localhost:8080/stock/requisitions/{id}`
- **Method:** `DELETE`
- **User authority:** `Admin`
- **Authorization:** `Bearer Token`

**Example Request:**
```bash
request: DELETE 'http://localhost:8080/stock/requisitions/48' 
header: 'Authorization: Bearer <token>'
```

**Success Response:**
- **Status Code:** `200 OK`
- **Body:**
  ```json
  {
      "quantity": 200,
      "status": "In progress",
      "id": 48,
      "createdAt": "2024-06-04T20:59:14"
  }
  ```

## Change records

Changes to job status and requisitions are recorded. Job status change is recorded in job record and requisition changes are recorded in inventory usage record.
  
## Conclusion

The MRP API provides a robust set of tools for managing users, orders, jobs, stock, and change records, ensuring efficient and traceable operations within your production environment. By leveraging the API's functionalities, you can streamline your material requirements planning and maintain accurate records of all processes.
