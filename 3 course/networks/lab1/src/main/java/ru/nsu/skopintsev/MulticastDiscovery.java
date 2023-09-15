package ru.nsu.skopintsev;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MulticastDiscovery {
    private final String multicastGroupAddress;
    private final int multicastPort;
    private final MudakTable mudakTable;
    private final DatagramSocket senderSocket;
    private ScheduledExecutorService scheduler;

    public MulticastDiscovery(String multicastGroupAddress, int multicastPort) {
        this.multicastGroupAddress = multicastGroupAddress;
        this.multicastPort = multicastPort;

        this.mudakTable = new MudakTable();

        try {
            this.senderSocket = new DatagramSocket(); // Инициализация сокса для отправки
        } catch (SocketException e) {
            System.err.println("Create datagram socket failed " + e.getMessage());
            throw new RuntimeException("Failed to create DatagramSocket", e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Sending LEAVE command before exiting...");
            sendCommand(MessageType.LEAVE);
        }));

        setScheduler();
    }

    public void start() {
        try (MulticastSocket receiveSocket = new MulticastSocket(multicastPort)) {
            InetAddress group = InetAddress.getByName(multicastGroupAddress);
            receiveSocket.joinGroup(group);

            receiverMulticast(receiveSocket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiverMulticast(MulticastSocket receiveSocket) {
        byte[] buffer = new byte[1];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            try {
                receiveSocket.receive(packet);
                byte receivedMessageType = packet.getData()[0];
                String user = packet.getAddress().getHostAddress() + ":" + packet.getPort();
                MessageType messageType = MessageType.fromValue(receivedMessageType);

                if (messageType != null) {
                    switch (messageType) {
                        case REPORT -> {
                            System.out.println("Received MUDAK Report message from " + user);
                            mudakTable.updateEntry(user);
                        }
                        case LEAVE -> {
                            System.err.println("Received MUDAK Leave message from " + user);
                            mudakTable.removeEntry(user);
                        }
                        default -> System.out.println("Received unknown message from " + user);
                    }
                }
                mudakTable.killZombie();
            } catch (IOException e) {
                System.err.println("receiveMulticast error: " + e.getMessage());
            }
        }
    }

    private void sendMulticast() {
        sendCommand(MessageType.REPORT);
    }

    public void sendCommand(@NotNull MessageType commandType) {
        try {
            InetAddress group = InetAddress.getByName(multicastGroupAddress);
            byte[] message = { (byte) commandType.getValue() };
            DatagramPacket packet = new DatagramPacket(message, message.length, group, multicastPort);
            senderSocket.send(packet);
        } catch (IOException e) {
            System.err.println("sendCommand error: " + e.getMessage());
        }
    }

    private void setScheduler() {
        scheduler = Executors.newScheduledThreadPool(2);

        scheduler.scheduleAtFixedRate(this::printTable, 1000, 900, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(this::sendMulticast, 1, 10, TimeUnit.SECONDS);
    }

    private void printTable() {
        mudakTable.printTable();
    }
}
