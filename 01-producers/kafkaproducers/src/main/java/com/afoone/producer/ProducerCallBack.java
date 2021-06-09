package com.afoone.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerCallBack implements Callback {
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        System.out.println("ha terminado con el offset "+recordMetadata.offset()+ "de la particion "+ recordMetadata.partition());
    }
}
