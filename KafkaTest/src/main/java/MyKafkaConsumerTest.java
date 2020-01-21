import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class MyKafkaConsumerTest {
    public static void main(String[] args) {
        String topicName = "my-topic";
        String groupId = "my-consumer-group-id";

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("group.id", groupId);
        properties.put("enable.auto.commit", "true");
        // properties.put("auto.offset.reset", "earliest"); //
        // properties.put("auto.offset.reset", "latest");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer
                = new KafkaConsumer<String, String>(properties);

        consumer.subscribe(Collections.singletonList(topicName));

        try {
            while (true) {
                // org.apache.kafka.clients.consumer.KafkaConsumer.poll(java.time.Duration)
                ConsumerRecords<String, String> poll = consumer.poll(1000);
                System.out.println("poll size: " + poll.count());
                poll.iterator().forEachRemaining(c -> {
                    System.out.println(String.format("offset: %s, key: %s, value: %s",
                            c.offset(), c.key(), c.value()));
                });
            }
        } finally {
            consumer.close();
        }
    }
}
