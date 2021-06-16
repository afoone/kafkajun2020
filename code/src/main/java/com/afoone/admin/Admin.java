package com.afoone.admin;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class Admin {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // Configuración
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "iprocuratio.com:9092");


        AdminClient adminClient = AdminClient.create(properties);

        for (Node node: adminClient.describeCluster().nodes().get()) {
            System.out.println(node.host()+":"+node.port());
        }

        // Ver los topics
        Collection<TopicListing> topicListings = adminClient.listTopics().listings().get();
        for (TopicListing topic:topicListings) {
//            System.out.println(topic.toString());
        }

        // Crear topics
        NewTopic newTopic = new NewTopic("new-topic", 3, (short) 1);
        adminClient.createTopics(Collections.singletonList(newTopic));

        // Ver los topics
       topicListings = adminClient.listTopics().listings().get();
        for (TopicListing topic:topicListings) {
            if (topic.name().equals("new-topic")) {
                System.out.println(topic.toString());
            }
        }

        //topic x 0 1 2 3 4 5 6

        }
}
