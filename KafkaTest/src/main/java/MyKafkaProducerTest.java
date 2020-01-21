import org.apache.kafka.clients.producer.*;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MyKafkaProducerTest {
    public static void main(String args[]) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("partitioner.class", "MyPartitioner");
        // properties.put("acks", "-1");
        properties.put("retries", "3");
        properties.put("batch.size", 323840);
        properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, new ArrayList<String>() {{
            add("MyProducerInterceptor");
        }});


        Producer<String, String> producer = new KafkaProducer<>(properties);

        IntStream.range(0, 2).forEach(i -> {
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>("my-topic", "k-" + i, "v-" + i);
                    // new ProducerRecord<>("my-topic", 1, "k-" + i, "v-" + i);

            Future<RecordMetadata> sendFuture =
                    producer.send(producerRecord,
                            (recordMetadta, exception) -> {
                                if (Objects.nonNull(exception)) {
                                    // do something;
                                }
                            });
        });

        producer.close();
    }
}