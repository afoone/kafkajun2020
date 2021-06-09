package com.afoone.producer;

import com.afoone.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializer;

import java.util.Properties;

public class AvroProducer {

    public static void main(String[] args) throws InterruptedException{
        System.out.println("my avro producer");
        // Propiedades (para configurar el productor)
        Properties properties = new Properties();
        // Hay 3 que son obligatorias: 1. Donde nos conectamos 2 y 3: serializadores de clave y valor
        // Donde nos conectamos
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // serialiador de avro
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName() );
        properties.put("schema.registry.url", "http://localhost:8081");


        // Generaremos el productor
        // el productor utiliza el objeto AVRO
        KafkaProducer<String, User> kafkaProducer = new KafkaProducer<>(properties);

        // Generamos un usuario
        User user = new User();
        user.setName("pepito");
        user.setFavoriteNumber(6);
        user.setFavoriteColor("azul");

        // Crear un mensaje
        // Creamos un mensaje con el objeto AVRO
        ProducerRecord<String, User> record = new ProducerRecord<>("usuario_creado", user);

        // Enviar mensajes
        // Envío de un mensaje asíncrono
        kafkaProducer.send(record, new ProducerCallback());
        System.out.println("enviar ejecutado");

        Thread.sleep(3000L);
    }

}
