package kafkaproducer.producer;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

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
		
		/**
		 * This approach of sending message is
		 * called fire and forget approach
		 */
		producer.send(producerRecord);
		
		/**
		 * This approach of sending message is
		 * called synchronous approach,as you wait for
		 * recordmetaData to come.
		 */
		try {
			RecordMetadata recordMetaData = producer.send(producerRecord).get();
			System.out.println("Partition :: " + recordMetaData.partition() + 
					"Offset :: " + recordMetaData.offset());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/**
		 * This approach of sending message is
		 * called asynchronous approach,
		 * you provide a callBack mechanism, which is called 
		 * after the message is received successfully at kafka.
		 */
		MyCallBack myCallBack = new MyCallBack();
		producer.send(producerRecord, myCallBack);
		
		/**
		 * finally close the producer.
		 */
		producer.close();
	}
	
	static class MyCallBack implements Callback{

		@Override
		public void onCompletion(RecordMetadata metadata, Exception exception) {
			/*
			 * In case of exception retry
			 */
			if (exception != null) {
				//resend the messa
			}
			
		}
		
		
	}

}
