# GraphQL and gRPC POC

This is a POC for GraphQL and gRPC. As i have showen in this project we can simply use the same service layer and expose both interfaces using seperate controllers (ProductController.java && ProductGrpcController.java). Even the streams can be exposed in the same manner. GrphQl is easier to setup, more flexible, and much better for exposing out to customers while gRPC is much better for internal communication. 

## Requirements
1. Open JDK 23 (change the setup in `settings.json`)

## Build
```bash
./mvnw clean install
```
**This might through some errors regarding removing temp files or a ceratin dir in such cases use the following. It might take couple of tries. This is a known issue when working with gRPC with mvn. This can be avoided by using gradle.**
```bash
rm -rf target && ./mvnw clean install
```

## Running
```bash
./mvnw spring-boot:run
```

## GraphQL
1. uri: http://localhost:8080/graphql
2. for testing use the graphiql interface at http://localhost:8080/graphiql

### Example GraphQl Queries
1. Select all Products
```graphql
query Products {
    products {
        id
        name
        description
        price
        createdAt
        updatedAt
    }
}
```

2. Select a single product
```graphql
query Product {
    product(id: 2) {
        id
        name
        description
        price
        createdAt
        updatedAt
    }
}
```

3. Create a product
```graphql
mutation CreateProduct {
    createProduct(input: { name: "test", description: "test", price: 12.12 }) {
        id
        name
        description
        price
        createdAt
        updatedAt
    }
}
```

### Using postman is also easy for graphql

## gRPC
1. uri: localhost:9090
2. test using postman

## Scripts for the postgres tables
**I am using `production` as the schema. Change it accordingly in the models as well.**
```sql
CREATE TABLE production.products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE production.users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    product_id INTEGER,  -- Using INTEGER since it references another table
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```