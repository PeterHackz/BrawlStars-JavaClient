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
        bytestream.writeInt(211); // minor version 
        bytestream.writeString("db748fbfc2deb455586fc0ae3a7eea562eb4c0c9"); // master hash 
        bytestream.writeInt(0);
        bytestream.writeInt(0);
    }
}
