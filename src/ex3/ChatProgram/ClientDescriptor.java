package ex3.ChatProgram;

import java.io.IOException;


public class ClientDescriptor implements StringConsumer, StringProducer{
    private String name = "";
    StringConsumer consumer;

    public ClientDescriptor() {

    }

    //user join or left the chat
    @Override
    public void consume(String str) throws IOException {
        if(str.equals("disconnect")){
            consumer.consume(name + " has left the chat");
            removeConsumer(consumer);
        }
        else if (name.equals("")) {
            name = str;
            consumer.consume(name + " has joined the chat!");
        } else
            consumer.consume(name + ": " + str);

    }

    @Override
    public void addConsumer(StringConsumer sc) {
        consumer = sc;
    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        consumer = null;
    }
}
