package cn.gson.oasys.common;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class TronUtils {
	static int ADDRESS_SIZE = 21;
	private static byte addressPreFixByte = (byte) 0x41; // 41 + address (byte) 0xa0; //a0 + address
	public static String toHexAddress(String tAddress) {
		return ByteArray.toHexString(decodeFromBase58Check(tAddress));
	}


	private static byte[] decodeFromBase58Check(String addressBase58) {
		if (StringUtils.isEmpty(addressBase58)) {
			return null;
		}
		byte[] address = decode58Check(addressBase58);
		if (!addressValid(address)) {
			return null;
		}
		return address;
	}

	private static byte[] decode58Check(String input) {
		byte[] decodeCheck = Base58.decode(input);
		if (decodeCheck.length <= 4) {
			return null;
		}
		byte[] decodeData = new byte[decodeCheck.length - 4];
		System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
		byte[] hash0 = Sha256Hash.hash(true, decodeData);
		byte[] hash1 = Sha256Hash.hash(true, hash0);
		if (hash1[0] == decodeCheck[decodeData.length] && hash1[1] == decodeCheck[decodeData.length + 1]
				&& hash1[2] == decodeCheck[decodeData.length + 2] && hash1[3] == decodeCheck[decodeData.length + 3]) {
			return decodeData;
		}
		return null;
	}

	private static boolean addressValid(byte[] address) {
		if (ArrayUtils.isEmpty(address)) {
			return false;
		}
		if (address.length != ADDRESS_SIZE) {
			return false;
		}
		byte preFixbyte = address[0];
		return preFixbyte == getAddressPreFixByte();
		// Other rule;
	}
	private static byte getAddressPreFixByte() {
		return addressPreFixByte;
	}

	public static void main(String args[]) {
		System.out.println(toHexAddress("TDSapWWEAxsjZMyoBSeFMheyQ1pnk3YLa2"));
		//System.out.println(toHexAddress("TGvZFtnLdiTuH1CweHuALJFVE2GTDvHBbF").equals("414c488a9061587ecf9bdd654c1ba1123371e7f732"));
//		414c488a9061587ecf9bdd654c1ba1123371e7f732
	}

}
