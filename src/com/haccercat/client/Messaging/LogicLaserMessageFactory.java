package com.haccercat.client.Messaging;

import com.haccercat.client.Messaging.Messages.PiranhaMessage;

import com.haccercat.client.Messaging.Messages.Server.Security.ServerHelloMessage;

import com.haccercat.client.Messaging.Messages.Client.Login.LoginMessage;

public class LogicLaserMessageFactory {
    public static PiranhaMessage createMessageByType(int type) {
        PiranhaMessage message = null;
        if (type == 20100) {
            message = new ServerHelloMessage();
        } else if (type == 10101) {
            message = new LoginMessage();
            message.setCapacity(250);
        }
        return message;
    }
}
