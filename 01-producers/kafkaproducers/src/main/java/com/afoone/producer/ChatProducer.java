package com.afoone.producer;


import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


public class ChatProducer {
    public static void main(String[] args) throws  InterruptedException, ExecutionException {
        System.out.println("my first producer");
        // Propiedades (para configurar el productor)
        Properties properties = new Properties();
        // Hay 3 que son obligatorias: 1. Donde nos conectamos 2 y 3: serializadores de clave y valor
        // Donde nos conectamos
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "iprocuratio.com:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        Scanner sc = new Scanner(System.in);

        // Generaremos el productor
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);



        while (sc.hasNextLine()) {
            String entrada = sc.nextLine();
            ProducerRecord<String, String> record = new ProducerRecord<>("chat", "atienda", entrada);

            // Enviar mensajes
            // Envío de un mensaje asíncrono
            kafkaProducer.send(record, new ProducerCallback());
            System.out.println("enviar ejecutado");
        }

    }
}
