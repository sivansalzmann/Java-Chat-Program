package ex3.ChatProgram;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionProxy extends Thread implements StringConsumer, StringProducer{
    private Socket socket = null;
    private StringConsumer consumer;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    public ConnectionProxy(Socket socket) throws IOException {
        this.socket = socket;
        //dis - input stream
        //dos - output stream
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());

    }

    @Override
    public void consume(String str) throws IOException {
        dos.writeUTF(str);
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        consumer = sc;
    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        consumer = null;
    }

    //run still the socket is connected
    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                String readMsg = dis.readUTF();
                consumer.consume(readMsg);
            }
            this.removeConsumer(consumer);
        } catch (Exception e) {
        }
    }
}
