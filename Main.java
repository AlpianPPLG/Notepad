package notepad;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

public class Main extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, formatMenu, viewMenu, helpMenu;
    private JMenuItem newItem, openItem, saveItem, exitItem;
    private JMenuItem cutItem, copyItem, pasteItem;
    private JMenuItem zoomInItem, zoomOutItem;
    private JMenuItem aboutItem;
    private JFileChooser fileChooser;
    private File currentFile;

    public Main() {
        setTitle("Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Buat komponen UI
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        // Buat menu bar
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Buat menu File
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        newItem = new JMenuItem("New");
        newItem.addActionListener(this);
        fileMenu.add(newItem);
        openItem = new JMenuItem("Open");
        openItem.addActionListener(this);
        fileMenu.add(openItem);
        saveItem = new JMenuItem("Save");
        saveItem.addActionListener(this);
        fileMenu.add(saveItem);
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);
        fileMenu.add(exitItem);

        // Buat menu Edit
        editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        cutItem = new JMenuItem(new DefaultEditorKit.CutAction());
        cutItem.setText("Cut");
        editMenu.add(cutItem);
        copyItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyItem.setText("Copy");
        editMenu.add(copyItem);
        pasteItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteItem.setText("Paste");
        editMenu.add(pasteItem);

        // Buat menu Format
        formatMenu = new JMenu("Format");
        menuBar.add(formatMenu);
        // Tidak ada item dalam menu Format

        // Buat menu View
        viewMenu = new JMenu("View");
        menuBar.add(viewMenu);
        zoomInItem = new JMenuItem("Zoom In");
        zoomInItem.addActionListener(this);
        viewMenu.add(zoomInItem);
        zoomOutItem = new JMenuItem("Zoom Out");
        zoomOutItem.addActionListener(this);
        viewMenu.add(zoomOutItem);

        // Buat menu Help
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);

        // Siapkan file chooser
        fileChooser = new JFileChooser();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newItem) {
            textArea.setText("");
            currentFile = null;
        } else if (e.getSource() == openItem) {
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                currentFile = fileChooser.getSelectedFile();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(currentFile));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    reader.close();
                    textArea.setText(sb.toString());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error opening file: " + ex.getMessage());
                }
            }
        } else if (e.getSource() == saveItem) {
            if (currentFile == null) {
                int result = fileChooser.showSaveDialog(this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    currentFile = fileChooser.getSelectedFile();
                } else {
                    return;
                }
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile));
                writer.write(textArea.getText());
                writer.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        } else if (e.getSource() == exitItem) {
            System.exit(0);
        } else if (e.getSource() == zoomInItem) {
            // Implement zoom in logic
            textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getSize() + 2f));
        } else if (e.getSource() == zoomOutItem) {
            // Implement zoom out logic
            textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getSize() - 2f));
        } else if (e.getSource() == aboutItem) {
            // Show About dialog
            JOptionPane.showMessageDialog(this, "Notepad\nVersion 1.0\nDeveloped by [Your Name]");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
