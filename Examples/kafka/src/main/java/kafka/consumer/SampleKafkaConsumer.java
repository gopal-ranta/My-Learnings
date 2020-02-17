package kafka.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import kafka.customSerializer.MyCustomClass;

public class SampleKafkaConsumer {
	
	public static void main(String[] args) {
		
		String topicName = "topicName";
		String groupName = "MyConsumerGroup";
		
		Properties props = new Properties();
		props.put("bootstarp.servers", "localhost:9092, loaclhost:9093");
		/**
		 * new to consumer.
		 */
		props.put("group.id", groupName);
		
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		KafkaConsumer<String, MyCustomClass> kafkaConsumer = new KafkaConsumer<>(props);
		kafkaConsumer.subscribe(Arrays.asList(topicName));
		
		while(true) {
			ConsumerRecords<String, MyCustomClass> records = kafkaConsumer.poll(100);
			for (ConsumerRecord<String, MyCustomClass> record : records) {
				System.out.println("Age :: " + record.value().getAge() + " Name :: " + record.value().getName());
			}
		}
	}

}
