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

        bytestream.writeInt(47);
        bytestream.writeInt(1);
        bytestream.writeInt(236);
        bytestream.writeString("5613229054f0259cca492bc2e27164e4c5c2c914");

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
