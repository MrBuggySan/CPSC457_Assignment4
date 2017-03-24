public class PrintToScreen {
  public static void threadMessage(String threadName, String message) {
        System.out.format("%s: %s%n",
                          threadName,
                          message);
    }
}
