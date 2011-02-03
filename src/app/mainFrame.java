/*
 * mainFrame.java
 *
 * Created on 23/Jan/2011, 11:00:13
 */
package app;

import com.icdif.audio.analysis.PeakDetector;
import com.icdif.audio.analysis.SamplesReader;
import com.icdif.audio.analysis.SpectralDifference;
import com.icdif.audio.io.AudioDecoder;
import com.icdif.audio.io.MP3Decoder;
import com.icdif.audio.io.WaveDecoder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

/**
 *
 * @author wanderer
 */
public class mainFrame extends javax.swing.JFrame{

    private ArrayList<AnalysisContent> contents = new ArrayList<AnalysisContent>();
    private final int WINDOWSIZE_DEFAULT = 1024;
    private final int HOPSIZE_DEFAULT = 512;
    private PlotTask plotTask;
    private importTask task;
    private ProgressMonitor progressMonitor;
    ExtensionFileFilter filterSound = new ExtensionFileFilter("Wave and MP3", new String[]{"MP3", "WAV", "WAVE"});
    ExtensionFileFilter filterProject = new ExtensionFileFilter("SoundIt", new String[]{"SOUNDIT"});

    /** Creates new form mainFrame */
    public mainFrame() {

        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogAbout = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButtonClose = new javax.swing.JButton();
        jFileChooserImportAudio = new javax.swing.JFileChooser();
        jFileChooserProject = new javax.swing.JFileChooser();
        jToolBar1 = new javax.swing.JToolBar();
        jComboBox1 = new javax.swing.JComboBox();
        jButtonPlot = new javax.swing.JButton();
        jButtonPlotCalc = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jProgressBar1 = new javax.swing.JProgressBar();
        jMenuBarTop = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuItemOpenProject = new javax.swing.JMenuItem();
        jMenuItemSave = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItemExit = new javax.swing.JMenuItem();
        jMenuImport = new javax.swing.JMenu();
        jMenuItemImportAudio = new javax.swing.JMenuItem();
        jMenuOptions = new javax.swing.JMenu();
        jMenuItemConfigure = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuItemHelp = new javax.swing.JMenuItem();
        jMenuItemAbout = new javax.swing.JMenuItem();

        jDialogAbout.setTitle("About SoundIT");

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 18));
        jLabel1.setText("About SoundIT");

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 0, 12));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("<html>SoundIT is a program built using the java programming language and intended to provide several functions related to audio.<br/> It allows one to process the text data from different Soundmeters (NA27).<br/> It also allows one to read and process data from audio files (.wav and .mp3). From this streams, it can plot the values in the time domain (amplitude vs time), but it can also find and plot onsets in the audio, by using the Fourier Transform and other processing methods in the frequency domain. <br/><br/>  More info at:<br/> http://icdif.com/computing/soundit <br/> and<br/>https://github.com/Shemahmforash/SoundIt");
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButtonClose.setText("Close");
        jButtonClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogAboutLayout = new javax.swing.GroupLayout(jDialogAbout.getContentPane());
        jDialogAbout.getContentPane().setLayout(jDialogAboutLayout);
        jDialogAboutLayout.setHorizontalGroup(
            jDialogAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAboutLayout.createSequentialGroup()
                .addGroup(jDialogAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialogAboutLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jDialogAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                            .addComponent(jLabel1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogAboutLayout.createSequentialGroup()
                        .addContainerGap(433, Short.MAX_VALUE)
                        .addComponent(jButtonClose)))
                .addContainerGap())
        );
        jDialogAboutLayout.setVerticalGroup(
            jDialogAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAboutLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonClose)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jFileChooserProject.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Beat Detector");
        setName("mainFrame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jToolBar1.add(jComboBox1);

        jButtonPlot.setText("Plot Samples");
        jButtonPlot.setFocusable(false);
        jButtonPlot.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonPlot.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonPlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlotActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonPlot);

        jButtonPlotCalc.setText("Plot Calculations");
        jButtonPlotCalc.setFocusable(false);
        jButtonPlotCalc.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonPlotCalc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonPlotCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlotCalcActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonPlotCalc);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setFocusable(false);
        jToolBar2.setName("BottomToolBar"); // NOI18N

        jProgressBar1.setMaximumSize(new java.awt.Dimension(150, 20));
        jToolBar2.add(jProgressBar1);

        jMenuFile.setText("File");

        jMenuItemOpenProject.setText("Open Project");
        jMenuItemOpenProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemOpenProjectActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemOpenProject);

        jMenuItemSave.setText("Save Project");
        jMenuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemSave);
        jMenuFile.add(jSeparator1);

        jMenuItemExit.setText("Exit");
        jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExitActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuItemExit);

        jMenuBarTop.add(jMenuFile);

        jMenuImport.setText("Import");

        jMenuItemImportAudio.setText("Import Audio");
        jMenuItemImportAudio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemImportAudioActionPerformed(evt);
            }
        });
        jMenuImport.add(jMenuItemImportAudio);

        jMenuBarTop.add(jMenuImport);

        jMenuOptions.setText("Options");

        jMenuItemConfigure.setText("Configure");
        jMenuOptions.add(jMenuItemConfigure);

        jMenuBarTop.add(jMenuOptions);

        jMenuHelp.setText("Help");

        jMenuItemHelp.setText("Help");
        jMenuHelp.add(jMenuItemHelp);

        jMenuItemAbout.setText("About");
        jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAboutActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuItemAbout);

        jMenuBarTop.add(jMenuHelp);

        setJMenuBar(jMenuBarTop);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 376, Short.MAX_VALUE)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Exits the program by clicking in the menu button
     * @param evt
     */
    private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExitActionPerformed
        this.formWindowClosing(null);
    }//GEN-LAST:event_jMenuItemExitActionPerformed

    /**
     * Imports the audio from a file into an new entry in the contents arraylist
     * @param evt
     */
    private void jMenuItemImportAudioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemImportAudioActionPerformed
        try {

            jFileChooserImportAudio.setFileFilter(filterSound);

            final int returnVal = jFileChooserImportAudio.showOpenDialog(this);

            if (returnVal == jFileChooserImportAudio.APPROVE_OPTION) {

                File file = jFileChooserImportAudio.getSelectedFile();

                if (!comboContains(jComboBox1, file.getName())) {
                    System.out.println("File: " + file.getAbsolutePath());


                    jComboBox1.addItem(file.getName());

                    progressMonitor = new ProgressMonitor(this,
                            "Importing " + file.getName(),
                            "", 0, 100);
                    progressMonitor.setProgress(0);

                    task = new importTask(file);
                    task.addPropertyChangeListener(propertyChangeListenerImport);
                    task.execute();
                } else {
                    //System.out.println("The file " + file.getName() + " was already processed");

                    JOptionPane.showMessageDialog(this, "The file " + file.getName() + " was already processed");
                }


            }

        } catch (Exception ex) {
            Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItemImportAudioActionPerformed

    /**
     * Saves the contents arraylist to a file
     * @param evt
     */
    private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveActionPerformed

        if (contents.size() > 0) {
            jFileChooserProject.setFileFilter(filterProject);

            int returnVal = jFileChooserProject.showSaveDialog(this);
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            if (returnVal == jFileChooserProject.APPROVE_OPTION) {
                File file = jFileChooserProject.getSelectedFile();

                if (!file.exists()) {
                    try {

                        file.createNewFile();
                        System.out.println("New file " + file.getAbsolutePath() + " has been created to the current directory");
                    } catch (IOException ex) {
                        Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                System.out.println("Faço save de " + file.getAbsolutePath());

                /*try {
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(contents);

                JOptionPane.showMessageDialog(this, "The contents of this project were saved successfully to " + file.getName());
                } catch (FileNotFoundException e) {
                e.printStackTrace();
                } catch (IOException e) {
                e.printStackTrace();
                }*/

                SaveProjectTask saveProjectTask = new SaveProjectTask(file);
                saveProjectTask.execute();

                //JOptionPane.showMessageDialog(this, "The contents of this project were saved successfully to " + file.getName());

            }

            setCursor(null); //turn off the wait cursor
        } else {
            JOptionPane.showMessageDialog(this, "You can't save an empty project. First you must import audio.", "No File Chosen", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_jMenuItemSaveActionPerformed

    /**
     * In response to a click in the menu, it open the AboutBox dialog
     * @param evt
     */
    private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAboutActionPerformed
        jDialogAbout.setBounds(0, 0, 400, 400);
        jDialogAbout.setVisible(true);

    }//GEN-LAST:event_jMenuItemAboutActionPerformed

    /**
     * In response to the close button in the AboutBox Dialog, it closes the AboutBox
     * @param evt
     */
    private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseActionPerformed
        jDialogAbout.dispose();
    }//GEN-LAST:event_jButtonCloseActionPerformed

    /**
     * This method plots the samples in the file selected in the combobox.
     * This samples are too big to be saved in the program, so they are read from the file
     * @param evt
     */
    private void jButtonPlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlotActionPerformed
        //jButtonPlot.setEnabled(false);


        if (jComboBox1.getSelectedIndex() >= 0) {            

            // find the selected index of the comboBox. Since the combobox is filled
            // at the same order as the contents array list, the indexes are the same
            int selectedIndex = jComboBox1.getSelectedIndex();

            String fileName = contents.get(selectedIndex).getFile().getAbsolutePath();

            String extension = FileUtils.getExtension(fileName);

            progressMonitor = new ProgressMonitor(this,
                    "Plotting " + contents.get(selectedIndex).getFile().getName(),
                    "", 0, 100);
            progressMonitor.setProgress(0);

            progressMonitor.setNote("Plotting");

            System.out.println("Invoquei o prog monitor");

            //System.out.println("Extension = " + extension);

            AudioDecoder decoder = null;

            try {
                if (extension.equals(".mp3")) {
                    decoder = new MP3Decoder(new FileInputStream(fileName));
                } else if (extension.equals(".wav") || extension.equals(".wave")) {
                    decoder = new WaveDecoder(new FileInputStream(fileName));
                }
            } catch (Exception e) {
            }

            //the samples are too big to be kept in the clas methods, so they are read from the file
            ArrayList<Float> samplesFile = new ArrayList<Float>();

            SamplesReader samplesReader = new SamplesReader(decoder, WINDOWSIZE_DEFAULT);

            samplesFile = samplesReader.getAllSamples();

            //Instances of javax.swing.SwingWorker are not reusuable, so
            //we create new instances as needed.
            plotTask = new PlotTask(samplesFile, "PCM DATA - " + fileName, 800, 600, WINDOWSIZE_DEFAULT, Color.RED, fileName, true);

            plotTask.addPropertyChangeListener(this.propertyChangeListenerPlot);
            plotTask.execute();

            //I clean the variables
            decoder = null;
            //samplesFile.clear();
        } else if (contents.size() > 0) {
            JOptionPane.showMessageDialog(this, "You need to choose the file to plot from the Combo Box", "No File Chosen", JOptionPane.WARNING_MESSAGE);
        } else if (contents.size() == 0) {
            JOptionPane.showMessageDialog(this, "In order to plot, you first need to import audio to the programa", "You need to import audio first", JOptionPane.WARNING_MESSAGE);
        }


    }//GEN-LAST:event_jButtonPlotActionPerformed

    /**
     * This method gets the saved data from a file and sets the arraylist contents with it
     * @param evt
     */
    private void jMenuItemOpenProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemOpenProjectActionPerformed
        try {

            jFileChooserProject.setFileFilter(filterProject);

            int returnVal = jFileChooserProject.showOpenDialog(this);

            if (returnVal == jFileChooserProject.APPROVE_OPTION) {
                File file = jFileChooserProject.getSelectedFile();
                if (file.exists()) {

                    //this varibale keeps the response from the confirm Dialog (responses: 1,0,-1)
                    int response = JOptionPane.YES_OPTION;

                    if (contents.size() > 0) {

                        //default icon, custom title
                        response = JOptionPane.showConfirmDialog(
                                this,
                                "Do you wish to import a new project? You have an unsaved project. If you choose to import a new project, the current project will be lost.",
                                "Confirm project opening",
                                JOptionPane.YES_NO_OPTION);

                        //System.out.println("Resposta: " + response);

                    }
                    //if the response is yes, I clear the arraylist contents and the combo and execute the task of project opening
                    if ((response == JOptionPane.YES_OPTION)) {
                        jComboBox1.removeAllItems();
                        contents.clear();
                        OpenProjectTask openTask = new OpenProjectTask(file);
                        openTask.execute();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItemOpenProjectActionPerformed

    private void jButtonPlotCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlotCalcActionPerformed
        if (jComboBox1.getSelectedIndex() >= 0) {
            progressMonitor = new ProgressMonitor(this,
                    "Plotting",
                    "", 0, 100);
            progressMonitor.setProgress(0);

            progressMonitor.setNote("Plotting");

            System.out.println("Invoquei o prog monitor");

            // find the selected index of the comboBox. Since the combobox is filled
            // at the same order as the contents array list, the indexes are the same
            int selectedIndex = jComboBox1.getSelectedIndex();

            String fileName = contents.get(selectedIndex).getFile().getAbsolutePath();

            String extension = FileUtils.getExtension(fileName);

            //System.out.println("Extension = " + extension);

            AudioDecoder decoder = null;

            try {
                if (extension.equals(".mp3")) {
                    decoder = new MP3Decoder(new FileInputStream(fileName));
                } else if (extension.equals(".wav") || extension.equals(".wave")) {
                    decoder = new WaveDecoder(new FileInputStream(fileName));
                }
            } catch (Exception e) {
            }

            /*SpectralDifference spectDiff = new SpectralDifference(decoder, WINDOWSIZE_DEFAULT,
            HOPSIZE_DEFAULT, true, 44100);

            //Instances of javax.swing.SwingWorker are not reusuable, so
            //we create new instances as needed.
            plotTask = new PlotTask(spectDiff.getSpectralDifference(), "Spectral Dif - " + fileName, 800, 600, 1, Color.RED, fileName, true, HOPSIZE_DEFAULT);*/


            //plotTask = new PlotTask(contents.get(selectedIndex).getSpectralFlux(), "Spectral Dif - " + fileName, 800, 600, 1, Color.RED, fileName, true, HOPSIZE_DEFAULT);
            plotTask = new PlotTask(contents.get(selectedIndex).getPeaks(), "Peaks - " + fileName, 800, 600, 1, Color.RED, fileName, true, HOPSIZE_DEFAULT);
            plotTask.addPropertyChangeListener(this.propertyChangeListenerPlot);
            plotTask.execute();
        } else if (contents.size() > 0) {
            JOptionPane.showMessageDialog(this, "You need to choose the file to plot from the Combo Box", "No File Chosen", JOptionPane.WARNING_MESSAGE);
        } else if (contents.size() == 0) {
            JOptionPane.showMessageDialog(this, "In order to plot, you first need to import audio to the program.", "You need to import audio first", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButtonPlotCalcActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
         //Display confirm dialog
        int confirmed = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to quit?", "Confirm Quit",
                JOptionPane.YES_NO_OPTION);

        //Close if user confirmed
        if (confirmed == JOptionPane.YES_OPTION) {
                //Close frame
                this.dispose();
        }
    }//GEN-LAST:event_formWindowClosing
    /**
     * Updates the progressMonitor when importing an audio file
     */
    PropertyChangeListener propertyChangeListenerImport = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if ("progress" == propertyChangeEvent.getPropertyName()) {
                int progress = (Integer) propertyChangeEvent.getNewValue();
                progressMonitor.setProgress(progress);
                String message =
                        String.format("Completed %d%%.\n", progress);
                progressMonitor.setNote(message);
                System.out.println("Message = " + message);
                if (progressMonitor.isCanceled() || task.isDone()) {
                    if (progressMonitor.isCanceled()) {
                        task.cancel(true);
                        System.out.println("Task canceled");

                    } else {
                        System.out.println("Task completed");
                    }
                    jButtonPlot.setEnabled(true);
                }
            }
        }
    };

    /**
     * Updates the progressMonitor when preparing to plot
     */
    PropertyChangeListener propertyChangeListenerPlot = new PropertyChangeListener() {

        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if ("progress" == propertyChangeEvent.getPropertyName()) {
                int progress = (Integer) propertyChangeEvent.getNewValue();
                progressMonitor.setProgress(progress);
                String message =
                        String.format("Completed %d%%.\n", progress);
                progressMonitor.setNote(message);
                System.out.println("Message = " + message);
                if (progressMonitor.isCanceled() || plotTask.isDone()) {
                    if (progressMonitor.isCanceled()) {
                        plotTask.cancel(true);
                        System.out.println("Task canceled");

                    } else {
                        System.out.println("Task completed");
                    }
                    jButtonPlot.setEnabled(true);
                }
            }
        }
    };

    

    /**
     * This task saves the project, i.e., the arraylist Contents to a file
     */
    class SaveProjectTask extends SwingWorker<Void, Void> {

        private File file;

        @Override
        protected Void doInBackground() throws Exception {
            try {
                FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(contents);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JOptionPane.showMessageDialog(mainFrame.this, "Project saved successfully to " + file.getName());

            return null;
        }

        public SaveProjectTask(File file) {
            this.file = file;
        }
    }

    /**
     * This task is responsible for opening a project, i.e., for reading the contents of a .soundit file into an arraylist of object
     */
    class OpenProjectTask extends SwingWorker<Void, Void> {

        private File file;

        private OpenProjectTask(File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());

            ObjectInputStream ois = new ObjectInputStream(fis);

            Object obj = ois.readObject();

            contents = (ArrayList<AnalysisContent>) obj;

            for (AnalysisContent content : contents) {
                jComboBox1.addItem(content.getFile().getName());
            }

            JOptionPane.showMessageDialog(mainFrame.this, "Project imported successfully from " + file.getName());

            return null;
        }
    }

    /**
     * This task is responsible for importing the data from an audio file into the attributes of an object
     */
    class importTask extends SwingWorker<Void, Void> {

        private File file;
        private AnalysisContent content = new AnalysisContent();

        public void setFile(File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            content.setFile(file);
            setProgress(10);
            String extension = FileUtils.getExtension(file.getAbsolutePath());

            System.out.println("Extension = " + extension);

            AudioDecoder decoder = null;

            /*if (extension.equals(".mp3")) {
            decoder = new MP3Decoder(new FileInputStream(file.getAbsolutePath()));
            } else if (extension.equals(".wav") || extension.equals(".wave")) {
            decoder = new WaveDecoder(new FileInputStream(file.getAbsolutePath()));
            }
            setProgress(20);
            SamplesReader samplesReader = new SamplesReader(decoder, WINDOWSIZE_DEFAULT);
            content.setSamples(samplesReader.getAllSamples());
            System.out.println("Samples importadas");
            
            decoder = null;*/

            setProgress(40);
            if (extension.equals(".mp3")) {
                decoder = new MP3Decoder(new FileInputStream(file.getAbsolutePath()));
            } else if (extension.equals(".wav") || extension.equals(".wave")) {
                decoder = new WaveDecoder(new FileInputStream(file.getAbsolutePath()));
            }

            SpectralDifference spectDiff = new SpectralDifference(decoder, WINDOWSIZE_DEFAULT,
                    HOPSIZE_DEFAULT, true, 44100);

            content.setSpectralFlux(spectDiff.getSpectralDifference());
            System.out.println("SpectralFlux calculado");
            setProgress(60);
            PeakDetector peaks = new PeakDetector(spectDiff.getSpectralDifference());

            // calculates the peaks
            peaks.calcPeaks();
            System.out.println("Peaks calculados");

            setProgress(80);

            content.setThreshold(peaks.getThreshold());

            content.setPeaks(peaks.getPeaks());

            //I add the results processed from this file, to the arraylist of contents
            contents.add(content);

            System.out.println("tudo adicionado");

            setProgress(100);
            return null;
        }

        public importTask(File file) {
            this.file = file;
        }

        @Override
        protected void done() {
            super.done();
            progressMonitor.setProgress(0);
            progressMonitor.close();

            jComboBox1.setSelectedItem(content.getFile().getName());
        }
    }

    /**
     * Checks if a combo box contais a particular object
     * @param combo
     * @param o
     * @return
     */
    public boolean comboContains(JComboBox combo, Object o) {
        int size = combo.getItemCount();
        for (int i = 0; i < size; i++) {
            Object obj = combo.getItemAt(i);
            if (obj.equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new mainFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonClose;
    private javax.swing.JButton jButtonPlot;
    private javax.swing.JButton jButtonPlotCalc;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JDialog jDialogAbout;
    private javax.swing.JFileChooser jFileChooserImportAudio;
    private javax.swing.JFileChooser jFileChooserProject;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBarTop;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenu jMenuImport;
    private javax.swing.JMenuItem jMenuItemAbout;
    private javax.swing.JMenuItem jMenuItemConfigure;
    private javax.swing.JMenuItem jMenuItemExit;
    private javax.swing.JMenuItem jMenuItemHelp;
    private javax.swing.JMenuItem jMenuItemImportAudio;
    private javax.swing.JMenuItem jMenuItemOpenProject;
    private javax.swing.JMenuItem jMenuItemSave;
    private javax.swing.JMenu jMenuOptions;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    // End of variables declaration//GEN-END:variables
}
////o hop é usado para se fazer o cálculo da spectral flux sobrepondo-se várias windows consecutivas
//                //int hop = 1024;
//
//                String extension = FileUtils.getExtension(file.getAbsolutePath());
//
//                System.out.println("Extension = " + extension);
//
//                AudioDecoder decoder = null;
//
//                if (extension.equals(".mp3")) {
//                    decoder = new MP3Decoder(new FileInputStream(file.getAbsolutePath()));
//                } else if (extension.equals(".wav") || extension.equals(".wave")) {
//                    decoder = new WaveDecoder(new FileInputStream(file.getAbsolutePath()));
//                }
//
//                //WaveDecoder decoder = new WaveDecoder(new FileInputStream(file.getAbsolutePath()));
//                //MP3Decoder decoder = new MP3Decoder(new FileInputStream(file.getAbsolutePath()));
//
//
//                /*ArrayList<Float> allSamples = new ArrayList<Float>();
//                float[] samples = new float[1024];
//                while (decoder.readSamples(samples) > 0) {
//                for (int i = 0; i < samples.length; i++) {
//                allSamples.add(samples[i]);
//                }
//                }
//                System.out.println("Num samples = " + allSamples.size());*/
//
//                /*SamplesReader samplesReader = new SamplesReader(decoder, WINDOWSIZE_DEFAULT);
//                ArrayList<Float> allSamples = samplesReader.getAllSamples();*/
//
//                SpectralDifference spectDiff = new SpectralDifference(decoder, WINDOWSIZE_DEFAULT,
//                        HOPSIZE_DEFAULT, true, 44100);
//
//                /*PeakDetector peaks = new PeakDetector(spectDiff.getSpectralDifference());
//
//                // calculates the peaks
//                peaks.calcPeaks();*/
//
//
//                //it plots the samples in another thread
//                //PlotThread plotThread = new PlotThread(spectDiff.getSpectralDifference(), "Spectral Flux - " + file.getName(), 800, 600, 1, Color.RED);
//
//                //PlotThread plotThread = new PlotThread(spectDiff.getSpectralDifference(), "Spectral Flux - " + file.getName(), 800, 600, 1, Color.RED, true, file.getAbsolutePath());
//
//                //PlotThread plotThread = new PlotThread(allSamples, "Samples (PCM) - " + file.getName(), 800, 600, 1024, Color.RED, true, file.getAbsolutePath());
//
//                //plotThread.start();
//
//                //PlotTask plotTask = new PlotTask(allSamples, "Samples (PCM) - " + file.getName(), 800, 600, WINDOWSIZE_DEFAULT, Color.RED, file.getAbsolutePath(), true);
//                plotTask = new PlotTask(spectDiff.getSpectralDifference(), "Spectral Dif - " + file.getName(), 800, 600, 1, Color.RED, file.getAbsolutePath(), true, HOPSIZE_DEFAULT);
//
//                //PlotTask plotTask = new PlotTask(peaks.getPeaks(), "Spectral Dif - " + file.getName(), 800, 600, 1, Color.RED, file.getAbsolutePath(), true, HOPSIZE_DEFAULT);
//                plotTask.execute();

