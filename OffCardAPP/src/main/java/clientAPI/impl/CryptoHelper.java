package clientAPI.impl;

import java.nio.ByteBuffer;

public class CryptoHelper {

	public static byte[] signData(byte data){
		return signData(new byte[]{data});
	}
	
	
	public static byte[] signData(short data){
		return signData(ByteBuffer.allocate(Short.BYTES).putShort(data).array());
	}
	
	
	public static byte[] signData(int data){
		return signData(ByteBuffer.allocate(Integer.BYTES).putInt(data).array());
	}
	
	public static byte[] signData(String data){
		return signData(data.getBytes());
	}
	
	public static byte[] signData(byte[] data){
		
		//ToDo
		return data;
	}
}
