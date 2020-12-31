package ex3.ChatProgram;

public interface StringProducer {
    public void addConsumer(StringConsumer sc);
    public void removeConsumer(StringConsumer sc);
}
