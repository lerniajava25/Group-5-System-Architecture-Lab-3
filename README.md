# Group 5 - System Architecture (Ghudsan, Osama, Waqar)

This repository contains the implementation of a RESTful Web Service built with Jakarta EE 11, JAX-RS, CDI, and Bean Validation running on a WildFly application server. The application features a thread-safe, in-memory architecture controlled by a `ReentrantLock`.

## Prerequisites
- Java 21 or higher
- IntelliJ IDEA (Ultimate Edition recommended)
- WildFly Application Server (configured inside IntelliJ or run externally)

## Project Packaging & Deployment

Before running the server, clean and package the application into a `.war` file using the integrated Maven wrapper:

```bash
.\mvnw clean package
```

To manually deploy the artifact to WildFly, copy the generated archive from the `target/` directory to your server's deployment path (replace `<PATH_TO_WILDFLY>` with the actual installation directory on your computer):

```bash
Copy-Item .\target\systemArchitectureGroup5Assignment3-1.0-SNAPSHOT.war -Destination "<PATH_TO_WILDFLY>\standalone\deployments\api.war"
```
Or
Automatic Deployment via IntelliJ Configurations

Once deployed, the root endpoint will be active at: `http://localhost:8080/api/pets`

---

## Pet Management REST API Testing Guide

This guide details how to verify and interact with the 6 REST API endpoints exposed by the JAX-RS resource layer.

Execute the following curl commands in your terminal or Git Bash inside IntelliJ to test the complete lifecycle of the pet service layer.

### 1. Adopt a New Pet (POST)
Submits a new pet payload to the application context. The internal service assigns a unique key sequence in background routines while validating incoming parameters.
```bash
curl -X POST http://localhost:8080/api/pets -H "Content-Type: application/json" -d "{\"name\": \"Charlie\", \"species\": \"Dog\", \"hungerLevel\": 50, \"happiness\": 50}"
```

### 2. List Pets with Pagination (GET)
Retrieves a page of pets. `offset` is the number of pets to skip and `limit` is the maximum number of pets to return. Both parameters are optional.
```bash
curl -X GET "http://localhost:8080/api/pets?offset=0&limit=10"
```

### 3. View Specific Pet Status (GET with ID)
Queries the properties of an individual pet directly by parsing the targeted map sequence index path parameter.
```bash
curl -X GET http://localhost:8080/api/pets/1
```

### 4. Feed the Pet (PUT)
Invokes atomic service routines protected by locking patterns to decrease the pet's current hunger configuration value by 10 points.
```bash
curl -X PUT http://localhost:8080/api/pets/1/feed
```

### 5. Play with the Pet (PUT)
Invokes atomic service routines protected by locking patterns to increase the pet's current happiness configuration value by 10 points.
```bash
curl -X PUT http://localhost:8080/api/pets/1/play
```

### 6. Release/Delete the Pet (DELETE)
Removes the data structure entry key mapping entirely from the in-memory persistence simulation array.
```bash
curl -X DELETE http://localhost:8080/api/pets/1
```

## Validations (ValidationException, NotFoundException)

### ValidationException
Post only if Hunger level is under 100 else show error message "Hunger level cannot be above 100".
```bash
curl -X POST http://localhost:8080/api/pets -H "Content-Type: application/json" -d "{\"name\": \"\", \"species\": \"Dog\", \"hungerLevel\": 150, \"happiness\": 80}"
```
#### Error:
```bash
{"error":"Bad Request","Status":"400","message":"Validation failed","errors":{"hungerLevel":"Hunger level cannot be above 100","name":"A pet name is required"}}
```


### NotFoundException
Get pets by ID and show error message if ID doesn't exist.
```bash
curl -X GET http://localhost:8080/api/pets/999
```
#### Error:
```bash
{"error":"Not Found","message":"Pet with ID 999 not found"}
```


## Bonus Features

### Sorting
Query by allowed values: name, species, hungerLevel, happiness. Else show error.
```bash
curl -X GET "http://localhost:8080/api/pets?sortBy=secretField&order=desc"
```
#### Error:
```bash
{"error":"Bad Request","Status":"400","message":"Validation failed","errors":{"arg0":"Invalid sort field. Allowed values are: name, species, hungerLevel, happiness"}}
```
---

## High-Grade Bonus Feature: Dynamic Species Filtering

The application includes an advanced server-side filtering implementation designed for higher grades. It leverages the `UriInfo` context mapping layer to dynamically parse queries directly from the URL path.

To verify this feature, ensure you have populated the memory storage with distinct animal types (e.g., both "cat" and "dog" entries). You can validate the runtime behavior using either a standard Web Browser or curl terminal routines.

### Option A: Test Directly in the Web Browser (Recommended)
Open your preferred web browser and enter the following URLs into the address bar to view the dynamically filtered datasets:
- To list only dogs: `http://localhost:8080/api/pets?species=dog`
- To list only cats: `http://localhost:8080/api/pets?species=cat`

### Option B: Test via Terminal (curl Commands)

#### Test 1: Fetch Only Dogs
Execute a query to retrieve data elements matching the target identifier sequence. The backend engine isolates the entries and excludes non-matching nodes automatically.
```bash
curl -X GET "http://localhost:8080/api/pets?species=dog"
```

#### Test 2: Case-Insensitive Matching
The string operations are bound using case-insensitive validation lookups. Passing alternative uppercase representations will still map and execute successfully.
```bash
curl -X GET "http://localhost:8080/api/pets?species=cat"

```
