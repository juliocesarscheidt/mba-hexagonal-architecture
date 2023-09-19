
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
# {"id":"42c65c78-bab4-4c67-af32-e94c3ac48d0d","cpf":"090.450.805-01","email":"julio@mail.com","name":"Julio Cesar"}

curl -X GET -H "Content-Type: application/json" --url "http://172.16.0.2:9050/customers/42c65c78-bab4-4c67-af32-e94c3ac48d0d"
# {"id":"42c65c78-bab4-4c67-af32-e94c3ac48d0d","cpf":"090.450.805-01","email":"julio@mail.com","name":"Julio Cesar"}


curl -X POST -H "Content-Type: application/json" -d '{"cnpj": "31.335.134/0001-97", "email": "julio@blackdevs.com", "name": "blackdevs tecnologia ltda"}' --url "http://172.16.0.2:9050/partners" -i
# {"id":"526f0d28-0fbc-410e-97f1-431e661390ea","cnpj":"31.335.134/0001-97","email":"julio@blackdevs.com","name":"blackdevs tecnologia ltda"}

curl -X GET -H "Content-Type: application/json" --url "http://172.16.0.2:9050/partners/526f0d28-0fbc-410e-97f1-431e661390ea"
# {"id":"526f0d28-0fbc-410e-97f1-431e661390ea","cnpj":"31.335.134/0001-97","email":"julio@blackdevs.com","name":"blackdevs tecnologia ltda"}


curl -X POST -H "Content-Type: application/json" -d '{"name": "event blackdevs", "date": "2023-09-10", "totalSpots": 100, "partnerId": "526f0d28-0fbc-410e-97f1-431e661390ea"}' --url "http://172.16.0.2:9050/events" -i
# {"id":"47645b3e-ae14-4270-8fe2-1a33ff882752","date":"2023-09-10","name":"event blackdevs","totalSpots":100,"partnerId":"526f0d28-0fbc-410e-97f1-431e661390ea"}

curl -X POST -H "Content-Type: application/json" -d '{"customerId": "42c65c78-bab4-4c67-af32-e94c3ac48d0d"}' --url "http://172.16.0.2:9050/events/47645b3e-ae14-4270-8fe2-1a33ff882752/subscribe" -i
# {"eventId":"47645b3e-ae14-4270-8fe2-1a33ff882752","ticketId":"55b2d63b-f2ac-4264-bb2a-04a828b997ab","ticketStatus":"PENDING","reservationDate":"2023-09-19T01:25:38.198267300Z"}



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
    
    createEvent(input: { name : "event blackdevs", date: "2023-09-10", totalSpots: 100, partnerId: "b346bac2-b3ee-4892-8c80-3a6bc666378a"}) {
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
