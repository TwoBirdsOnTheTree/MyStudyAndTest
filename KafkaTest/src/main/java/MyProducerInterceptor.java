import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class MyProducerInterceptor implements ProducerInterceptor<String, String> {
    private int successCount;
    private int errorCount;

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        System.out.println(String.format("Interceptor: %s", record));
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (null == exception)
            successCount++;
        else
            errorCount++;
    }

    @Override
    public void close() {
        System.out.println(String.format("成功次数是: %s, 失败次数是: %s", successCount, errorCount));
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
