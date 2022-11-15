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
        bytestream.writeInt(209);
        bytestream.writeString("e13eb3b80ac96ef51c3baa7eb25064aadfe00fed");

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
