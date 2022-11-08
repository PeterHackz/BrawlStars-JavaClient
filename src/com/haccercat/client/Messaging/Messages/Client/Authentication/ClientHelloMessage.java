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
        bytestream.writeInt(201); // minor version 
        bytestream.writeString("6ae6e058604fa57e250294c1660fa1e7cc728994"); // master hash 
        bytestream.writeInt(0);
        bytestream.writeInt(0);
    }
}
