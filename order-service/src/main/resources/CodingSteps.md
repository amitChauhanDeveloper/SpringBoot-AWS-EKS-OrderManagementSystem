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


docker exec -it springboot-aws-eks-ordermanagementsystem_kafka_1 /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order_placed --from-beginning

docker exec -it springboot-aws-eks-ordermanagementsystem_kafka_1 /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic order_placed --delete

docker exec -it springboot-aws-eks-ordermanagementsystem_kafka_1 /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list


