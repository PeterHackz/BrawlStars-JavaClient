package com.haccercat.client.Messaging.Messages;

import com.haccercat.client.DataStream.ByteStream;

public class PiranhaMessage {
    public ByteStream bytesream;
    
    public PiranhaMessage() {}
    
    public void setBuffer(byte[] buf) {
        bytesream = new ByteStream(buf);
    }
    
    public void setCapacity(int bufsize) {
        bytesream = new ByteStream(bufsize);
    }
    
    public byte[] getBuffer() {
        return bytesream.getBytes();
    }
    
    public ByteStream getByteStream() {
        return bytesream;
    }
    
    public int getEncodingLength() {
        return bytesream.offset;
    }
    
    public void encode() {}
    public void decode() {}
    public int[] process() {
        return null;
    }
  
}
