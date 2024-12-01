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
    private JMenuItem aboutItem, themeItem;
    private JFileChooser fileChooser;
    private File currentFile;
    private JLabel statusLabel;
    private JEditorPane lineNumbering; // Pane untuk nomor baris
    private JPanel editorPanel; // Panel untuk menampung textarea dan line numbers

    public Main() {
        setTitle("Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Buat komponen UI
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        // Panel untuk menampung line numbers dan text area
        editorPanel = new JPanel(new BorderLayout());
        lineNumbering = new JEditorPane();
        lineNumbering.setEditable(false);
        lineNumbering.setBackground(Color.LIGHT_GRAY);
        lineNumbering.setPreferredSize(new Dimension(50, 0)); // Lebar tetap untuk nomor baris

        editorPanel.add(lineNumbering, BorderLayout.WEST);
        editorPanel.add(scrollPane, BorderLayout.CENTER);
        add(editorPanel, BorderLayout.CENTER);

        // Buat menu bar
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Buat menu File
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        newItem = new JMenuItem("New");
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK)); // Ctrl+N
        newItem.addActionListener(this);
        fileMenu.add(newItem);
        
        openItem = new JMenuItem("Open");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK)); // Ctrl+O
        openItem.addActionListener(this);
        fileMenu.add(openItem);
        
        saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)); // Ctrl+S
        saveItem.addActionListener(this);
        fileMenu.add(saveItem);
        
        exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK)); // Ctrl+Q
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
        
        // Buat menu View
        viewMenu = new JMenu("View");
        menuBar.add(viewMenu);
        zoomInItem = new JMenuItem("Zoom In");
        zoomInItem.addActionListener(this);
        viewMenu.add(zoomInItem);
        
        zoomOutItem = new JMenuItem("Zoom Out");
        zoomOutItem.addActionListener(this);
        viewMenu.add(zoomOutItem);
        
        themeItem = new JMenuItem("Toggle Theme");
        themeItem.addActionListener(this);
        viewMenu.add(themeItem);

        // Buat menu Help
        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);

        // Siapkan file chooser
        fileChooser = new JFileChooser();

        // Status Bar
        statusLabel = new JLabel("Status: Ready | Words: 0");
        add(statusLabel, BorderLayout.SOUTH);
        
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                updateStatus("Typing...");
                updateLineNumbers();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                updateStatus("Ready | Words: " + countWords(textArea.getText()));
                updateLineNumbers();
            }
        });
    }

    private void updateStatus(String message) {
        statusLabel.setText("Status: " + message);
    }

    private int countWords(String text) {
        String trimmedText = text.trim();
        if (trimmedText.isEmpty()) {
            return 0;
        }
        String[] words = trimmedText.split("\\s+");
        return words.length;
    }

    private void updateLineNumbers() {
        StringBuilder lineNumbers = new StringBuilder();
        int lineCount = textArea.getLineCount();
        for (int i = 1; i <= lineCount; i++) {
            lineNumbers.append(i).append("\n");
        }
        lineNumbering.setText(lineNumbers.toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newItem) {
            textArea.setText("");
            currentFile = null;
            updateStatus("New File Created | Words: 0");
            updateLineNumbers();
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
                    updateStatus("Opened: " + currentFile.getName() + " | Words: " + countWords(textArea.getText()));
                    updateLineNumbers();
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
                updateStatus("Saved: " + currentFile.getName() + " | Words: " + countWords(textArea.getText()));
                updateLineNumbers();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
            }
        } else if (e.getSource() == exitItem) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else if (e.getSource() == zoomInItem) {
            textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getSize() + 2f));
            updateStatus("Zoomed In | Words: " + countWords(textArea.getText()));
        } else if (e.getSource() == zoomOutItem) {
            textArea.setFont(textArea.getFont().deriveFont(textArea.getFont().getSize() - 2f));
            updateStatus("Zoomed Out | Words: " + countWords(textArea.getText()));
        } else if (e.getSource() == aboutItem) {
            JOptionPane.showMessageDialog(this, "Notepad\nVersion 1.0\nDeveloped by Alpian");
        } else if (e.getSource() == themeItem) {
            toggleTheme();
        }
    }

    private void toggleTheme() {
        Color background = textArea.getBackground();
        if (background.equals(Color.WHITE)) {
            textArea.setBackground(Color.DARK_GRAY);
            textArea.setForeground(Color.LIGHT_GRAY);
            lineNumbering.setBackground(Color.DARK_GRAY);
            updateStatus("Theme: Dark | Words: " + countWords(textArea.getText()));
        } else {
            textArea.setBackground(Color.WHITE);
            textArea.setForeground(Color.BLACK);
            lineNumbering.setBackground(Color.LIGHT_GRAY);
            updateStatus("Theme: Light | Words: " + countWords(textArea.getText()));
        }
        updateLineNumbers();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}