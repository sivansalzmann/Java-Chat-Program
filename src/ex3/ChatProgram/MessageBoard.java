package ex3.ChatProgram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageBoard implements StringConsumer, StringProducer{
    private List<StringConsumer> proxies;

    public MessageBoard() {

        proxies = new ArrayList<>();
    }

    //set ArrayList of proxies
    @Override
    public void consume(String str) throws IOException {
        int i=0;
        try {
            for ( ; i < proxies.size(); i++) {
                proxies.get(i).consume(str);
            }
        }catch (IOException e) {
            removeConsumer(proxies.get((i)));
            i--;
        }

    }

    @Override
    public void addConsumer(StringConsumer sc) {
        proxies.add(sc);

    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        proxies.remove(sc);

    }
}
