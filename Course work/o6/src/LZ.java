import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LZ {
  private static final int MAX_LOOKBACK = 32 * 1024; // 64 KB
  static byte[] buffer; // bytearray buffer
  static int position = 0;
  //static byte[] bytes; // bytearray with (soon) source data
  static int endPos = 0;

  public static byte[] compress(byte[] input_bytes) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(outputStream);
    
    buffer = new byte[MAX_LOOKBACK];
    position = 0;
    endPos = 0;
    int last = 0;

    for (int i = 0; i < input_bytes.length; i++) { // iterate through the input data, byte by byte
      int posBuff = checkInBuffer(input_bytes[i], endPos); // check if the current byte is in the buffer
      if (posBuff == -1) { // if the byte is not in the buffer, it adds it to the buffer and increments the position
        addToBuffer(input_bytes[i]);
        position++;
      } else { // if it is in the buffer, tries to find the longest match
        int max = buildWord(posBuff, i, input_bytes);
        int maxIndex = posBuff;
        while (true) { // tries to find a longer match
          posBuff = checkInBuffer(input_bytes[i], posBuff - 1);
          if (posBuff == -1) break;
          if (buildWord(posBuff, i, input_bytes) > max) {
            max = buildWord(posBuff, i, input_bytes);
            maxIndex = posBuff;
          }
        }
        if (max > 6) {
          out.writeShort((short) (position - last));
          byte[] temp = new byte[position - last];
          int count = 0;
          for (int j = last; j < position; j++) temp[count++] = input_bytes[j];
          out.write(temp);
          out.writeShort((short) (-(i - maxIndex)));
          out.writeShort((short) max);

          for (int j = 0; j < max; j++) {
            addToBuffer(input_bytes[i++]);
            position++;
          }
          last = i;
          i--;
        } else {
          addToBuffer(input_bytes[i]);
          position++;
        }
      }
    }
    out.writeShort((short) (position - last)); // writes the length of the unmatched bytes
    for (int i = last; i < position; i++) out.writeByte(input_bytes[i]); // writes the unmatched bytes
    out.close();

    return outputStream.toByteArray();
  }

  public static byte[] decompress(byte[] data) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
    DataOutputStream out = new DataOutputStream(outputStream);
    position = 0;
    endPos = 0;
    buffer = new byte[MAX_LOOKBACK];
    short start = in.readShort();
    byte[] bytes = new byte[start];
    in.readFully(bytes);
    out.write(bytes);
    for (int j = 0; j < start; j++) {
      addToBuffer(bytes[j]);
      position++;
    }
    while (in.available() > 1) { // Important that there are at least 2 bytes left
      short back = in.readShort();
      short copyAmount = in.readShort();
      int end = endPos;
      bytes = new byte[copyAmount];
      int i = 0;
      for (int tempIndex = end + back; tempIndex < (end + back) + copyAmount; tempIndex++) {
        byte index = get(tempIndex);
        bytes[i++] = index;
        addToBuffer(index);
        position++;
      }
      out.write(bytes);
      start = in.readShort();
      bytes = new byte[start];
      in.readFully(bytes);
      for (int j = 0; j < start; j++) {
        addToBuffer(bytes[j]);
        position++;
      }
      out.write(bytes);
    }

    in.close();
    out.close();

    return outputStream.toByteArray();
  }

  // Adds a byte to the buffer, and updates end position
  private static void addToBuffer(byte b) {
    if (position >= buffer.length) {
      endPos = 0;
    }
    buffer[endPos] = b;
    endPos = (position + 1) % buffer.length;
  }

  // Retrieves a byte from the buffer
  private static byte get(int index) {
    if (index >= buffer.length) {
      int i = index % buffer.length;
      return buffer[i];
    } else if (index < 0) {
      int i = buffer.length + (index % buffer.length);
      if (i == buffer.length) i = 0;
      return buffer[i];
    }
    return buffer[index];
  }

  // Checks if byte is in buffer, from a given position
  private static int checkInBuffer(byte b, int pos) {
    for (int i = pos; i >= 0; i--) { // goes backwards from end-position
      if (buffer[i] == b) return i;
    }
    return -1;
  }

  // Finds the longest match of bytes, starting from a given position
  private static int buildWord(int posBuff, int posByte, byte[] input_bytes) {
    byte byte1 = input_bytes[posByte];
    byte buff1 = get(posBuff);
    int count = 0;
    while (buff1 == byte1 && posByte != input_bytes.length - 1) {
      count++;
      byte1 = input_bytes[++posByte];
      buff1 = get(++posBuff);
    }
    return count;
  }

  public static void main(String[] args) {
        try {
          byte[] input_bytes = Files.readAllBytes(Paths.get("diverse.lyx"));
          byte[] compressed_bytes = compress(input_bytes);
          FileOutputStream out = new FileOutputStream("diverse.hec");
          out.write(compressed_bytes);
          out.close();
  
          input_bytes = Files.readAllBytes(Paths.get("diverse.hec"));
          byte[] decompressed_bytes = decompress(input_bytes);
          FileOutputStream outDecomp = new FileOutputStream("løsning.lyx");
          outDecomp.write(decompressed_bytes);
          outDecomp.close();

        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}