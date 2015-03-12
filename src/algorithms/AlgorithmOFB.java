package algorithms;

public class AlgorithmOFB {

	private byte[] initReg;

	public AlgorithmOFB(byte[] init) {
		this.initReg = init.clone();
	}
	
	public byte[] encrypt(byte[] key, byte[] message) throws Exception {
		return cipher(key, message);
	}
	
	public byte[] decrypt(byte[] key, byte[] message) throws Exception {
		return cipher(key, message);
	}

	private byte[] cipher(byte[] key, byte[] message) throws Exception {
		AlgorithmDES cipher = new AlgorithmDES();
		byte[] shiftReg = initReg.clone();
		byte[] output = new byte[message.length];

		for (int i = 0; i < message.length; i++) {
			byte[] encrypted = cipher.encrypt(key, shiftReg);
			output[i] = xor(encrypted[0], message[i]);
			shiftReg = appendRight(shiftReg, encrypted[0]);
		}

		return output;
	}

	private byte xor(byte a, byte b) throws Exception {
		return (byte) (0xff & ((int) a) ^ ((int) b));
	}

	private byte[] appendRight(byte[] in, byte b) {
		byte[] out = new byte[in.length];
		for (int i = 1; i < in.length; i++) {
			out[i - 1] = in[i];
		}
		out[in.length - 1] = b;
		return out;
	}

}
