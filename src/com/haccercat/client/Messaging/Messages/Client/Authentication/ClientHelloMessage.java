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
        bytestream.writeInt(48); // crypto version
        bytestream.writeInt(59); // major version
        bytestream.writeInt(1); // build version
        bytestream.writeInt(212); // minor version 
        bytestream.writeString("86460ad83cb5e3a4ff8474eddb936d23008dfc1f"); // master hash 
        bytestream.writeInt(2);
        bytestream.writeInt(2);
    }
}
