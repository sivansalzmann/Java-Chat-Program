package ex3.ChatProgram;

import java.io.IOException;
import java.util.List;

public class MessageBoard implements StringConsumer, StringProducer{
    private List<StringConsumer> proxies;

    @Override
    public void consume(String str) throws IOException {
        for(int i=0;i<proxies.size();i++) {
            proxies.get(i).consume(str);
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
