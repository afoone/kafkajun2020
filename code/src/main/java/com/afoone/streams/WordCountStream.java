package com.afoone.streams;


import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

public class WordCountStream {

    public static void main(String[] args) throws InterruptedException {
        // Propiedades
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "iprocuratio.com:9092");
        // serializadores / deserializadores de la clave y el valor
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "alfonso.wordcount");

        // Topología  - proceso de transformación del Stream
        StreamsBuilder builder = new StreamsBuilder();
        // mi objetivo coger datos de alfonso.frases y contar las ocurrencias de cada palabra

        // Creado un stream de datos a partir del topic "alfonso.frases"
        KStream<String, String> source = builder.stream("alfonso.frases");

        // la problemática es que estoy "creando" más mensajes de los que recibo
        // por ejemplo el mensaje "hola mundo" va a crear dos mensajes: "hola" y "mundo"
        // usamos flatmapvalues que pasaremos una lambda que nos tiene que devolver un array

        Pattern pattern = Pattern.compile("\\W+");

        source
                .flatMapValues(value -> Arrays.asList(pattern.split(value)))
                .map((key, value)-> new KeyValue<>(value, value)) // genero por cada palabra un eleemento con la key de la palabra
                .groupByKey() // agrupo por key
                .count(Materialized.as("count"))
                .mapValues(value->Long.toString(value))
                .toStream() // genera el changelof
                .to("alfonso.wordcount");

        // "Compilo" la topologia
        Topology topology = builder.build();

        System.out.println(topology.describe());

        // Iniciar el stream
        KafkaStreams stream = new KafkaStreams(topology, properties);
        stream.start();

        Thread.sleep(1000000L);

        stream.close();

    }
}
