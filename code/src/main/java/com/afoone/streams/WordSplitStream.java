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
import java.util.regex.Pattern;

public class WordSplitStream {

    public static void main(String[] args) throws InterruptedException {
        // Propiedades
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "iprocuratio.com:9092");
        // serializadores / deserializadores de la clave y el valor
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "alfonso.wordsplit");

        // Topología  - proceso de transformación del Stream
        StreamsBuilder builder = new StreamsBuilder();
        // mi objetivo coger datos de alfonso.frases y cada palabra que uso la voy a sacar por el topic alfonso.palabras

        // Creado un stream de datos a partir del topic "alfonso.nombres"
        KStream<String, String> source = builder.stream("alfonso.frases");

        // la problemática es que estoy "creando" más mensajes de los que recibo
        // por ejemplo el mensaje "hola mundo" va a crear dos mensajes: "hola" y "mundo"
        // usamos flatmapvalues que pasaremos una lambda que nos tiene que devolver un array

        Pattern pattern = Pattern.compile("\\W+");

        source
                .flatMapValues(value -> Arrays.asList(pattern.split(value))) // esta función tiene que devolver una colección con los valores que quiero que estén en mi stream
                .filterNot((key,value)->value.toLowerCase().equals("de"))
                .to("alfonso.palabras"); // lo escribo en otro topic

        // "Compilo" la topologia
        Topology topology = builder.build();

        // Iniciar el stream
        KafkaStreams stream = new KafkaStreams(topology, properties);
        stream.start();

        Thread.sleep(1000000L);

        stream.close();

    }
}
