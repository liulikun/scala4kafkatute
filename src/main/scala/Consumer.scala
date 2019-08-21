import java.time.Duration
import java.util
import java.util.Properties

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

object Consumer {
  def main(args: Array[String]): Unit = {
    val consumerProps = new Properties()
    consumerProps.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "kafka.docker:9092")
    consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "my-service")
    consumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, "my-client")
    consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
    consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")

    val consumer = new KafkaConsumer[String, String](consumerProps)
    consumer.subscribe(util.Arrays.asList(Producer.testTopic))
    while (true) {
      val records = consumer.poll(Duration.ofSeconds(10))
      records.forEach { record =>
        println(s"offset = ${record.offset}, key = ${record.key}, value = ${record.value}")
      }
      consumer.commitSync()
    }
  }
}
