import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class MySerializer implements Serializer<Object> {
    private StringSerializer stringSerializer = new StringSerializer();

    @Override
    public byte[] serialize(String topic, Object data) {
        if (data instanceof String) {
            return stringSerializer.serialize(topic, (String) data);
        }
        return new byte[0];
    }
}
