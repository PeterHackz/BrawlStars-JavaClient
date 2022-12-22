package com.haccercat.client.Messaging.Messages.Client.Authentication;

import com.haccercat.client.Messaging.Messages.PiranhaMessage;
import com.haccercat.client.DataStream.ByteStream;

public class ClientHelloMessage extends PiranhaMessage {
    public ClientHelloMessage() {
        super();
    }
    public void encode() {
        ByteStream bytestream = getByteStream();
        bytestream.writeInt(2); // protocol version
        bytestream.writeInt(35); // crypto version
        bytestream.writeInt(47); // major version
        bytestream.writeInt(1); // build version
        bytestream.writeInt(236); // minor version 
        bytestream.writeString("5613229054f0259cca492bc2e27164e4c5c2c914"); // master hash 
        bytestream.writeInt(0);
        bytestream.writeInt(0);
    }
}
