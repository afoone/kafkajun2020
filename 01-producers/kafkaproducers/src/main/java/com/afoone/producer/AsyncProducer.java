package com.afoone.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

class ProducerCallback implements Callback {
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        System.out.print("ha terminado con el offset "+recordMetadata.offset() + "en la partición "+ recordMetadata.partition());
    }
}

public class AsyncProducer {
    public static void main(String[] args) throws  InterruptedException, ExecutionException {
        System.out.println("my first producer");
        // Propiedades (para configurar el productor)
        Properties properties = new Properties();
        // Hay 3 que son obligatorias: 1. Donde nos conectamos 2 y 3: serializadores de clave y valor
        // Donde nos conectamos
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Generaremos el productor
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        // Crear un mensaje
        ProducerRecord<String, String> record = new ProducerRecord<>("usuario_creado", "jperez");

        // Enviar mensajes
        // Envío de un mensaje asíncrono
        kafkaProducer.send(record, new ProducerCallback());
        System.out.println("enviar ejecutado");

        Thread.sleep(3000L);

    }
}
