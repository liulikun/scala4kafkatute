import java.util.Properties

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer._

object Producer {
  val testTopic = "my-topic"

  def main(args: Array[String]): Unit = {
    val producerProps = new Properties()
    producerProps.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "kafka.docker:9092")
    producerProps.put(ProducerConfig.ACKS_CONFIG, "all")
    producerProps.put(ProducerConfig.RETRIES_CONFIG, "3")
    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](producerProps)

    (1 to 1000000).foreach { i =>
      val callback: Callback = (metadata: RecordMetadata, exception: Exception) => {
        if (exception != null) {
          println("ERROR: " + exception)
        }

        println(metadata)
      }
      producer.send(new ProducerRecord[String, String](testTopic, s"key$i", s"value$i"), callback)
    }

    producer.close()
  }
}
