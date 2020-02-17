package kafka.customDeserializer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.kafka.common.serialization.Deserializer;

import kafka.customSerializer.MyCustomClass;

public class CustomDeserializer implements Deserializer<MyCustomClass> {

	private String encoding = "UTF8";
	@Override
	public MyCustomClass deserialize(String topic, byte[] data) {
		// TODO Auto-generated method stub
		if (data == null)
			return null;
		
		ByteBuffer bf = ByteBuffer.wrap(data);
		/**
		 * get the params in the same sequence 
		 * as you added in custom serializer
		 */
		
		/**
		 * get age
		 */
		int age = bf.getInt();
		
		/**
		 * get size of name
		 */
		int sizeOfName = bf.getInt();
		
		/**
		 * get the bytes of name
		 */
		 byte[] nameBytes = new byte[sizeOfName];
		 bf.get(nameBytes);
		 String deserialiedName = null;
		 try {
			deserialiedName = new String(nameBytes, encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new MyCustomClass(deserialiedName, age);
	}

}
