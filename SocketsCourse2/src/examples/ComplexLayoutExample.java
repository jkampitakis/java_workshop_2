package examples;

import javax.swing.*;
import java.awt.*;

public class ComplexLayoutExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Complex Layout");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            // Common constraints
            gbc.insets = new Insets(5, 5, 5, 5);
            
            // Title spanning all columns
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            JLabel title = new JLabel("Dashboard", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 16));
            panel.add(title, gbc);
            
            // Reset gridwidth
            gbc.gridwidth = 1;
            
            // Three buttons in a row
            gbc.gridy = 1;
            gbc.weightx = 1.0;
            
            gbc.gridx = 0;
            panel.add(new JButton("Left"), gbc);
            
            gbc.gridx = 1;
            panel.add(new JButton("Middle"), gbc);
            
            gbc.gridx = 2;
            panel.add(new JButton("Right"), gbc);
            
            // Large text area spanning 3 columns
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 3;
            gbc.weighty = 1.0;
            gbc.fill = GridBagConstraints.BOTH;
            JTextArea textArea = new JTextArea();
            textArea.setRows(5);
            panel.add(new JScrollPane(textArea), gbc);
            
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}