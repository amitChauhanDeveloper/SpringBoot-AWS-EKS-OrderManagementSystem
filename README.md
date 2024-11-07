# SpringBoot-AWS-EKS-OrderManagementSystem

zookeeper-server-start.sh config/zookeeper.properties
kafka-server-start.sh config/server.properties
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order_placed --from-beginning
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic order_cancelled --from-beginning
kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic order_cancelled
