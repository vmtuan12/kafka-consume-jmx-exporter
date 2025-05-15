package com.jmxExport;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.time.Duration;
import java.util.*;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class WebServer {
    public static void main(String[] args) throws Exception {
        Map<String, UserResource> mappingUserResource = new HashMap<>();

        File myObj = new File("/home/mhtuan/work/java-work/export-jmx/pass.db");
        Scanner reader = new Scanner(myObj);
        while (reader.hasNextLine()) {
            String userPassword = reader.nextLine();
            String user = userPassword.split(":")[0];

            UserResource userResource = new UserResource();
            mappingUserResource.put(user, userResource);

            registerBean(userResource, user);
        }
        reader.close();

        JSONParser parser = new JSONParser();
        Consumer<String, String> consumer = new KafkaConsumer<>(kafkaProperties());

        consumer.subscribe(Arrays.asList(new String[]{"test-trino-log"}));
        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                if(records.count() != 0) {
                    for(ConsumerRecord<String, String> record: records) {
                        JSONObject jsonObject = (JSONObject) parser.parse(record.value());

                        JSONObject context = (JSONObject) ((JSONObject) jsonObject.get("eventPayload")).get("context");
                        JSONObject stats = (JSONObject) ((JSONObject) jsonObject.get("eventPayload")).get("statistics");

                        String user = (String) context.get("user");

                        Double cpuTime = (Double) stats.get("cpuTime");
                        Double executionTime = (Double) stats.get("executionTime");
                        Long peakUserMemoryBytes = (Long) stats.get("peakUserMemoryBytes");
                        Long peakTaskTotalMemory = (Long) stats.get("peakTaskTotalMemory");

                        UserResource resource = mappingUserResource.get(user);
                        resource.setCpuTime(cpuTime);
                        resource.setExecutionTime(executionTime);
                        resource.setPeakUserMemoryBytes(peakUserMemoryBytes);
                        resource.setPeakTaskTotalMemory(peakTaskTotalMemory);

                        System.out.println(user + " - " + cpuTime + " - " + executionTime + " - " + peakUserMemoryBytes + " - " + peakTaskTotalMemory);
                        Thread.sleep(5000);

                        resource.setCpuTime(0.0);
                        resource.setExecutionTime(0.0);
                        resource.setPeakUserMemoryBytes(0L);
                        resource.setPeakTaskTotalMemory(0L);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties kafkaProperties() {
        Properties prop = new Properties();
        prop.put("bootstrap.servers", "localhost:9091");

        prop.setProperty("group.id", "tuandz-1");
        prop.setProperty("enable.auto.commit", "true");
        prop.setProperty("auto.commit.interval.ms", "1000");
        prop.setProperty("auto.offset.reset", "latest");

        prop.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        prop.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        return prop;
    }

    private static void registerBean(UserResource info, String username) {
        try {
            ObjectName objectName = new ObjectName("com.jmxExport:type=basic,name=" + username);
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.registerMBean(info, objectName);
        } catch (MalformedObjectNameException | InstanceAlreadyExistsException |
                 MBeanRegistrationException | NotCompliantMBeanException e) {
            e.printStackTrace();
        }
    }
}
