# Kafka CLI

Assume you're using zookeeper `localhost:2181/kafka` for Kafka, by default it is `localhost:2181`.

* List topics

```bash
bin/kafka-topics.sh --list --zookeeper localhost:2181/kafka

```

* Describe topic
```bash
bin/kafka-topics.sh --describe --topic my-topic --zookeeper localhost:2181/kafka

```

* Check topic offsets

```bash
bin/kafka-run-class.sh kafka.tools.GetOffsetShell --broker-list localhost:9092 --topic my-topic
```

* Check consumer group offsets

```bash
bin/kafka-run-class.sh kafka.admin.ConsumerGroupCommand --bootstrap-server localhost:9092 --group my-service --describe
```

* Add more partitions

```bash
bin/kafka-topics.sh --alter --topic my-topic --zookeeper localhost:2181/kafka --partitions 2
```
