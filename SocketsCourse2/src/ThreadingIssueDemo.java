import javax.swing.*;
import java.awt.*;

public class ThreadingIssueDemo {
    private JFrame frame;
    private JLabel statusLabel;

    public ThreadingIssueDemo() {
        // Simulate some initialization delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        frame = new JFrame("Threading Issue Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        statusLabel = new JLabel("Initializing...");
        frame.add(statusLabel, BorderLayout.CENTER);

        // Simulate background work that updates UI
        new Thread(() -> {
            try {
                Thread.sleep(500);
                // This is unsafe! Updating Swing components from a non-EDT thread
                statusLabel.setText("Updated from background thread!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // BAD: Creating GUI directly without SwingUtilities.invokeLater
        new ThreadingIssueDemo();

        // This can lead to:
        // 1. Race conditions
        // 2. Unpredictable UI behavior
        // 3. Potential exceptions
        // 4. Components not displaying correctly
    }
} 