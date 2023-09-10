
```bash

docker-compose up -d mysql
docker-compose logs -f --tail 50 mysql


mvn compile -D skipTests=true

mvn package -D skipTests=true

mvn spring-boot:run

```

## API REST

```bash
curl -X POST -H "Content-Type: application/json" -d '{"name": "john", "cpf": "001", "email": "abc@mail.com"}' --url "http://172.16.0.2:9050/customers" -i

curl -X GET -H "Content-Type: application/json" --url "http://172.16.0.2:9050/customers/1"


curl -X POST -H "Content-Type: application/json" -d '{"cnpj": "00000", "email": "abc@mail.com", "name": "john"}' --url "http://172.16.0.2:9050/partners" -i

curl -X GET -H "Content-Type: application/json" --url "http://172.16.0.2:9050/partners/1"


docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.customers'"
docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.partners'"

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
    
    createPartner(input: { name: "john", cnpj: "00000", email: "abc@mail.com" }) {
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
  
  partnerOfId(id: "1") {
    id
    name
    email
  }
}

```
