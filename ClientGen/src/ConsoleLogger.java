public class ConsoleLogger implements Logger {

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }
}
