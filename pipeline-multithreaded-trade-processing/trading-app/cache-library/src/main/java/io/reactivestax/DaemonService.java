package io.reactivestax;

public class DaemonService {

    public void spanDaemonThread(int evictionNumber) {
        for (int i = 0; i < evictionNumber; i++) {
            String threadName = "cleanupThread" + i;
            Thread cleanupThread = new Thread(() -> {
                while (true) {
                    try {
                        cleanupExpiredEntries();
                        Thread.sleep(2000); // Check every 2 seconds
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            cleanupThread.setDaemon(true);
            cleanupThread.start();
        }
    }

}
