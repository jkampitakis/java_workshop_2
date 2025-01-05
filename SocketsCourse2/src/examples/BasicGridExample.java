package examples;

import javax.swing.*;
import java.awt.*;

public class BasicGridExample {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Basic GridBag 2x2");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 200);
            
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            // Add four buttons in a 2x2 grid
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            
            // Top-left button
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JButton("1"), gbc);
            
            // Top-right button
            gbc.gridx = 1;
            gbc.gridy = 0;
            panel.add(new JButton("2"), gbc);
            
            // Bottom-left button
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JButton("3"), gbc);
            
            // Bottom-right button
            gbc.gridx = 1;
            gbc.gridy = 1;
            panel.add(new JButton("4"), gbc);
            
            frame.add(panel);
            frame.setVisible(true);
        });

        /*
            FUN FACT:
                if we don't use SwingUtilities.invokeLater and the order we place the components is changed to 
                
                .......
                    frame.setVisible(true);
                    frame.add(panel);
                .....

                we will not see the panel in the window!!
        */

    }
} 