
```bash

docker-compose up -d mysql
docker-compose logs -f --tail 50 mysql


mvn compile -D skipTests=true

mvn package -D skipTests=true

mvn spring-boot:run

```
