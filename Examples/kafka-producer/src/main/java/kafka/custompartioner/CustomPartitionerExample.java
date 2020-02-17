package kafka.custompartioner;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.utils.Utils;

public class CustomPartitionerExample {
	
	public static void main(String[] args) {
		String topicName = "sampleTopic";
		String key = "Key1";
		String value = "Value1";
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092, localhost:9093");
		props.put("key.serializer", "org.apache.common.StringSerialize");
		props.put("value.serializer", "org.apache.common.StringSerialize");
		props.put("partitioner.class", "MyCustomPartitioner");
		props.put("my.key.name", "gopal");
		
		Producer<String, String> producer = new KafkaProducer<>(props);
		ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topicName, key, value);
		
		/**
		 * This approach of sending message is
		 * called fire and forget approach
		 */
		for (int i=0; i<10; i++)
			producer.send(new ProducerRecord<>(topicName, "gopal", "abc" + i));
		
		for (int i=0; i<10; i++)
			producer.send(new ProducerRecord<>(topicName, "ratna", "abc" + i));
	}

}
class MyCustomPartitioner implements Partitioner {

	String keyName = null;
	
	@Override
	public void configure(Map<String, ?> configs) {
		/**
		 * Get the key that you want to treat specially
		 * this key has already been set in the producer config
		 * in our example its "gopal", see producer code.
		 */
		keyName = configs.get("my.key.name").toString();		
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		int numOfPatitions = cluster.partitionCountForTopic(topic);
		int partion;
		/**
		 * you want to send all the messages which have key "gopal", 
		 * to partitions 0,1,2
		 * 
		 */
		int sp = (int) Math.abs(numOfPatitions*.3);
		
		/**
		 * if key is "gopal", return partition 
		 * any of 0,1,2
		 */
		if (((String)key).equals(keyName)) 
			partion = Utils.toPositive(Utils.murmur2(valueBytes)) %sp;
			
		/**
		 * if key is other than "gopal", return partition 
		 * any of 3,4,5,6,7,8,9
		 */
		else
			partion = Utils.toPositive(Utils.murmur2(valueBytes)) % (numOfPatitions - sp) + sp;
		
		return partion;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
}