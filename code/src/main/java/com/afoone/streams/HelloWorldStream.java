package com.afoone.streams;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

public class HelloWorldStream {

    public static void main(String[] args) throws InterruptedException {
        // Propiedades
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "iprocuratio.com:9092");
        // serializadores / deserializadores de la clave y el valor
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "alfonso.helloworld");

        // Topología  - proceso de transformación del Stream
        StreamsBuilder builder = new StreamsBuilder();
        // mi objetivo es coger datos de un topic llamado "nombres" y hacer un nuevo topic "saludos"d saludando todo el mundo

        // Creado un stream de datos a partir del topic "alfonso.nombres"
        KStream<String, String> source = builder.stream("alfonso.nombres");

        // Transformo los valores en otra cosa (esto es, otro valor)
        source
                .filter((key, value) -> value.toLowerCase(Locale.ROOT).startsWith("a")) // filtro sólo los valores que empiezan por a
                .mapValues(value -> "hola "+ value) // transformando cada valor del stream en otro valor
                .to("alfonso.saludos"); // lo escribo en otro topic

        // "Compilo" la topologia
        Topology topology = builder.build();

        // Iniciar el stream
        KafkaStreams stream = new KafkaStreams(topology, properties);
        stream.start();

        Thread.sleep(1000000L);

        stream.close();

    }
}
