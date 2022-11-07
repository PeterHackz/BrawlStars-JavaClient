package com.haccercat.client.TcpSocket;

import java.io.IOException;
import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.io.FileOutputStream;
import java.util.HashMap;

import com.haccercat.client.Messaging.Messages.PiranhaMessage;
import com.haccercat.client.Messaging.LogicLaserMessageFactory;
import com.haccercat.client.Pepper.PepperCrypto;
import com.haccercat.client.Messaging.Messages.Client.Authentication.ClientHelloMessage;

class Dumper {
    public static HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    public static void prepare() {
        File dir = new File("PacketsDumps");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    public static void write(int type, byte[] data) {
        if (map.containsKey(type)) {
            map.put(type, map.get(type)+1);
        } else {
            map.put(type, 0);
        }
        try {
            FileOutputStream fos = new FileOutputStream("PacketsDumps/"+String.valueOf(type)+"-"+map.get(type).toString()+".bin");
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

public class Connection {

    private AsynchronousSocketChannel Channel;
    private String Domain;
    private int Port;
    public Boolean dump;
    public PepperCrypto Crypto;

    public Connection(String domain, int port, Boolean dump) {
        if (dump) {
            Dumper.prepare();
        }
        this.dump = dump;
        try {
            Channel = AsynchronousSocketChannel.open();
        } catch(IOException e) {
            System.out.printf("[fatal-error] failed to create async socket channel: %s\n", e.getMessage());
            System.exit(0);
        }
        Domain = domain;
        Port = port;
        Crypto = new PepperCrypto();
    }

    public void start() {
        Channel.connect(new InetSocketAddress(Domain, Port), Channel, new CompletionHandler < Void, AsynchronousSocketChannel > () {
            @Override
            public void completed(Void result, AsynchronousSocketChannel channel) {
                System.out.printf("succesfully connected to %s:%d\n", Domain, Port);
                nextHeader();
                sendPepperAuthentication();
            }
            @Override
            public void failed(Throwable err, AsynchronousSocketChannel channel) {
                System.out.printf("[fatal-error] failed to connect to %s:%d\n", Domain, Port);
                System.exit(0);
            }
        });
    }

    public void nextMessage(byte[] header) {
        int type = (((header[0] & 0xFF) << 8) | (header[1] & 0xFF));
        int length = (((header[2] & 0xFF) << 16) | ((header[3] & 0xFF) << 8) | (header[4] & 0xFF));
        int version = (((header[5] & 0xFF) << 8) | (header[6] & 0xFF));
        ByteBuffer buffer = ByteBuffer.allocate(length);
        Channel.read(buffer, Channel, new CompletionHandler < Integer, AsynchronousSocketChannel > () {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel channel) {
                if (result == -1) {
                    close();
                    System.out.println("[recv] server closed connection");
                } else {
                    onMessage(type, length, version, buffer.array());
                    nextHeader();
                }
            }
            @Override
            public void failed(Throwable err, AsynchronousSocketChannel channel) {
                System.out.println("failed to receive data from server");
                close();
            }
        });
    }

    public void nextHeader() {
        ByteBuffer buffer = ByteBuffer.allocate(7);
        Channel.read(buffer,
            Channel,
            new CompletionHandler < Integer,
            AsynchronousSocketChannel > () {
                @Override
                public void completed(Integer result, AsynchronousSocketChannel channel) {
                    if (result == -1) {
                        close();
                        System.out.println("[recv] server closed connection");
                    } else {
                        nextMessage(buffer.array());
                    }
                }
                @Override
                public void failed(Throwable err, AsynchronousSocketChannel channel) {
                    System.out.println("failed to receive data from server");
                    close();
                }
            });
    }

    public void onMessage(int type,
        int length,
        int version,
        byte[] payload) {
        System.out.printf("received message, type: %d, length: %d, version: %d, expected length: %d\n",
            type,
            payload.length,
            version,
            length);
        byte[] decrypted = Crypto.decrypt(type,
            payload);
            if (dump) {
                Dumper.write(type, decrypted);
            }
        PiranhaMessage message = LogicLaserMessageFactory.createMessageByType(type);
        if (message == null) {
            System.out.printf("ignoring unsupported message (%d)\n", type);
            return;
        }
        message.setBuffer(decrypted);
        message.decode();
        int[] messages = message.process();
        if (messages != null) {
            for (int msg: messages) {
                message = LogicLaserMessageFactory.createMessageByType(msg);
                message.encode();
                encryptAndSend(msg, 1, message.getBuffer());
            }
        }
    }

    public void encryptAndSend(int type, int version, byte[] payload) {
        byte[] encrypted = Crypto.encrypt(type, payload);
        int length = encrypted.length;
        byte[] header = new byte[7];
        header[0] = (byte)(type >> 8 & 0xFF);
        header[1] = (byte)(type & 0xFF);
        header[2] = (byte)(length >> 16 & 0xFF);
        header[3] = (byte)(length >> 8 & 0xFF);
        header[4] = (byte)(length & 0xFF);
        header[5] = (byte)(version >> 8 & 0xFF);
        header[6] = (byte)(version & 0xFF);
        ByteBuffer buf = ByteBuffer.allocate(7 + length);
        buf.put(header);
        buf.put(encrypted);
        buf.flip();
        sendMessage(buf,
            type);
    }

    public void sendMessage(ByteBuffer buffer,
        int type) {
        Channel.write(buffer,
            Channel,
            new CompletionHandler < Integer,
            AsynchronousSocketChannel > () {
                @Override
                public void completed(Integer result, AsynchronousSocketChannel channel) {
                    if (result == -1) {
                        System.out.println("[send] server close connection");
                    } else {
                        System.out.printf("message of type %d was sent\n", type);
                    }
                }
                @Override
                public void failed(Throwable err, AsynchronousSocketChannel channel) {
                    System.out.println("failed to send data to server");
                    close();
                }
            });
    }

    public void sendPepperAuthentication() {
        PiranhaMessage message = new ClientHelloMessage();
        message.setCapacity(100);
        message.encode();
        encryptAndSend(10100,
            0,
            message.getBuffer());
    }

    public void close() {
        try {
            Channel.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
