package com.afoone.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

class ProducerCallback4Partitions implements Callback {
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        System.out.println("ha terminado con el offset "+recordMetadata.offset()+ "de la particion "+ recordMetadata.partition());
    }
}

public class CustomPartitionerProducer {
    public static void main(String[] args) throws  InterruptedException, ExecutionException {
        System.out.println("my first producer");
        // Propiedades (para configurar el productor)
        Properties properties = new Properties();
        // Hay 3 que son obligatorias: 1. Donde nos conectamos 2 y 3: serializadores de clave y valor
        // Donde nos conectamos
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, "2000");

        // Generaremos el productor
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
       // kafkaProducer

        // Crear un mensaje
        ProducerRecord<String, String> record = new ProducerRecord<>("partitions", "1");

        // Enviar mensajes
        // Envío de un mensaje asíncrono
        kafkaProducer.send(record, new ProducerCallback4Partitions());
        ProducerRecord<String, String> record2 = new ProducerRecord<>("partitions", Integer.valueOf(2), null, " este a la particion 2");

        Thread.sleep(3000L);

        // Enviar mensajes
        // Envío de un mensaje asíncrono
        kafkaProducer.send(record2, new ProducerCallback4Partitions());
        ProducerRecord<String, String> record3 = new ProducerRecord<>("partitions", "3");

        Thread.sleep(3000L);
        // Enviar mensajes
        // Envío de un mensaje asíncrono
        kafkaProducer.send(record3, new ProducerCallback4Partitions());
        System.out.println("enviar ejecutado");

        Thread.sleep(3000L);

    }
}
