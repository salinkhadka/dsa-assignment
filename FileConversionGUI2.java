import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;


//question number 6
public class FileConversionGUI2 extends JFrame {

    private JButton btnSelectFiles;
    private JComboBox<String> cbFormat;
    private JButton btnStart;
    private JProgressBar progressBar;
    private JTextArea txtStatus;
    private JButton btnCancel;
    private JList<ImageIcon> lstResizedImages;
    private JList<String> lstConvertedDocs;

    private DefaultListModel<ImageIcon> resizedImagesModel;
    private DefaultListModel<String> convertedDocsModel;
    private List<File> filesToConvert;
    private ExecutorService executorService;
    private boolean isCancelled = false;

    public FileConversionGUI2() {
        setTitle("File Conversion Tool");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        // Panel for file selection
        JPanel fileSelectionPanel = new JPanel();
        fileSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        fileSelectionPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        btnSelectFiles = new JButton("Select Files");
        btnSelectFiles.setFont(new Font("Arial", Font.BOLD, 14));
        btnSelectFiles.setBackground(new Color(70, 130, 180));
        btnSelectFiles.setForeground(Color.WHITE);
        btnSelectFiles.setPreferredSize(new Dimension(150, 30));
        btnSelectFiles.addActionListener(e -> selectFiles());
        fileSelectionPanel.add(btnSelectFiles);

        String[] formats = {"PDF to Docx", "Image Resize"};
        cbFormat = new JComboBox<>(formats);
        cbFormat.setFont(new Font("Arial", Font.BOLD, 14));
        cbFormat.setPreferredSize(new Dimension(150, 30));
        fileSelectionPanel.add(cbFormat);

        btnStart = new JButton("Start");
        btnStart.setFont(new Font("Arial", Font.BOLD, 14));
        btnStart.setBackground(new Color(55, 50, 69));
        btnStart.setForeground(Color.WHITE);
        btnStart.setPreferredSize(new Dimension(100, 30));
        btnStart.addActionListener(e -> startConversion());
        fileSelectionPanel.add(btnStart);

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setBackground(new Color(150, 80, 120));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setPreferredSize(new Dimension(100, 30));
        btnCancel.addActionListener(e -> cancelConversion());
        fileSelectionPanel.add(btnCancel);

        add(fileSelectionPanel, BorderLayout.NORTH);

        // Center panel with progress bar and status text area
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(1150, 30));
        centerPanel.add(progressBar, BorderLayout.NORTH);

        txtStatus = new JTextArea(10, 30);
        txtStatus.setFont(new Font("Arial", Font.PLAIN, 14));
        txtStatus.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtStatus);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // List for resized images
        resizedImagesModel = new DefaultListModel<>();
        lstResizedImages = new JList<>(resizedImagesModel);
        lstResizedImages.setCellRenderer(new ImageCellRenderer());
        lstResizedImages.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = lstResizedImages.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        int choice = JOptionPane.showConfirmDialog(
                                FileConversionGUI2.this,
                                "Subscribe to unlock content?",
                                "Subscribe",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (choice == JOptionPane.YES_OPTION) {
                            File file = new File("converted_files", resizedImagesModel.getElementAt(index).getDescription());
                            openFile(file);
                        }
                    }
                }
            }
        });
        JScrollPane resizedImagesScrollPane = new JScrollPane(lstResizedImages);
        resizedImagesScrollPane.setBorder(BorderFactory.createTitledBorder("Resized Images"));
        resizedImagesScrollPane.setPreferredSize(new Dimension(580, 300));

        // List for converted documents
        convertedDocsModel = new DefaultListModel<>();
        lstConvertedDocs = new JList<>(convertedDocsModel);
        lstConvertedDocs.setFont(new Font("Arial", Font.PLAIN, 14));
        lstConvertedDocs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = lstConvertedDocs.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        int choice = JOptionPane.showConfirmDialog(
                                FileConversionGUI2.this,
                                "Subscribe to unlock content?",
                                "Subscribe",
                                JOptionPane.YES_NO_OPTION
                        );
                        if (choice == JOptionPane.YES_OPTION) {
                            File file = new File("converted_files", convertedDocsModel.getElementAt(index));
                            openFile(file);
                        }
                    }
                }
            }
        });
        JScrollPane convertedDocsScrollPane = new JScrollPane(lstConvertedDocs);
        convertedDocsScrollPane.setBorder(BorderFactory.createTitledBorder("Converted Documents"));
        convertedDocsScrollPane.setPreferredSize(new Dimension(580, 300));

        // Split pane for resized images and converted docs
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, resizedImagesScrollPane, convertedDocsScrollPane);
        splitPane.setDividerLocation(600); // Initial position of the divider
        add(splitPane, BorderLayout.SOUTH);
    }

    private void selectFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"), "Desktop"));
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter("All Files", "*"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            filesToConvert = List.of(fileChooser.getSelectedFiles());
            txtStatus.append("Selected files:\n");
            for (File file : filesToConvert) {
                txtStatus.append(file.getAbsolutePath() + "\n");
            }
        }
    }

    private void startConversion() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Subscribe to unlock content?",
                "Subscribe",
                JOptionPane.YES_NO_OPTION
        );
        if (choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Thank you for subscribing!", "Subscribed", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "You need to subscribe to access content.", "Subscription Required", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelConversion() {
        isCancelled = true;
        if (executorService != null) {
            executorService.shutdownNow();
        }
        txtStatus.append("Conversion cancelled.\n");
    }

    private class FileConversionTask extends SwingWorker<Void, String> {
        private final File file;
        private final String format;

        public FileConversionTask(File file, String format) {
            this.file = file;
            this.format = format;
        }

        @Override
        protected Void doInBackground() {
            try {
                if (isCancelled) return null;

                publish("Converting: " + file.getName());
                // Simulate file conversion with sleep
                Thread.sleep(2000);

                // Simulate file conversion and save the converted file
                String convertedFileName = simulateFileConversion(file, format);
                saveConvertedFile(file, convertedFileName);

                publish("Converted: " + convertedFileName);

                // Update progress bar
                int progress = (int) ((double) (filesToConvert.indexOf(file) + 1) / filesToConvert.size() * 100);
                setProgress(progress);
            } catch (InterruptedException | IOException e) {
                publish("Conversion failed for: " + file.getName());
            }
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            for (String chunk : chunks) {
                txtStatus.append(chunk + "\n");
            }
        }

        @Override
        protected void done() {
            txtStatus.append("Conversion completed.\n");
            progressBar.setValue(100);
        }

        private String simulateFileConversion(File file, String format) {
            // Implement file conversion logic
            // For demo purposes, return the original file name with an added suffix
            return file.getName().replaceFirst("[.][^.]+$", "") + "_converted." + format;
        }

        private void saveConvertedFile(File originalFile, String convertedFileName) throws IOException {
            // Implement file saving logic
            // For demo purposes, just copy the original file to a new location
            File convertedFile = new File("converted_files", convertedFileName);
            Files.copy(originalFile.toPath(), convertedFile.toPath());
        }
    }

    private void openFile(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            txtStatus.append("Unable to open file: " + file.getAbsolutePath() + "\n");
        }
    }

    private class ImageCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof ImageIcon) {
                ImageIcon icon = (ImageIcon) value;
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setIcon(icon);
                label.setText(""); // Clear text to only show the image
                return label;
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileConversionGUI2::new);
    }
}
