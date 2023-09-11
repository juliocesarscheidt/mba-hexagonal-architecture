
```bash

docker-compose up -d mysql
docker-compose logs -f --tail 50 mysql


mvn compile -D skipTests=true

mvn package -D skipTests=true

mvn spring-boot:run


mvn test

```

## API REST

```bash
curl -X POST -H "Content-Type: application/json" -d '{"name": "john", "cpf": "001", "email": "abc@mail.com"}' --url "http://172.16.0.2:9050/customers" -i

curl -X GET -H "Content-Type: application/json" --url "http://172.16.0.2:9050/customers/1"


curl -X POST -H "Content-Type: application/json" -d '{"cnpj": "00000", "email": "abc@mail.com", "name": "john"}' --url "http://172.16.0.2:9050/partners" -i

curl -X GET -H "Content-Type: application/json" --url "http://172.16.0.2:9050/partners/1"


curl -X POST -H "Content-Type: application/json" -d '{"id": 1, "name": "event1", "date": "2023-09-10+11:00:00", "totalSpots": 100, "partner": {"id": 1, "cnpj": "00000", "email": "abc@mail.com", "name": "john"}}' --url "http://172.16.0.2:9050/events" -i
{"id":1,"date":"2023-09-10+11:00:00","name":"event1","totalSpots":100,"partnerId":1}


curl -X POST -H "Content-Type: application/json" -d '{"customerId": 1}' --url "http://172.16.0.2:9050/events/1/subscribe" -i
{"eventId":1,"ticketStatus":"PENDING","reservationData":"2023-09-10T17:05:43.666769800Z"


docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.customers'"
docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.partners'"
docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.events'"
docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.tickets'"

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
