package com.haccercat.client.Messaging.Messages.Server.Security;

import com.haccercat.client.Messaging.Messages.PiranhaMessage;
import com.haccercat.client.DataStream.ByteStream;

public class ServerHelloMessage extends PiranhaMessage {
    public ServerHelloMessage() {
        super();
    }
    public void decode() {
    }
    public int[] process() {
        int[] r = new int[1];
        r[0] = 10101;
        return r;
    }
}
