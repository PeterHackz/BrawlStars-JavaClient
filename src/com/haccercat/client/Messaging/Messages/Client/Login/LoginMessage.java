package com.haccercat.client.Messaging.Messages.Client.Login;

import com.haccercat.client.Messaging.Messages.PiranhaMessage;
import com.haccercat.client.DataStream.ByteStream;

public class LoginMessage extends PiranhaMessage {
    public LoginMessage() {
        super();
    }
    public void encode() {
        ByteStream bytestream = getByteStream();
        bytestream.writeInt(0); // high
        bytestream.writeInt(0); // low
        bytestream.writeString(); // token

        bytestream.writeInt(46);
        bytestream.writeInt(1);
        bytestream.writeInt(201);
        bytestream.writeString("6ae6e058604fa57e250294c1660fa1e7cc728994");

        bytestream.writeString();
        bytestream.writeDataReference(1, 0);
        bytestream.writeString("en-US");
        bytestream.writeString();
        bytestream.writeBoolean(false);
        bytestream.writeString();
        bytestream.writeString();
        bytestream.writeBoolean(true);
        bytestream.writeString();
        bytestream.writeInt(1448);
        bytestream.writeVInt(0);
        bytestream.writeString();

        bytestream.writeString();
        bytestream.writeString();
        bytestream.writeVInt(0);

        bytestream.writeString();
        bytestream.writeString();
        bytestream.writeString();

        bytestream.writeString();

        bytestream.writeBoolean(false);
        bytestream.writeString();
        bytestream.writeString();
    }
}
