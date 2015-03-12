package algorithms;
public class AlgorithmDES {
	
	private boolean log;
	
	   private final int[] INIT_PERM = {
	      58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
	      62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
	      57, 49, 41, 33, 25, 17,  9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
	      61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
	   };
	   
	   private final int[] EXPAND_PERM = {
	      32,  1,  2,  3,  4,  5, 4,  5,  6,  7,  8,  9,
	       8,  9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17,
	      16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25,
	      24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32,  1
	   };
	   
	   private final int[] PBLOCK = {
	      16,  7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10,
	       2,  8, 24, 14, 32, 27,  3,  9, 19, 13, 30,  6, 22, 11, 4, 25
	   };
	   
	   private final int[] INV_INIT_PERM = {
	      40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
	      38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
	      36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
	      34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41,  9, 49, 17, 57, 25
	   };
	   
	   
	   private final int[] SBLOCK = {
		// S1
		14,  4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7,
		 0, 15,  7,  4, 14,  2, 13,  1, 10,  6, 12, 11,  9,  5,  3,  8,
		 4,  1, 14,  8, 13,  6,  2, 11, 15, 12,  9,  7,  3, 10,  5,  0,
		15, 12,  8,  2,  4,  9,  1,  7,  5, 11,  3, 14, 10,  0,  6, 13,
		
		// S2
		15,  1,  8, 14,  6, 11,  3,  4,  9,  7,  2, 13, 12,  0,  5, 10,
		 3, 13,  4,  7, 15,  2,  8, 14, 12,  0,  1, 10,  6,  9, 11,  5,
		 0, 14,  7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15,
		13,  8, 10,  1,  3, 15,  4,  2, 11,  6,  7, 12,  0,  5, 14,  9,
		
		// S3
		10,  0,  9, 14,  6,  3, 15,  5,  1, 13, 12,  7, 11,  4,  2,  8,
		13,  7,  0,  9,  3,  4,  6, 10,  2,  8,  5, 14, 12, 11, 15,  1,
		13,  6,  4,  9,  8, 15,  3,  0, 11,  1,  2, 12,  5, 10, 14,  7,
		 1, 10, 13,  0,  6,  9,  8,  7,  4, 15, 14,  3, 11,  5,  2, 12,
		 
		// S4
		 7, 13, 14,  3,  0,  6,  9, 10,  1,  2,  8,  5, 11, 12,  4, 15,
		13,  8, 11,  5,  6, 15,  0,  3,  4,  7,  2, 12,  1, 10, 14,  9,
		10,  6,  9,  0, 12, 11,  7, 13, 15,  1,  3, 14,  5,  2,  8,  4,
		 3, 15,  0,  6, 10,  1, 13,  8,  9,  4,  5, 11, 12,  7,  2, 14,
		 
		// S5
		 2, 12,  4,  1,  7, 10, 11,  6,  8,  5,  3, 15, 13,  0, 14,  9,
		14, 11,  2, 12,  4,  7, 13,  1,  5,  0, 15, 10,  3,  9,  8,  6,
		 4,  2,  1, 11, 10, 13,  7,  8, 15,  9, 12,  5,  6,  3,  0, 14,
		11,  8, 12,  7,  1, 14,  2, 13,  6, 15,  0,  9, 10,  4,  5,  3,
		
		// S6
		12,  1, 10, 15,  9,  2,  6,  8,  0, 13,  3,  4, 14,  7,  5, 11,
		10, 15,  4,  2,  7, 12,  9,  5,  6,  1, 13, 14,  0, 11,  3,  8,
		 9, 14, 15,  5,  2,  8, 12,  3,  7,  0,  4, 10,  1, 13, 11,  6,
		 4,  3,  2, 12,  9,  5, 15, 10, 11, 14,  1,  7,  6,  0,  8, 13,
		 
		// S7
		 4, 11,  2, 14, 15,  0,  8, 13,  3, 12,  9,  7,  5, 10,  6,  1,
		13,  0, 11,  7,  4,  9,  1, 10, 14,  3,  5, 12,  2, 15,  8,  6,
		 1,  4, 11, 13, 12,  3,  7, 14, 10, 15,  6,  8,  0,  5,  9,  2,
		 6, 11, 13,  8,  1,  4, 10,  7,  9,  5,  0, 15, 14,  2,  3, 12,
		 
		// S8
		13,  2,  8,  4,  6, 15, 11,  1, 10,  9,  3, 14,  5,  0, 12,  7,
		 1, 15, 13,  8, 10,  3,  7,  4, 12,  5,  6, 11,  0, 14,  9,  2,
		 7, 11,  4,  1,  9, 12, 14,  2,  0,  6, 10, 13, 15,  3,  5,  8,
		 2,  1, 14,  7,  4, 10,  8, 13, 15, 12,  9,  0,  3,  5,  6, 11
	   };
	   
