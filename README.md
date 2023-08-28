
```bash

docker-compose up -d mysql
docker-compose logs -f --tail 50 mysql


mvn compile -D skipTests=true

mvn package -D skipTests=true

mvn spring-boot:run

```

## Graphql

```bash
# http://localhost:9050/graphiql?path=/graphql

mutation {
  createCustomer(input: { name: "john", cpf: "000", email: "abc@mail.com" }) {
    id
    name
    email
  }
}

query {
  customerOfId(id: "1") {
    id
    name
    email
  }
}

```
