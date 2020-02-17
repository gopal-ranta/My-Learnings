package kafka.customSerializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.kafka.common.serialization.Serializer;

public class CustomSerializer implements Serializer<MyCustomClass>{

	private String encoding = "UTF8";
	@Override
	public byte[] serialize(String topic, MyCustomClass data) {
		// TODO Auto-generated method stub
		int sizeOfName;
		byte[] serializeName;
		
		if (data == null)
			return null;
		
		try {
			serializeName = data.getName().getBytes(encoding);
			sizeOfName = serializeName.length;
			/**
			 * 4 for age(int), 4 for sizeOfName, and remaining is the 
			 * actual bytes for name
			 */
			ByteBuffer bf = ByteBuffer.allocate(4 + 4 + sizeOfName);
			
			/**
			 * Put the age 
			 */
			bf.putInt(data.getAge());
			
			/**
			 * put the size of name
			 */
			bf.putInt(sizeOfName);
			
			/**
			 * put bytes of name
			 */
			bf.put(serializeName);
			
			return bf.array();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
