
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
curl -X POST -H "Content-Type: application/json" -d '{"name": "Julio Cesar", "cpf": "090.450.805-01", "email": "julio@mail.com"}' --url "http://172.16.0.2:9050/customers" -i
# {"id":"62fd4f34-da53-4e37-919f-741e1f55e188","cpf":"090.450.805-01","email":"julio@mail.com","name":"Julio Cesar"}

curl -X GET -H "Content-Type: application/json" --url "http://172.16.0.2:9050/customers/62fd4f34-da53-4e37-919f-741e1f55e188"
# {"id":"62fd4f34-da53-4e37-919f-741e1f55e188","cpf":"090.450.805-01","email":"julio@mail.com","name":"Julio Cesar"}


curl -X POST -H "Content-Type: application/json" -d '{"cnpj": "31.335.134/0001-97", "email": "julio@blackdevs.com", "name": "blackdevs tecnologia ltda"}' --url "http://172.16.0.2:9050/partners" -i
# {"id":"12e53677-2d32-4eba-8852-486cfc11d9a6","cnpj":"31.335.134/0001-97","email":"julio@blackdevs.com","name":"blackdevs tecnologia ltda"}

curl -X GET -H "Content-Type: application/json" --url "http://172.16.0.2:9050/partners/12e53677-2d32-4eba-8852-486cfc11d9a6"
# {"id":"12e53677-2d32-4eba-8852-486cfc11d9a6","cnpj":"31.335.134/0001-97","email":"julio@blackdevs.com","name":"blackdevs tecnologia ltda"}


curl -X POST -H "Content-Type: application/json" -d '{"name": "event blackdevs", "date": "2023-09-10T00:00:00", "totalSpots": 100, "partnerId": "12e53677-2d32-4eba-8852-486cfc11d9a6"}' --url "http://172.16.0.2:9050/events" -i
# {"id":"924e02f9-d89d-4cab-ab15-3ac9fccd1241","date":"2023-09-10T00:00:00","name":"event blackdevs","totalSpots":100,"partnerId":"12e53677-2d32-4eba-8852-486cfc11d9a6"}

curl -X POST -H "Content-Type: application/json" -d '{"customerId": "62fd4f34-da53-4e37-919f-741e1f55e188"}' --url "http://172.16.0.2:9050/events/924e02f9-d89d-4cab-ab15-3ac9fccd1241/subscribe" -i
# {"eventId":"924e02f9-d89d-4cab-ab15-3ac9fccd1241","ticketId":"62ab5dcb-7d3d-4177-924b-660590b50ed5","ticketStatus":"PENDING","reservationDate":"2023-09-19T01:46:17.521061300Z"}



docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.customers'"
docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.partners'"
docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.events'"
docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.tickets'"
docker-compose exec mysql sh -c "mysql -uroot -padmin -e 'select * from events.events_tickets'"

```


## Graphql

```bash
# http://172.16.0.2:9050/graphiql?path=/graphql

  mutation {
    createCustomer(input: { name: "Julio Cesar", cpf: "090.450.805-01", email: "julio@mail.com" }) {
      id
      name
      email
    }
    
    createPartner(input: { name: "blackdevs tecnologia ltda", cnpj: "31.335.134/0001-97", email: "julio@blackdevs.com" }) {
      id
      name
      email
    }
    
    createEvent(input: { name : "event blackdevs", date: "2023-09-10T00:00:00", totalSpots: 100, partnerId: "b346bac2-b3ee-4892-8c80-3a6bc666378a"}) {
      id
      date
      name
      totalSpots
      partnerId
    }

    subscribeCustomerToTicket(input: { customerId : "5e6fb2f9-eded-4216-afab-413a3deec9b9", eventId: "b5d1b754-cb56-4e3c-a743-a506e34a1112"}) {
      eventId
      ticketId
      ticketStatus
      reservationDate
    }
  }

query {
  customerOfId(id: "5e6fb2f9-eded-4216-afab-413a3deec9b9") {
    id
    name
    email
  }
  
  partnerOfId(id: "b346bac2-b3ee-4892-8c80-3a6bc666378a") {
    id
    name
    email
  }
}

```
