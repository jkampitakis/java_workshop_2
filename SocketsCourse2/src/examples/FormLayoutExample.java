package examples;

import javax.swing.*;
import java.awt.*;

public class FormLayoutExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Form Layout");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            // Common constraints
            gbc.insets = new Insets(5, 5, 5, 5); // Add padding
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Name label
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0.0;
            panel.add(new JLabel("Name:"), gbc);
            
            // Name field
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            panel.add(new JTextField(15), gbc);
            
            // Email label
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.weightx = 0.0;
            panel.add(new JLabel("Email:"), gbc);
            
            // Email field
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            panel.add(new JTextField(15), gbc);
            
            // Submit button spanning both columns
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            panel.add(new JButton("Submit"), gbc);
            
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}