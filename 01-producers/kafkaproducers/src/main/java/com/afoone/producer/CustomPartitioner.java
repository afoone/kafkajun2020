package com.afoone.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

public class CustomPartitioner  implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keybytes, Object value, byte[] valueBytes, Cluster cluster) {
        // Apple nos viene con la key "apple" - > a la cola de prioridad
        List<PartitionInfo> partitions = cluster.availablePartitionsForTopic(topic);
        int npartitions = partitions.size();
        if (keybytes.toString().startsWith("apple")) return 0;
        return (Math.abs(Utils.murmur2(keybytes))%npartitions)+1;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
