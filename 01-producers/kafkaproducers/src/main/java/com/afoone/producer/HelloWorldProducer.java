package com.afoone.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 *       Entidades Cliente.java (cuenta bancaria) ClienteDTO.java
 *         class Cliente {
 *             private nombre;
 *             private apellido;
 *             private iban;
 *         }
 *         class ClienteDTO {
 *             private nombre
 *             private apellido
 *         }
 *
 * Serialización
 *         ==> ByteArray "{nombre: "juanido", apellido: "pere"}"
 *             -> avro
 *                 "nombre//pepito-applido//perez"
 */

public class HelloWorldProducer {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("my first producer");
        // Propiedades (para configurar el productor)
        Properties properties = new Properties();
        // Hay 3 que son obligatorias: 1. Donde nos conectamos 2 y 3: serializadores de clave y valor
        // Donde nos conectamos
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());

        // Generaremos el productor
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        // Crear un mensaje
        ProducerRecord<String, String> record = new ProducerRecord<>("test", "hola mundo");

        // Enviar mensajes
        // Envío de un mensaje síncrono
        RecordMetadata respuesta = kafkaProducer.send(record).get();

        System.out.println("offset: "+respuesta.offset() + " partition " + respuesta.partition());

    }
}
