import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class ConsumerRebalanceTest {
    private static String topicName = "my-topic-rebalance-test";

    public static void main(String[] args) {
        ConsumerRebalanceTest t = new ConsumerRebalanceTest();
        t.test_rebalance_listener();
    }

    void test_rebalance_listener() {
        String groupId = "my-consumer-group-rebalance-id";

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", groupId);
        properties.put("enable.auto.commit", "true");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer
                = new KafkaConsumer<String, String>(properties);
        consumer.subscribe(Collections.singletonList(topicName),
                new ConsumerRebalanceListener() {
                    @Override
                    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

                    }

                    @Override
                    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

                    }
                });
    }

    @Test
    void producer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(properties);
        ProducerRecord<String, String> producerRecord =
                new ProducerRecord<>(topicName, "message-key", "message-value");
        producer.send(producerRecord);
        producer.close();
    }
}
