package kafkaproducer.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SampleKafkaProducer {
	public static void main(String[] args) {
	
		String topicName = "sampleTopic";
		String key = "Key1";
		String value = "Value1";
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092, localhost:9093");
		props.put("key.serializer", "org.apache.common.StringSerialize");
		props.put("value.serializer", "org.apache.common.StringSerialize");
		
		Producer<String, String> producer = new KafkaProducer<>(props);
		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topicName, key, value);
		
		producer.send(producerRecord);
		
		producer.close();
	}
	

}
