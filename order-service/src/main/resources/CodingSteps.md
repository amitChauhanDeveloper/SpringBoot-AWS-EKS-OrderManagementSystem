1. Create a controller - 200 OK
2. Create the DTOs - OrderDto , Order ItemDto
3. Create the entity
4. Write the repository class
5. Write the service logic
6. Kafka producer class
7. Datasource Config


PS C:\Users\Varsha> docker run -d -p 3306:3306 --name mysql-container-new -e MYSQL_ROOT_PASSWORD=varsharoot mysql:latest

PS C:\Users\Varsha> docker exec -it mysql-container-new mysql -u root -p

docker exec -it kafka kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order_placed --from-beginning

#amit
zookeeper-server-start.sh config/zookeeper.properties
kafka-server-start.sh config/server.properties
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order_placed --from-beginning
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order_cancelled --from-beginning
kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic order_cancelled
