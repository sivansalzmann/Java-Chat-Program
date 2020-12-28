package ex3.ChatProgram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientDescriptor implements StringConsumer, StringProducer{
    private String name = "";
    private List<StringConsumer> consumers;

    public ClientDescriptor() {
        consumers = new ArrayList<>();
    }

    @Override
    public void consume(String str) throws IOException {
        if(name == "") {
            name = str;

            for(StringConsumer consumer : consumers) {
                consumer.consume(name + "joined to the chat (:");
            }
        }
        else {
            for (StringConsumer consumer : consumers) {
                consumer.consume(name +"message:" + str);
            }
        }

    }

    @Override
    public void addConsumer(StringConsumer sc) {
        consumers.add(sc);
    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        consumers.remove(sc);
    }
}
