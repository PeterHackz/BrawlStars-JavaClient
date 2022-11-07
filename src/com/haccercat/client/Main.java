package com.haccercat.client;

import com.haccercat.client.TcpSocket.Connection;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("2 required args are missing: <domain> <port> <optional: dump>");
            System.exit(0);
        }
        int port = 0;
        try {
            port = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            System.out.printf("failed to parse port: %s\n", e.getMessage());
            System.exit(0);
        }
        Boolean shouldDump = false;
        if (args.length > 2) {
            shouldDump = args[2].equals("dump");
        }
        Connection connection = new Connection(args[0], port, shouldDump);
        connection.start();
        try {
            Thread.currentThread().join();
        } catch(InterruptedException e) {
            System.out.printf("[fatal-error] failed to make main thread run forever: %s\n", e.getMessage());
            System.exit(0);
        }
    }
}
