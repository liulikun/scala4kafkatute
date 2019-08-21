import java.time.Duration
import java.util
import java.util.Properties
import java.util.concurrent.Future

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}

object Main {
  def main(args: Array[String]): Unit = {
    val producerProps = new Properties()
    producerProps.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "kafka.docker:9092")
    producerProps.put(ProducerConfig.ACKS_CONFIG, "all")
    producerProps.put(ProducerConfig.RETRIES_CONFIG, "3")
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](producerProps)

    val testTopic = "my-topic"

    (1 to 100).foreach { i =>
      val result: Future[RecordMetadata] = producer.send(new ProducerRecord[String, String](testTopic, s"key$i", s"value$i"))
      val metadata = result.get()
      println(s"$metadata")
    }

    producer.close()

    val consumerProps = new Properties()
    consumerProps.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "kafka.docker:9092")
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-service")
    consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")

    val consumer = new KafkaConsumer[String, String](consumerProps)
    consumer.subscribe(util.Arrays.asList(testTopic))
    while (true) {
      val records = consumer.poll(Duration.ofSeconds(10))
      records.forEach { record =>
        println(s"offset = ${record.offset}, key = ${record.key}, value = ${record.value}")
      }
      consumer.commitSync()
    }
  }
}
