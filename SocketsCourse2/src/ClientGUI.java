import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientGUI {
    private JFrame frame;
    private JComboBox<String> actionDropdown;
    private JPanel dynamicPanel;
    private JTextArea responseArea;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField schoolField;
    private JTextField semesterField;
    private JTextField passedCoursesField;
    private JTextField searchLastNameField;

    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private Socket socket;

    public ClientGUI() {
        frame = new JFrame("Client GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);

        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Action"
        ));
        actionDropdown = new JComboBox<>(new String[]{"Select Action", "Search", "Insert", "Bye"});
        actionDropdown.setPreferredSize(new Dimension(150, 25));
        topPanel.add(actionDropdown);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        dynamicPanel = new JPanel(new GridBagLayout());
        dynamicPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Action Details"
        ));
        mainPanel.add(dynamicPanel, BorderLayout.CENTER);

        responseArea = new JTextArea();
        responseArea.setRows(8);
        responseArea.setEditable(false);
        responseArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane responseScrollPane = new JScrollPane(responseArea);
        responseScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Server Responses"
        ));
        mainPanel.add(responseScrollPane, BorderLayout.SOUTH);

        frame.add(mainPanel);
        // Add a WindowListener to send "bye" when the frame is closed
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.out.println("window closing...");
                performBye(); // Send "bye" to the server on window close
            }
        });
        frame.setVisible(true);

        // The `::` operator in Java is called a **method reference**.
        // It provides a shorthand way of referencing a method or constructor without explicitly invoking it.
        // Instead of passing a `Runnable` as a lambda function or an anonymous class, you can use the method reference to pass the functionality directly.
        
        // 1. using a lambda function
        // new Thread(() -> initializeConnection()).start();

        // 2. using a Runnable interface
        // new Thread(new Runnable() {
        //     public void run() {
        //         initializeConnection();
        //     }
        // }).start();

        new Thread(this::initializeConnection).start();
       

        actionDropdown.addActionListener(e -> updateActionPanel());
    }

   
    private void initializeConnection() {
        try {
            socket = new Socket("localhost", 1235);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            showResponse("[INFO] Connected to the server.");
        } catch (IOException e) {
            showResponse("[ERROR] Unable to connect to the server.");
        }
    }

    private void closeResources() {
        try {
            if (bufferedWriter != null) bufferedWriter.close();
            if (bufferedReader != null) bufferedReader.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            showResponse("[ERROR] Error closing connections.");
        }
    }

    private void updateActionPanel() {
        dynamicPanel.removeAll();
        dynamicPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LINE_START;  // Add this line to align left
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        String selectedAction = (String) actionDropdown.getSelectedItem();

        switch (selectedAction) {
            case "Search":
                JLabel searchLabel = new JLabel("Enter Last Name:");
                searchLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                gbc.weightx = 0.0;
                dynamicPanel.add(searchLabel, gbc);

                searchLastNameField = new JTextField();
                searchLastNameField.setPreferredSize(new Dimension(150, 25));
                gbc.gridx = 1;
                gbc.weightx = 1.0;
                dynamicPanel.add(searchLastNameField, gbc);

                JButton searchButton = new JButton("Search");
                searchButton.setPreferredSize(new Dimension(100, 30));
                gbc.gridx = 2;
                gbc.gridy = 0;
                searchButton.addActionListener(e -> performSearch());
                dynamicPanel.add(searchButton, gbc);
                break;

            case "Insert":
                Dimension fieldSize = new Dimension(150, 25);
                Font labelFont = new Font("Dialog", Font.PLAIN, 12);

                firstNameField = new JTextField();
                lastNameField = new JTextField();
                schoolField = new JTextField();
                semesterField = new JTextField();
                passedCoursesField = new JTextField();

                firstNameField.setPreferredSize(fieldSize);
                lastNameField.setPreferredSize(fieldSize);
                schoolField.setPreferredSize(fieldSize);
                semesterField.setPreferredSize(fieldSize);
                passedCoursesField.setPreferredSize(fieldSize);

                addField(dynamicPanel, gbc, "First Name:", 0, firstNameField, labelFont);
                addField(dynamicPanel, gbc, "Last Name:", 1, lastNameField, labelFont);
                addField(dynamicPanel, gbc, "School:", 2, schoolField, labelFont);
                addField(dynamicPanel, gbc, "Semester:", 3, semesterField, labelFont);
                addField(dynamicPanel, gbc, "Passed Courses:", 4, passedCoursesField, labelFont);

                JButton insertButton = new JButton("Insert");
                insertButton.setPreferredSize(new Dimension(100, 30));
                gbc.gridx = 0;
                gbc.gridy = 5;
                gbc.gridwidth = 2;
                insertButton.addActionListener(e -> performInsert());
                dynamicPanel.add(insertButton, gbc);
                break;

            case "Bye":
                JButton byeButton = new JButton("Disconnect");
                byeButton.setPreferredSize(new Dimension(100, 30));
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 0.0;
                // gbc.gridwidth = 2;
                byeButton.addActionListener(e -> performBye());
                dynamicPanel.add(byeButton, gbc);
                break;

            default:
                JLabel defaultLabel = new JLabel("Please select an action from the dropdown.");
                defaultLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
                dynamicPanel.add(defaultLabel, gbc);
        }

        // It's a common pattern in Swing to call both methods together when making dynamic changes to the UI. 
        // Think of revalidate() as "recalculate the layout" and repaint() as "redraw everything with the new layout".
        dynamicPanel.revalidate();
        dynamicPanel.repaint();
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String labelText, int row, JTextField textField, Font labelFont) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        panel.add(textField, gbc);
    }

    private void performSearch() {
        try {
            String lastName = searchLastNameField.getText().trim();
            if (lastName.isEmpty()) {
                showResponse("[ERROR] Last name cannot be empty.");
                return;
            }
            sendRequest("search:" + lastName);
        } catch (Exception ex) {
            showResponse("[ERROR] Search failed.");
        }
    }

    private void performInsert() {
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String school = schoolField.getText().trim();
            int semester = Integer.parseInt(semesterField.getText().trim());
            int passedCourses = Integer.parseInt(passedCoursesField.getText().trim());

            sendRequest(String.format("insert:%s,%s,%s,%d,%d",
                    firstName, lastName, school, semester, passedCourses));
        } catch (NumberFormatException e) {
            showResponse("[ERROR] Semester and Passed Courses must be numbers.");
        } catch (Exception ex) {
            showResponse("[ERROR] Insertion failed.");
        }
    }

    private void performBye() {
        try {
            sendRequest("bye");
            closeResources();
            System.exit(0);
        } catch (Exception ex) {
            showResponse("[ERROR] Failed to disconnect.");
        }
    }

    private void sendRequest(String request) throws IOException {
        bufferedWriter.write(request);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        String response = bufferedReader.readLine();
        showResponse("[Server] " + response);
    }

    private void showResponse(String response) {
        responseArea.append(response + "\n");
    }

    public static void main(String[] args) {
        // SwingUtilities.invokeLater() is important in Swing applications:
        // 1) Thread Safety: Swing is not thread-safe. All Swing components should be created and modified on the Event Dispatch Thread (EDT). invokeLater() ensures that your GUI initialization code runs on the EDT.
        // 2) Preventing Race Conditions: Without invokeLater(), you might run into race conditions where GUI components are being accessed from multiple threads, leading to unpredictable behavior or crashes.
        SwingUtilities.invokeLater(() -> new ClientGUI());

        // The main thread is like a receptionist who handles incoming tasks
        // The EDT is like a specialized UI designer who knows how to properly set up and modify the GUI

        // invokeLater() is like the receptionist (main thread) handing over GUI-related tasks to the UI designer (EDT)


        //This is particularly important in your ClientGUI application because:

        // You're creating multiple Swing components
        // You're setting up listeners
        // You're managing a dynamic UI that changes based on user actions

    }

    /*
        -- BAD: Creating GUI directly on main thread
        public static void main(String[] args) {
            new ClientGUI();  // Could cause threading issues
        }

        --  GOOD: Creating GUI on Event Dispatch Thread
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> new ClientGUI());
        }
     */
}