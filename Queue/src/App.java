import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;


public class App {
    private final Queue<Student> queue = new LinkedList<>();

    private void createAndShowGUI() {
        JFrame frame = new JFrame("FairSort Registration System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Input area
        JPanel input = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField nameField = new JTextField(15);
        JTextField idField = new JTextField(10);
        JButton addBtn = new JButton("Add Student to Queue");
        input.add(new JLabel("Name:"));
        input.add(nameField);
        input.add(new JLabel("ID:"));
        input.add(idField);
        input.add(addBtn);

    // Controls
    JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JButton processBtn = new JButton("Process Next Student");
    JButton viewBtn = new JButton("View Current Queue");
    JButton sampleBtn = new JButton("Add Sample Data");
    JButton clearBtn = new JButton("Clear Queue");
    JButton saveBtn = new JButton("Save Queue");
    JButton loadBtn = new JButton("Load Queue");
    JButton exportBtn = new JButton("Export CSV");
    controls.add(processBtn);
    controls.add(viewBtn);
    controls.add(sampleBtn);
    controls.add(clearBtn);
    controls.add(saveBtn);
    controls.add(loadBtn);
    controls.add(exportBtn);

        // Display area
        JTextArea display = new JTextArea();
        display.setEditable(false);
        JScrollPane scroll = new JScrollPane(display);

        panel.add(input, BorderLayout.NORTH);
        panel.add(controls, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        // Actions
        addBtn.addActionListener((ActionEvent e) -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            if (name.isEmpty() || id.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter both name and ID.", "Input required", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Student s = new Student(name, id);
            queue.add(s);
            display.append("Enqueued: " + s + "\n");
            nameField.setText("");
            idField.setText("");
        });

        processBtn.addActionListener((ActionEvent e) -> {
            Student next = queue.poll();
            if (next == null) {
                JOptionPane.showMessageDialog(frame, "No students in queue.", "Queue empty", JOptionPane.INFORMATION_MESSAGE);
            } else {
                display.append("Now serving: " + next + "\n");
            }
        });

        // Add some sample entries for demo
        sampleBtn.addActionListener((ActionEvent e) -> {
            Student s1 = new Student("Maria Garcia", "24-00042");
            Student s2 = new Student("John Doe", "24-00155");
            Student s3 = new Student("Alice Albright", "24-00001");
            queue.add(s1);
            queue.add(s2);
            queue.add(s3);
            display.append("Enqueued sample: " + s1 + "\n");
            display.append("Enqueued sample: " + s2 + "\n");
            display.append("Enqueued sample: " + s3 + "\n");
        });

        // Clear queue
        clearBtn.addActionListener((ActionEvent e) -> {
            queue.clear();
            display.append("Queue cleared.\n");
        });

        // Save queue to a simple data file (one line per student: name|id|arrivalMillis)
        saveBtn.addActionListener((ActionEvent e) -> {
            Path p = Paths.get("queue_data.txt");
            try (BufferedWriter w = Files.newBufferedWriter(p)) {
                for (Student s : queue) {
                    w.write(s.toDataLine());
                    w.newLine();
                }
                display.append("Queue saved to " + p.toAbsolutePath() + "\n");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Failed to save: " + ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Load queue from the data file (appends to current queue)
        loadBtn.addActionListener((ActionEvent e) -> {
            Path p = Paths.get("queue_data.txt");
            if (!Files.exists(p)) {
                JOptionPane.showMessageDialog(frame, "No saved queue file found at " + p.toAbsolutePath(), "File not found", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            try (BufferedReader r = Files.newBufferedReader(p)) {
                String line;
                int count = 0;
                while ((line = r.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length >= 3) {
                        String name = parts[0];
                        String id = parts[1];
                        long millis = Long.parseLong(parts[2]);
                        Student s = new Student(name, id, millis);
                        queue.add(s);
                        display.append("Loaded: " + s + "\n");
                        count++;
                    }
                }
                display.append("Loaded " + count + " entries from " + p.toAbsolutePath() + "\n");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Failed to load: " + ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Export to CSV (Name,ID,Arrival)
        exportBtn.addActionListener((ActionEvent e) -> {
            Path p = Paths.get("queue_export.csv");
            try (BufferedWriter w = Files.newBufferedWriter(p)) {
                w.write("Name,ID,Arrival\n");
                for (Student s : queue) {
                    // Escape any commas by wrapping in quotes if needed
                    String name = s.getName();
                    String id = s.getId();
                    String arrival = s.getFormattedArrival();
                    // simple CSV: wrap name if contains comma
                    if (name.contains(",")) name = '"' + name + '"';
                    w.write(name + "," + id + "," + arrival + "\n");
                }
                display.append("Exported CSV to " + p.toAbsolutePath() + "\n");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Failed to export: " + ex.getMessage(), "IO Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viewBtn.addActionListener((ActionEvent e) -> {
            if (queue.isEmpty()) {
                display.append("Queue is empty.\n");
                return;
            }
            display.append("Current queue (front -> back):\n");
            for (Student s : queue) {
                display.append("  " + s + "\n");
            }
        });

        frame.getContentPane().add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().createAndShowGUI());
    }
}