	   private final int[] KEY_PERM_1 = {
	      57, 49, 41, 33, 25, 17,  9, 1, 58, 50, 42, 34, 26, 18,
	      10,  2, 59, 51, 43, 35, 27, 19, 11,  3, 60, 52, 44, 36,
	      63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22,
	      14,  6, 61, 53, 45, 37, 29, 21, 13,  5, 28, 20, 12,  4
	   };
	   
	   private final int[] KEY_PERM_2 = {
	      14, 17, 11, 24,  1,  5, 3, 28, 15,  6, 21, 10,
	      23, 19, 12,  4, 26,  8, 16,  7, 27, 20, 13,  2,
	      41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48,
	      44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
	   };
	   
	   private final int[] SHIFTS = {
	      1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
	   };
	
	public AlgorithmDES() {
		this.log = false;
	}
	
	public AlgorithmDES(boolean log) {
		this.log = log;
	}
	
	public byte[] encrypt(byte[] key, byte[] message) throws Exception {
		String mode = "encrypt";
		byte[][] desKeys = getDESkeys(key);
		byte[] encrypted = cipher(message,desKeys,mode);
		
		if (log) log(key, message, encrypted);
		
		return encrypted;
	}
	
	public byte[] decrypt(byte[] key, byte[] message) throws Exception {
		String mode = "decrypt";
		byte[][] desKeys = getDESkeys(key);
		byte[] decrypted = cipher(message,desKeys,mode);
		
		if (log) log(key, message, decrypted);
		
		return decrypted;
	}
	
	private void log(byte[] key, byte[] message, byte[] cipher) {
        printBytes(key,"Key block");
        printBytes(message,"Input block");    
        printBytes(cipher,"Output block");    
	}
   
   private byte[] cipher(byte[] message, byte[][] desKeys, String mode) throws Exception {
      if (message.length<8) {
         throw new Exception("Message is less than 64 bits.");
      }
         
      message = permutation(message,INIT_PERM);
      int blockSize = INIT_PERM.length;
      byte[] left = copyOfRange(message,0,blockSize/2);
      byte[] right = copyOfRange(message,blockSize/2,blockSize/2);
      int numOfDESKeys = desKeys.length;
      for (int k=0; k<numOfDESKeys; k++) {
      	 byte[] rBackup = right;
         right = permutation(right,EXPAND_PERM);
         if (mode.equalsIgnoreCase("encrypt")) {
            right = XORBytes(right,desKeys[k]);
         } else {
            right = XORBytes(right,desKeys[numOfDESKeys-k-1]);
         }
         right = substitution(right); 
         right = permutation(right,PBLOCK); 
         right = XORBytes(left,right); 
         left = rBackup; 
      }
      byte[] lr = joinBits(right,blockSize/2,left,blockSize/2);
      lr = permutation(lr,INV_INIT_PERM);
      return lr;
   };
   
   private byte[] XORBytes(byte[] a, byte[] b) {
      byte[] out = new byte[a.length];
      for (int i=0; i<a.length; i++) {
         out[i] = (byte) (a[i] ^ b[i]);
      }
      return out;
   };
   
   private byte[] substitution(byte[] input) {
      input = splitBytes(input,6);
      byte[] out = new byte[input.length/2];
      int lhByte = 0;
      for (int block=0; block<input.length; block++) {
         byte valByte = input[block];
         int row = 2*(valByte>>7&0x0001)+(valByte>>2&0x0001); // 1 and 6 bity okreœlaj¹ nr wiersza w S-bloku
         int column = valByte>>3&0x000F; // œrodkowe 4 bity okreœlaj¹ numer kolumny w S-bloku
         int hByte = SBLOCK[64*block+16*row+column];
         if (block%2==0) {
        	 lhByte = hByte;
         } else {
        	 out[block/2] = (byte) (16*lhByte + hByte);
         }
      }
      return out;
   }
   
