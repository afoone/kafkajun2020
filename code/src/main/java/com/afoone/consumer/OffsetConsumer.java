package com.afoone.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class OffsetConsumer {
    public static void main(String[] args) {
        // Propiedades
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "iprocuratio.com:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // grupo, offset-reset
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "chat.reader");
        // el grupo, en una particióon tien un offset (por donde voy)
        //properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        // Crear el consumidor
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(properties);

        // Suscribimos el consumidor a topics
        //        Collection<String> topics = new ArrayList<String>();
        //        topics.add("chat");
        //        topics.add("clase");
        kafkaConsumer.subscribe(Collections.singletonList("chat"));

        // Crear el consumer poll
        // infinite loop
        while (true) {

            // recoger los records (offset 24)
          ConsumerRecords<String, String> records =  kafkaConsumer.poll(Duration.ofMillis(100L));
          // tengo records
            for (ConsumerRecord<String, String> record:records) {
                // Hacer lo que sea
                // Donde llamaría a la función para facturar
                System.out.println(record.key() + " ha dicho " + record.value());
            }
            // quiero informar a kafka que ya he procesado los tres.

            // <---------   SI PETA AQUÍ el sistema se pararía y al volver a acargar volvería al 24
            // AT LEAST ONCE

            // Oye kafka, que he procesado los tres y ya voy por el 27
            kafkaConsumer.commitSync();
        }


    }
}
