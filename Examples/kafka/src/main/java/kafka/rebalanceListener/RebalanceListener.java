package kafka.rebalanceListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;import org.apache.kafka.common.errors.TopicAuthorizationException;

import kafka.customSerializer.MyCustomClass;

/**
 * @author GRA
 *
 */
public class RebalanceListener implements ConsumerRebalanceListener {
	private KafkaConsumer<String, MyCustomClass> consumer;
	private Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();
	
	public void addCurrentOffset(String topic, int partition, long offSet) {
		/**
		 * this method is called by consumer 
		 * at every record processed, to maintain the offset up-to-date.
		 */
		currentOffset.put(new TopicPartition(topic, partition), new OffsetAndMetadata(offSet));
	}
	
	public Map<TopicPartition, OffsetAndMetadata> getCurrentOffSets() {
		return currentOffset;
	}
	
	
	
	public RebalanceListener(KafkaConsumer<String, MyCustomClass> consumer) {
		super();
		this.consumer = consumer;
	}

	@Override
	public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
		/**
		 * nothing to do much here
		 */
		for (TopicPartition partition : partitions) {
			System.out.println(partition.partition());
		}

	}

	@Override
	public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
		/**
		 * In case re-balancing happens
		 * and current partition is taken away from consumer. 
		 */
		consumer.commitSync(currentOffset);
		currentOffset.clear();

	}

}
