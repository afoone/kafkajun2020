package com.afoone.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;

import java.time.Duration;
import java.util.Properties;

public class PageViews {
    public static void main(String[] args) throws InterruptedException {
        // Propiedades
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "iprocuratio.com:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "alfonso.pageviews");

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> views = builder.stream("alfonso.views");
        KStream<String, String> clicks = builder.stream("alfonso.clicks");
        //source.mapValues(value -> "pagina vista " +  value).to("alfonso.views2");

        KStream<String, String> conversiones = views.outerJoin(
                clicks,
                (viewsValue, clicksvalue) -> "vista: "+viewsValue+" click: "+clicksvalue,
                JoinWindows.of(Duration.ofMinutes(5)));
                //Joined.with(Serdes.String(), Serdes.String(), Serdes.String())

        conversiones.to("conversiones");


        Topology topology = builder.build();

        KafkaStreams stream = new KafkaStreams(topology, properties);
        stream.start();

        Thread.sleep(10000000L);

        // KSQL -

    }
}