   private byte[] splitBytes(byte[] in, int len) {
      int numOfBytes = (8*in.length-1)/len + 1;
      byte[] out = new byte[numOfBytes];
      for (int i=0; i<numOfBytes; i++) {
         for (int j=0; j<len; j++) {
            int val = getBit(in, len*i+j); 
            setBit(out,8*i+j,val);
         }
      }
      return out;
   }
   
   private byte[][] getDESkeys(byte[] key)
      throws Exception {
      int activeKeySize = KEY_PERM_1.length; 
      int numOfDESKeys = SHIFTS.length;
      byte[] activeKey = permutation(key,KEY_PERM_1);
      int halfKeySize = activeKeySize/2;
      byte[] left = copyOfRange(activeKey,0,halfKeySize);
      byte[] right = copyOfRange(activeKey,halfKeySize,halfKeySize);
      byte[][] desKeys = new byte[numOfDESKeys][];
      for (int k=0; k<numOfDESKeys; k++) {
         left = shiftLeft(left,halfKeySize,SHIFTS[k]);
         right = shiftLeft(right,halfKeySize,SHIFTS[k]);
         byte[] leftRight = joinBits(left,halfKeySize,right,halfKeySize); 
         desKeys[k] = permutation(leftRight,KEY_PERM_2);
      }

      return desKeys;
   }
   
   private byte[] shiftLeft(byte[] input, int length, int step) {
      int numOfBytes = (length-1)/8 + 1;
      byte[] out = new byte[numOfBytes];
      for (int i=0; i<length; i++) {
         int val = getBit(input,(i+step)%length);
         setBit(out,i,val);
      }
      return out;
   }
   
   private byte[] joinBits(byte[] a, int aLength, byte[] b, 
      int bLength) {
      int numOfBytes = (aLength+bLength-1)/8 + 1;
      byte[] out = new byte[numOfBytes];
      int j = 0;
      for (int i=0; i<aLength; i++) {
         int val = getBit(a,i);
         setBit(out,j,val);
         j++;
      }
      for (int i=0; i<bLength; i++) {
         int val = getBit(b,i);
         setBit(out,j,val);
         j++;
      }
      return out;
   }
   
   private byte[] copyOfRange(byte[] input, int pos, int len) {
      int numOfBytes = (len-1)/8 + 1;
      byte[] out = new byte[numOfBytes];
      for (int i=0; i<len; i++) {
         int val = getBit(input,pos+i);
         setBit(out,i,val);
      }
      return out;
   }
   
   private byte[] permutation(byte[] input, int[] map) {
      int numOfBytes = (map.length-1)/8 + 1;
      byte[] out = new byte[numOfBytes];
      for (int i=0; i<map.length; i++) {
         int val = getBit(input,map[i]-1);
         setBit(out,i,val);
      }
      return out;
   }
   
   private int getBit(byte[] data, int position) {
      int posByte = position/8; 
      int posBit = position%8;
      byte valByte = data[posByte];
      int valInt = valByte>>(8-(posBit+1)) & 0x0001;
      return valInt;
   }
   private void setBit(byte[] data, int position, int val) {
      int posByte = position/8; 
      int posBit = position%8;
      byte oldByte = data[posByte];
      oldByte = (byte) (((0xFF7F>>posBit) & oldByte) & 0x00FF);
      byte newByte = (byte) ((val<<(8-(posBit+1))) | oldByte);
      data[posByte] = newByte;
   }
   
   private void printBytes(byte[] data, String name) {
      System.out.println("");
      System.out.println(name+":");
      for (int i=0; i<data.length; i++) {
         System.out.print(byteToBits(data[i])+" ");
      }
      System.out.println();
   }
   private String byteToBits(byte b) {
      StringBuffer buf = new StringBuffer();
      for (int i=0; i<8; i++)
         buf.append((int)(b>>(8-(i+1)) & 0x0001));
      return buf.toString();
   }
   
}