1. Create a controller - 200 OK
2. Create the DTOs - OrderDto , Order ItemDto
3. Create the entity
4. Write the repository class
5. Write the service logic
6. Kafka producer class

#amit
zookeeper-server-start.sh config/zookeeper.properties
kafka-server-start.sh config/server.properties

#short command configuration
nano .bashrc
# Function to consume messages from a given topic
kafka-consume() {
    docker exec -it springboot-aws-eks-ordermanagementsystem_kafka_1 /opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic "$1" --from-beginning
}

# Function to delete a given topic
kafka-delete() {
    docker exec -it springboot-aws-eks-ordermanagementsystem_kafka_1 /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --topic "$1" --delete
}

# Function to list all topics
kafka-list() {
    docker exec -it springboot-aws-eks-ordermanagementsystem_kafka_1 /opt/bitnami/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
}

short command
kafka-consume topicname
kafka-delete topicname
kafka-list

check service log 
docker-compose logs -f orderservice
docker-compose logs -f productservice



