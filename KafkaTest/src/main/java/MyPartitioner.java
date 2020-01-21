import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.internals.DefaultPartitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

public class MyPartitioner implements Partitioner {
    private DefaultPartitioner defaultPartitioner = new DefaultPartitioner();

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value,
                         byte[] valueBytes, Cluster cluster) {

        List<PartitionInfo> partitionInfos = cluster.availablePartitionsForTopic(topic);
        // System.out.println("availablePartitionsForTopic: " + partitionInfos.size());

        int partition = defaultPartitioner.partition(topic, key, keyBytes, value, valueBytes, cluster);
        System.out.println(String.format("Partitioner: 发送到的分区是: %s, key: %s, value: %s", partition, key, value));
        return partition;
    }

    @Override
    public void close() {
        defaultPartitioner.close();
    }

    @Override
    public void configure(Map<String, ?> configs) {
        defaultPartitioner.configure(configs);
    }
}
