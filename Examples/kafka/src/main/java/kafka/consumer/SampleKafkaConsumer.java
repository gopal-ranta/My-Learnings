package kafka.consumer;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import kafka.customSerializer.MyCustomClass;
import kafka.rebalanceListener.RebalanceListener;

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
		
		/*
		 * instantiate a rebalance listener
		 */
		RebalanceListener rebalanceListener = new RebalanceListener(kafkaConsumer);
		
		while(true) {
			ConsumerRecords<String, MyCustomClass> records = kafkaConsumer.poll(100);
			for (ConsumerRecord<String, MyCustomClass> record : records) {
				/**
				 * Process the record
				 */
				System.out.println("Age :: " + record.value().getAge() + " Name :: " + record.value().getName());
				
				/**
				 * If consumer goes down after processing the records and before committing the offset, 
	   			   in that case we can use database to maintain the processed recods and committed offset and 
	   			   make these two methods (processing the records and committing the offset) transactional. 
				 */
				
				rebalanceListener.addCurrentOffset(record.topic(), record.partition(), record.offset());
			}
		}
	}

}
