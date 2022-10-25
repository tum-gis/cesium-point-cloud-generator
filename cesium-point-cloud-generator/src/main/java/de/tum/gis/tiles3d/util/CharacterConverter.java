/*
 * Cesium Point Cloud Generator
 * 
 * Copyright 2017 - 2018
 * Chair of Geoinformatics
 * Technical University of Munich, Germany
 * https://www.gis.bgu.tum.de/
 * 
 * The Cesium Point Cloud Generator is developed at Chair of Geoinformatics,
 * Technical University of Munich, Germany.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tum.gis.tiles3d.util;
import java.nio.ByteBuffer;
import org.apache.commons.lang3.ArrayUtils;

public class CharacterConverter {
	
	public static final byte[] convertToByteArray(double[] inData) {
		int j = 0;
		int length = inData.length;
		byte[] outData = new byte[length * 8];
		for (int i = 0; i < length; i++) {
			long data = Double.doubleToLongBits(inData[i]);
			outData[j++] = (byte) (data >>> 0);
			outData[j++] = (byte) (data >>> 8);
			outData[j++] = (byte) (data >>> 16);
			outData[j++] = (byte) (data >>> 24);
			outData[j++] = (byte) (data >>> 32);
			outData[j++] = (byte) (data >>> 40);
			outData[j++] = (byte) (data >>> 48);
			outData[j++] = (byte) (data >>> 56);
		}
		
		return outData;
	}

	public static byte[] convertToByteArray(double inData) {
		return ByteBuffer.allocate(Double.SIZE / Byte.SIZE).putDouble(inData).array();
	}

	public static final byte[] convertToByteArray(float[] inData) {
		int j = 0;
		int length = inData.length;
		byte[] outData = new byte[length * 4];
		for (int i = 0; i < length; i++) {
			int data = Float.floatToIntBits(inData[i]);
			outData[j++] = (byte) (data >>> 0);
			outData[j++] = (byte) (data >>> 8);
			outData[j++] = (byte) (data >>> 16);
			outData[j++] = (byte) (data >>> 24);
		}
		
		return outData;
	}

	public static byte[] convertToByteArray(float inData) {
		return ByteBuffer.allocate(Float.SIZE / Byte.SIZE).putFloat(inData).array();
	}

	public static byte[] convertToByteArray(int inData) {
		byte[] result = new byte[4];
	
		result[0] = (byte) (inData >>> 0);
		result[1] = (byte) (inData >>> 8);
		result[2] = (byte) (inData >>> 16);
		result[3] = (byte) (inData >>> 24);
	
		return result;
	}

	public static float convertToFloat(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getFloat();
	}

	public static double convertToDouble(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getDouble();
	}

	public static byte[] concatTwoByteArrays(byte[] bytes1, byte[] bytes2) {
		return ArrayUtils.addAll(bytes1, bytes2);
	}

	public static double[] concatTwoDoubleArrays(double[] array1, double[] array2) {
		return ArrayUtils.addAll(array1, array2);
	}

}
