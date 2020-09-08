package com.menazord.videograb.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.menazord.videograb.model.DownloadStatus;
import com.menazord.videograb.model.ProviderModel;
import com.menazord.videograb.model.Video;
import com.menazord.videograb.model.VideoFormat;
import com.menazord.videograb.providers.Provider;
import com.menazord.videograb.providers.VideoService;
import com.menazord.videograb.providers.VideoServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

@Slf4j
public class MainForm {

    private ApplicationContext applicationContext;

    private JComboBox listVideoProviders;
    private JTextField txtUrl;
    private JButton btnGetDetails;
    private JTextField txtTitle;
    private JComboBox listAVFormats;
    private JButton btnDownload;
    private JButton btnExit;
    private JButton btnReset;
    private JPanel mainPanel;
    private JButton btnSaveAs;
    private JTextField txtDestination;
    private JProgressBar progressBar;


    public void run() {

        JFrame frame = new JFrame("VideoGrab - Backup your videos!");
        frame.setContentPane(this.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        this.resetForm();

        this.progressBar.setStringPainted(true);

        this.btnExit.addActionListener(
                (ActionEvent event) -> {
                    System.exit(0);
                }
        );

        this.btnReset.addActionListener(
                (ActionEvent event) -> {
                    resetForm();
                }
        );

        this.btnGetDetails.addActionListener(
                (ActionEvent event) -> {
                    getVideoDetails();
                }
        );

        this.btnSaveAs.addActionListener(
                (ActionEvent event) -> {
                    selectDestination();
                }
        );

        this.btnDownload.addActionListener(
                (ActionEvent event) -> {
                    downloadFile();
                }
        );

        for (Provider p : Provider.values()) {
            ProviderModel item = new ProviderModel(p.getFriendlyName(), p.name());
            listVideoProviders.addItem(item);
        }

        frame.setVisible(true);
    }

    private void resetForm() {
        this.txtUrl.setText("");
        this.txtUrl.setEnabled(true);
        this.txtTitle.setText("");
        this.btnDownload.setEnabled(false);
        this.listAVFormats.removeAllItems();
        this.listAVFormats.setEnabled(false);
        this.btnSaveAs.setEnabled(false);
        this.progressBar.setValue(0);

    }

    private void getVideoDetails() {

        ProviderModel item = (ProviderModel) this.listVideoProviders.getSelectedItem();
        String providerKey = item.getKey();

        try {
            VideoService service = loadService(providerKey);
            Video video = service.getDetails(this.txtUrl.getText());

            this.txtTitle.setText(video.getTitle());

            for (VideoFormat videoFormat : video.getFormats()) {
                this.listAVFormats.addItem(videoFormat);
            }

            this.listAVFormats.setEnabled(true);
            this.btnDownload.setEnabled(true);
            this.btnSaveAs.setEnabled(true);
            this.txtUrl.setEnabled(false);

        } catch (VideoServiceException e) {
            log.error("Service error while getting details", e);
            showError("Service error while getting details. " + e.getMessage());
        }
    }

    private void selectDestination() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Elige un directorio para guardar el video: ");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
            this.txtDestination.setText(selectedFile.getAbsolutePath());
        }

    }

    private void downloadFile() {

        ProviderModel item = (ProviderModel) this.listVideoProviders.getSelectedItem();
        String providerKey = item.getKey();

        VideoFormat format = (VideoFormat) this.listAVFormats.getSelectedItem();

        String outputFile = this.txtDestination.getText();
        try {
            // Start the download and get the status poller
            VideoService service = loadService(providerKey);
            DownloadStatus status = service.downloadVideo(format, outputFile);

            // Start the progress bar task
            ProgressTask task = new ProgressTask(status);
            task.addPropertyChangeListener(evt -> {
                if ("progress".equals(evt.getPropertyName())) {
                    int progress = (Integer) evt.getNewValue();
                    progressBar.setValue(progress);

                    // Download completed, notify user
                    if (progress == 100) {
                        JOptionPane.showMessageDialog(this.mainPanel,
                                "El video se descargo exitosamente.",
                                "Descarga completada",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            task.execute();

        } catch (VideoServiceException e) {
            log.error("Service error while downloading video", e);
            showError("Service error while downloading video. " + e.getMessage());
        }
    }

    private VideoService loadService(String serviceName) {
        return (VideoService) applicationContext.getBean(serviceName);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this.mainPanel,
                message,
                "Ocurrio un error",
                JOptionPane.ERROR_MESSAGE);

    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("VideoGrab");
        mainPanel.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(9, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(500, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Origen");
        panel1.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        listVideoProviders = new JComboBox();
        panel1.add(listVideoProviders, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("URL");
        panel1.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtUrl = new JTextField();
        panel1.add(txtUrl, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnGetDetails = new JButton();
        btnGetDetails.setText("Obtener detalles");
        panel1.add(btnGetDetails, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Titulo");
        panel1.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTitle = new JTextField();
        txtTitle.setEditable(false);
        panel1.add(txtTitle, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        listAVFormats = new JComboBox();
        panel1.add(listAVFormats, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Formato de AV");
        panel1.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnDownload = new JButton();
        btnDownload.setText("Descargar video");
        panel1.add(btnDownload, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnExit = new JButton();
        btnExit.setText("Salir");
        panel1.add(btnExit, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnReset = new JButton();
        btnReset.setText("Comenzar de nuevo");
        panel1.add(btnReset, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 20), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Guardar como");
        panel1.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        txtDestination = new JTextField();
        txtDestination.setEditable(false);
        panel2.add(txtDestination, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnSaveAs = new JButton();
        btnSaveAs.setText("Seleccionar");
        panel2.add(btnSaveAs, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        progressBar = new JProgressBar();
        panel1.add(progressBar, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(50, -1), null, new Dimension(50, -1), 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanel.add(spacer3, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(50, -1), null, new Dimension(50, -1), 1, false));
        final Spacer spacer4 = new Spacer();
        mainPanel.add(spacer4, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 30), null, new Dimension(-1, 30), 0, false));
        final Spacer spacer5 = new Spacer();
        mainPanel.add(spacer5, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 30), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
