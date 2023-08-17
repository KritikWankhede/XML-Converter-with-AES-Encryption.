package ProjectIntern;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class TextToXMLConverterGUI extends JFrame {

    private JTextArea textArea;
    private JButton btnConvert;
    private JButton btnEncrypt;
    private JButton btnDecrypt;

    private File inputFile;
    private File encryptedFile;
    private File decryptedFile;
    private String encryptionKey;

    public TextToXMLConverterGUI() {
        setTitle("XML Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JLabel lblDragDrop = new JLabel("Drag and drop text file here or click Here:");
        lblDragDrop.setHorizontalAlignment(SwingConstants.CENTER);
        lblDragDrop.setFont(new Font("Arial", Font.PLAIN, 14));
        lblDragDrop.setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    return true;
                }
                return false;
            }

            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }

                Transferable transferable = support.getTransferable();
                try {
                    @SuppressWarnings("unchecked")
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : files) {
                        if (file.isFile()) {
                            inputFile = file;
                            convertTextToXML(inputFile);
                            return true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

        lblDragDrop.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileChooser.setFileFilter(filter);

                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    inputFile = fileChooser.getSelectedFile();
                    convertTextToXML(inputFile);
                }
            }
        });

        contentPane.add(lblDragDrop, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        textArea = new JTextArea();
        scrollPane.setViewportView(textArea);

        JPanel buttonPanel = new JPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        btnConvert = new JButton("Convert to XML");
        btnConvert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textArea.getText();
                convertTextToXML(text);
            }
        });
        buttonPanel.add(btnConvert);

        btnEncrypt = new JButton("Encrypt XML");
        btnEncrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inputFile != null) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Save Encrypted XML");
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        encryptedFile = fileChooser.getSelectedFile();
                        encryptionKey = JOptionPane.showInputDialog("Enter encryption key:");
                        encryptXML();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a text file first.",
                            "No File Selected", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(btnEncrypt);

        btnDecrypt = new JButton("Decrypt XML");
        btnDecrypt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (encryptedFile != null) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Save Decrypted XML");
                    int result = fileChooser.showSaveDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        decryptedFile = fileChooser.getSelectedFile();
                        encryptionKey = JOptionPane.showInputDialog("Enter encryption key:");
                        decryptXML();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select an encrypted XML file first.",
                            "No File Selected", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(btnDecrypt);
    }

    private void convertTextToXML(File inputFile) {
        TextToXMLConverter converter = new TextToXMLConverter();
        converter.convertTextToXML(inputFile);
        if (converter.isConversionSuccessful()) {
            JOptionPane.showMessageDialog(this, "Text converted to XML successfully! Saved as: " +
                    converter.getOutputFile().getName(), "Conversion Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error converting text to XML. Please try again later.",
                    "Conversion Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void convertTextToXML(String text) {
        TextToXMLConverter converter = new TextToXMLConverter();
        converter.convertTextToXML(new File(text));
        if (converter.isConversionSuccessful()) {
            JOptionPane.showMessageDialog(this, "Text converted to XML successfully! Saved as: " +
                    converter.getOutputFile().getName(), "Conversion Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error converting text to XML. Please try again later.",
                    "Conversion Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void encryptXML() {
        XMLCipher xmlCipher = new XMLCipher();
        xmlCipher.encryptXML(inputFile, encryptedFile, encryptionKey);
        if (xmlCipher.isEncryptionSuccessful()) {
            JOptionPane.showMessageDialog(this, "XML encrypted successfully! Saved as: " +
                    encryptedFile.getName(), "Encryption Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error encrypting XML. Please try again later.",
                    "Encryption Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void decryptXML() {
        XMLCipher xmlCipher = new XMLCipher();
        xmlCipher.decryptXML(encryptedFile, decryptedFile, encryptionKey);
        if (xmlCipher.isDecryptionSuccessful()) {
            JOptionPane.showMessageDialog(this, "XML decrypted successfully! Saved as: " +
                    decryptedFile.getName(), "Decryption Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error decrypting XML. Please try again later.",
                    "Decryption Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    TextToXMLConverterGUI frame = new TextToXMLConverterGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
