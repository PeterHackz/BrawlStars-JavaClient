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
        bytestream.writeInt(34); // crypto version
        bytestream.writeInt(46); // major version
        bytestream.writeInt(1); // build version
        bytestream.writeInt(190); // minor version 
        bytestream.writeString("26d9b4db26f9b827da2ddf162779d6d96c18ee36"); // master hash 
        bytestream.writeInt(0);
        bytestream.writeInt(0);
    }
}
