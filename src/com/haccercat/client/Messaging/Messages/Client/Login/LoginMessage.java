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

        bytestream.writeInt(59);
        bytestream.writeInt(1);
        bytestream.writeInt(212);
        bytestream.writeString("86460ad83cb5e3a4ff8474eddb936d23008dfc1f");

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
