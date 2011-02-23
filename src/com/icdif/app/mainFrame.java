/*
 * mainFrame.java
 *
 * Created on 23/Jan/2011, 11:00:13
 */
package com.icdif.app;

import com.icdif.audio.analysis.PeakDetector;
import com.icdif.audio.analysis.SamplesReader;
import com.icdif.audio.analysis.SpectralDifference;
import com.icdif.audio.graph.Plot;
import com.icdif.audio.io.AudioDecoder;
import com.icdif.audio.io.MP3Decoder;
import com.icdif.audio.io.WavDecoder;

import java.net.URI;
import java.net.URISyntaxException;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import java.util.prefs.*;

/**
 * The class responsible for the Swing interface of the program
 * 
 * @author wanderer
 */
public class mainFrame extends javax.swing.JFrame {

	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = 3840101711617345278L;

	/**
	 * This arrayList of the objects AnalysisContent contains the information
	 * from all the files imported to the program. It's from the data in this
	 * class that the plots (except the samples) are made
	 */
	private ArrayList<AnalysisContent> contents = new ArrayList<AnalysisContent>();

	/**
	 * The default sampling rate (in the moment it's the only one supported by
	 * the framework)
	 */
	private final int SAMPLERATE_DEFAULT = 44100;

	/**
	 * The number of samples in one pixel of the plot
	 */
	private final int WINDOWSIZE_DEFAULT = 1024;
	/**
	 * The default size of the Hop (the margin of overlaping sample windows)
	 */
	private final int HOPSIZE_DEFAULT = 1024;
	/**
	 * The default value of the multiplier, that defines, from the flux, which
	 * are peaks and which are not
	 */
	private final float MULTIPLIER_DEFAULT = 1.6f;
	/**
	 * The window, i.e., the number of value around each one to calculate the
	 * threshold from the spectral flux
	 */
	private final int THRESHOLD_WINDOW_DEFAULT = 10;
	/**
	 * The plot is defined to, by default, play the audio when it is drawn
	 */
	private final boolean PLAYONPLOT_DEFAULT = true;

	/**
	 * The url of the help website
	 */
	private final String HELPURL = "http://icdif.com/soundit/";

	/**
	 * The url of the version control server version of the project
	 */
	private final String GITURL = "https://github.com/Shemahmforash/SoundIt";

	/**
	 * The preferred width of the main window
	 */
	private final int DEFAULT_WIDTH = 800;

	/**
	 * The preferred height of the main window
	 */
	private final int DEFAULT_HEIGHT = 600;

	/**
	 * The audio extensions allowed in the program. This is a filter to be used
	 * in the openfile dialogs.
	 */
	ExtensionFileFilter filterSound = new ExtensionFileFilter("Wave and MP3",
			new String[] { "MP3", "WAV", "WAVE" });

	/**
	 * The extension of this program files. It is a filter to be used when one
	 * is saving or opening a project.
	 */
	ExtensionFileFilter filterProject = new ExtensionFileFilter("SoundIt",
			new String[] { "SOUNDIT" });
	/**
	 * A variable to store the program preferences
	 */
	private Preferences prefs;

	/**
	 * This is the constructor of this class, that is responsible for setting
	 * the default preferences and creating the swing GUI.
	 */
	public mainFrame() {

		// instantiates a new preferences node
		prefs = Preferences.userRoot().node(this.getClass().getName());
		// sets the default preferences
		try {
			setDefaultPreferences();
		} catch (BackingStoreException ex) {
			JOptionPane.showMessageDialog(this,
					"Warning! Cannot set preferences!");
		}

		// start the swing components
		initComponents();
	}

	/**
	 * This method initializes the swing components of the frame
	 */
	private void initComponents() {

		// sets the default size of the main window
		this.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));

		jDialogAbout = new javax.swing.JDialog();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jButtonClose = new javax.swing.JButton();
		jFileChooserImportAudio = new javax.swing.JFileChooser();
		jFileChooserProject = new javax.swing.JFileChooser();
		jDialogConfigure = new javax.swing.JDialog();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jPanel1 = new javax.swing.JPanel();
		jLabelMultiplier = new javax.swing.JLabel();
		jTextFieldMultiplier = new javax.swing.JTextField();
		jLabelSamplesPerPixel = new javax.swing.JLabel();
		jTextFieldSamplesPerPixel = new javax.swing.JTextField();
		jLabelHopSize = new javax.swing.JLabel();
		jTextFieldHopSize = new javax.swing.JTextField();
		jLabelThresholdWindow = new javax.swing.JLabel();
		jTextFieldThresholdWindow = new javax.swing.JTextField();
		jLabelPlayOnPlot = new javax.swing.JLabel();
		jComboBoxPlayOnPlot = new javax.swing.JComboBox();
		jPanel2 = new javax.swing.JPanel();
		jLabelMultiplier1 = new javax.swing.JLabel();
		jLabelSamplesPerPixel1 = new javax.swing.JLabel();
		jLabelHopSize1 = new javax.swing.JLabel();
		jLabelHopSize2 = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		jButtonConfigOk = new javax.swing.JButton();
		jButtonConfigCancel = new javax.swing.JButton();
		jButtonConfigDefault = new javax.swing.JButton();
		jToolBarTop = new javax.swing.JToolBar();
		jComboBoxFiles = new javax.swing.JComboBox();
		jCheckBoxSpectFlux = new javax.swing.JCheckBox();
		jCheckBoxThreshold = new javax.swing.JCheckBox();
		jCheckBoxPeaks = new javax.swing.JCheckBox();
		jToolBarBottom = new javax.swing.JToolBar();
		jLabelProgress = new javax.swing.JLabel();
		jProgressBarTasks = new javax.swing.JProgressBar();
		jMenuBarTop = new javax.swing.JMenuBar();
		jMenuFile = new javax.swing.JMenu();
		jMenuItemOpenProject = new javax.swing.JMenuItem();
		jMenuItemSave = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JPopupMenu.Separator();
		jMenuItemExit = new javax.swing.JMenuItem();
		jMenuImport = new javax.swing.JMenu();
		jMenuItemImportAudio = new javax.swing.JMenuItem();
		jMenuPlot = new javax.swing.JMenu();
		jMenuItemPlotSamples = new javax.swing.JMenuItem();
		jMenuItemPlotCalc = new javax.swing.JMenuItem();
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
		jLabel2
				.setText("<html>SoundIT is a program built using the java programming language and intended to provide several functions related to audio.<br/> It allows one to read and process data from audio files (.wav and .mp3). From this streams, it can plot the values in the time domain (amplitude vs time), but it can also find and plot onsets in the audio, by using the Fourier Transform and other processing methods in the frequency domain. <br/><br/>  More info at:<br/>"
						+ HELPURL + "<br/> and<br/>" + GITURL);
		jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		jButtonClose.setText("Close");
		jButtonClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonCloseActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jDialogAboutLayout = new javax.swing.GroupLayout(
				jDialogAbout.getContentPane());
		jDialogAbout.getContentPane().setLayout(jDialogAboutLayout);
		jDialogAboutLayout
				.setHorizontalGroup(jDialogAboutLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jDialogAboutLayout
										.createSequentialGroup()
										.addGroup(
												jDialogAboutLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jDialogAboutLayout
																		.createSequentialGroup()
																		.addGap(
																				24,
																				24,
																				24)
																		.addGroup(
																				jDialogAboutLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								jLabel2,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								456,
																								Short.MAX_VALUE)
																						.addComponent(
																								jLabel1)))
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																jDialogAboutLayout
																		.createSequentialGroup()
																		.addContainerGap(
																				433,
																				Short.MAX_VALUE)
																		.addComponent(
																				jButtonClose)))
										.addContainerGap()));
		jDialogAboutLayout
				.setVerticalGroup(jDialogAboutLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jDialogAboutLayout
										.createSequentialGroup()
										.addGap(22, 22, 22)
										.addComponent(jLabel1)
										.addGap(18, 18, 18)
										.addComponent(
												jLabel2,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jButtonClose)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		jFileChooserProject.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);

		jDialogConfigure.setTitle("SoundIt Options");

		jLabelMultiplier.setText("Multiplier");

		jLabelSamplesPerPixel.setText("Samples per Pixel");

		jLabelHopSize.setText("Hop Size");

		jLabelThresholdWindow.setText("Threshold Window");

		jLabelPlayOnPlot.setText("Play on Plot");

		jComboBoxPlayOnPlot.setModel(new javax.swing.DefaultComboBoxModel(
				new String[] { "true", "false" }));

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jLabelMultiplier)
														.addComponent(
																jLabelSamplesPerPixel)
														.addComponent(
																jLabelHopSize))
										.addGap(28, 28, 28)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(
																jTextFieldHopSize)
														.addComponent(
																jTextFieldMultiplier)
														.addComponent(
																jTextFieldSamplesPerPixel,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																60,
																Short.MAX_VALUE))
										.addGap(18, 18, 18)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jLabelThresholdWindow)
														.addComponent(
																jLabelPlayOnPlot))
										.addGap(28, 28, 28)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jComboBoxPlayOnPlot,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jTextFieldThresholdWindow,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																49,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(33, Short.MAX_VALUE)));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jLabelMultiplier)
														.addComponent(
																jTextFieldMultiplier,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jTextFieldThresholdWindow,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jLabelThresholdWindow))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jLabelSamplesPerPixel)
														.addComponent(
																jTextFieldSamplesPerPixel,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jLabelPlayOnPlot)
														.addComponent(
																jComboBoxPlayOnPlot,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(13, 13, 13)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jTextFieldHopSize,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																jLabelHopSize))
										.addContainerGap(73, Short.MAX_VALUE)));

		jTabbedPane1.addTab("Parameters", jPanel1);

		jLabelMultiplier1.setText("Samples");

		jLabelSamplesPerPixel1.setText("Spectral Flux");

		jLabelHopSize1.setText("Threshold");

		jLabelHopSize2.setText("Peaks");

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																jLabelMultiplier1)
														.addComponent(
																jLabelSamplesPerPixel1)
														.addComponent(
																jLabelHopSize1)
														.addComponent(
																jLabelHopSize2))
										.addContainerGap(379, Short.MAX_VALUE)));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addGap(18, 18, 18)
										.addComponent(jLabelMultiplier1)
										.addGap(13, 13, 13)
										.addComponent(jLabelSamplesPerPixel1)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabelHopSize1)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabelHopSize2)
										.addContainerGap(68, Short.MAX_VALUE)));

		jTabbedPane1.addTab("Colors", jPanel2);

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(
				jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 473,
				Short.MAX_VALUE));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 191,
				Short.MAX_VALUE));

		jTabbedPane1.addTab("Other", jPanel3);

		jButtonConfigOk.setText("OK");
		jButtonConfigOk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonConfigOkActionPerformed(evt);
			}
		});

		jButtonConfigCancel.setText("Cancel");
		jButtonConfigCancel
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jButtonConfigCancelActionPerformed(evt);
					}
				});

		jButtonConfigDefault.setText("Default");
		jButtonConfigDefault
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jButtonConfigDefaultActionPerformed(evt);
					}
				});

		javax.swing.GroupLayout jDialogConfigureLayout = new javax.swing.GroupLayout(
				jDialogConfigure.getContentPane());
		jDialogConfigure.getContentPane().setLayout(jDialogConfigureLayout);
		jDialogConfigureLayout
				.setHorizontalGroup(jDialogConfigureLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jDialogConfigureLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jDialogConfigureLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																jTabbedPane1,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																485,
																Short.MAX_VALUE)
														.addGroup(
																jDialogConfigureLayout
																		.createSequentialGroup()
																		.addComponent(
																				jButtonConfigOk)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				jButtonConfigCancel)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addComponent(
																				jButtonConfigDefault)))
										.addContainerGap()));
		jDialogConfigureLayout
				.setVerticalGroup(jDialogConfigureLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jDialogConfigureLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jTabbedPane1,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												234, Short.MAX_VALUE)
										.addGap(18, 18, 18)
										.addGroup(
												jDialogConfigureLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																jButtonConfigCancel)
														.addComponent(
																jButtonConfigOk)
														.addComponent(
																jButtonConfigDefault))
										.addContainerGap()));

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("SoundIt - Sound analyser");
		setName("mainFrame"); // NOI18N
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		jToolBarTop.setFloatable(false);
		jToolBarTop.setRollover(true);

		jToolBarTop.add(jComboBoxFiles);

		jCheckBoxSpectFlux.setText("Spectral Flux");
		jCheckBoxSpectFlux.setFocusable(false);
		jCheckBoxSpectFlux
				.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		jCheckBoxSpectFlux
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jToolBarTop.add(jCheckBoxSpectFlux);

		jCheckBoxThreshold.setText("Threshold");
		jCheckBoxThreshold.setFocusable(false);
		jCheckBoxThreshold
				.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		jCheckBoxThreshold
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jToolBarTop.add(jCheckBoxThreshold);

		jCheckBoxPeaks.setSelected(true);
		jCheckBoxPeaks.setText("Peaks");
		jCheckBoxPeaks.setFocusable(false);
		jCheckBoxPeaks
				.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
		jCheckBoxPeaks
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jToolBarTop.add(jCheckBoxPeaks);

		jToolBarBottom.setFloatable(false);
		jToolBarBottom.setRollover(true);
		jToolBarBottom.setFocusable(false);
		jToolBarBottom.setName("BottomToolBar"); // NOI18N
		jToolBarBottom.add(jLabelProgress);

		jProgressBarTasks.setMaximumSize(new java.awt.Dimension(150, 20));
		jToolBarBottom.add(jProgressBarTasks);

		jMenuFile.setText("File");

		jMenuItemOpenProject.setText("Open Project");
		jMenuItemOpenProject
				.addActionListener(new java.awt.event.ActionListener() {
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
		jMenuItemImportAudio
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jMenuItemImportAudioActionPerformed(evt);
					}
				});
		jMenuImport.add(jMenuItemImportAudio);

		jMenuBarTop.add(jMenuImport);

		jMenuPlot.setText("Plot");

		jMenuItemPlotSamples.setText("Plot PCM Data (samples)");
		jMenuItemPlotSamples
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						plotSamplesActionPerformed(evt);
					}
				});
		jMenuPlot.add(jMenuItemPlotSamples);

		jMenuItemPlotCalc.setText("Plot Calculations");
		jMenuItemPlotCalc
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						plotCalcActionPerformed(evt);
					}
				});
		jMenuPlot.add(jMenuItemPlotCalc);

		jMenuBarTop.add(jMenuPlot);

		jMenuOptions.setText("Options");

		jMenuItemConfigure.setText("Configure");
		jMenuItemConfigure
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						jMenuItemConfigureActionPerformed(evt);
					}
				});
		jMenuOptions.add(jMenuItemConfigure);

		jMenuBarTop.add(jMenuOptions);

		jMenuHelp.setText("Help");

		jMenuItemHelp.setText("Help");
		jMenuItemHelp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jMenuItemHelpActionPerformed(evt);
			}
		});
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

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jToolBarTop, javax.swing.GroupLayout.DEFAULT_SIZE,
				DEFAULT_WIDTH, Short.MAX_VALUE).addComponent(jToolBarBottom,
				javax.swing.GroupLayout.Alignment.TRAILING,
				javax.swing.GroupLayout.DEFAULT_SIZE, DEFAULT_WIDTH,
				Short.MAX_VALUE));
		layout
				.setVerticalGroup(layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addComponent(
												jToolBarTop,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												378, Short.MAX_VALUE)
										.addComponent(
												jToolBarBottom,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												25,
												javax.swing.GroupLayout.PREFERRED_SIZE)));

		pack();
	}

	/**
	 * Exits the program by clicking in the menu button
	 * 
	 * @param evt
	 */
	private void jMenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {
		this.formWindowClosing(null);
	}

	/**
	 * Imports the audio from a file into an new entry in the contents arraylist
	 * 
	 * @param evt
	 */
	private void jMenuItemImportAudioActionPerformed(
			java.awt.event.ActionEvent evt) {
		try {

			jFileChooserImportAudio.setFileFilter(filterSound);

			final int returnVal = jFileChooserImportAudio.showOpenDialog(this);

			if (returnVal == jFileChooserImportAudio.APPROVE_OPTION) {

				File file = jFileChooserImportAudio.getSelectedFile();

				if (!comboContains(jComboBoxFiles, file.getName())) {
					System.out.println("File: " + file.getAbsolutePath());

					jComboBoxFiles.addItem(file.getName());

					/*
					 * progressMonitor = new ProgressMonitor(this, "Importing "
					 * + file.getName(), "", 0, 100);
					 * progressMonitor.setProgress(0);
					 */

					jLabelProgress.setText("Importing " + file.getName() + " ");

					ImportTask importTask = new ImportTask(file);
					importTask
							.addPropertyChangeListener(propertyChangeListenerImport);
					importTask.execute();
				} else {
					// System.out.println("The file " + file.getName() +
					// " was already processed");

					JOptionPane.showMessageDialog(this, "The file "
							+ file.getName() + " was already processed");
				}

			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(mainFrame.this,
					"Problems importing file: " + ex.getMessage(),
					"Can't import", JOptionPane.WARNING_MESSAGE);
		}

	}

	/**
	 * Saves the contents arraylist to a file, i.e., saves the current project
	 * 
	 * @param evt
	 */
	private void jMenuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {

		if (contents.size() > 0) {
			jFileChooserProject.setFileFilter(filterProject);

			int returnVal = jFileChooserProject.showSaveDialog(this);
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (returnVal == jFileChooserProject.APPROVE_OPTION) {
				File file = jFileChooserProject.getSelectedFile();

				if (!file.exists()) {
					try {

						file.createNewFile();
						System.out.println("New file " + file.getAbsolutePath()
								+ " has been created to the current directory");
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(mainFrame.this,
								"Problems creating file "
										+ file.getAbsolutePath() + " : "
										+ ex.getMessage(),
								"Can't create new file",
								JOptionPane.WARNING_MESSAGE);
					}
				}

				System.out.println("Faço save de " + file.getAbsolutePath());

				/*
				 * try { FileOutputStream fos = new
				 * FileOutputStream(file.getAbsolutePath()); ObjectOutputStream
				 * oos = new ObjectOutputStream(fos); oos.writeObject(contents);
				 * 
				 * JOptionPane.showMessageDialog(this,
				 * "The contents of this project were saved successfully to " +
				 * file.getName()); } catch (FileNotFoundException e) {
				 * e.printStackTrace(); } catch (IOException e) {
				 * e.printStackTrace(); }
				 */

				jProgressBarTasks.setIndeterminate(true);
				jLabelProgress.setText("Saving project to " + file.getName()
						+ " ");
				SaveProjectTask saveProjectTask = new SaveProjectTask(file);
				saveProjectTask.execute();

				// JOptionPane.showMessageDialog(this,
				// "The contents of this project were saved successfully to " +
				// file.getName());

			}

			setCursor(null); // turn off the wait cursor
		} else {
			JOptionPane
					.showMessageDialog(
							this,
							"You can't save an empty project. First you must import audio.",
							"No File Chosen", JOptionPane.WARNING_MESSAGE);
		}

	}

	/**
	 * In response to the close button in the AboutBox Dialog, it closes the
	 * AboutBox
	 * 
	 * @param evt
	 */
	private void jButtonCloseActionPerformed(java.awt.event.ActionEvent evt) {
		jDialogAbout.dispose();
	}

	/**
	 * This method gets the saved data from a file and sets the arraylist
	 * contents with it
	 * 
	 * @param evt
	 */
	private void jMenuItemOpenProjectActionPerformed(
			java.awt.event.ActionEvent evt) {
		try {

			jFileChooserProject.setFileFilter(filterProject);

			int returnVal = jFileChooserProject.showOpenDialog(this);

			if (returnVal == jFileChooserProject.APPROVE_OPTION) {
				File file = jFileChooserProject.getSelectedFile();
				if (file.exists()) {

					// this variable keeps the response from the confirm Dialog
					// (responses: 1,0,-1)
					int response = JOptionPane.YES_OPTION;

					if (contents.size() > 0) {

						// default icon, custom title
						response = JOptionPane
								.showConfirmDialog(
										this,
										"Do you wish to import a new project? You have an unsaved project. If you choose to import a new project, the current project will be lost.",
										"Confirm project opening",
										JOptionPane.YES_NO_OPTION);

						// System.out.println("Resposta: " + response);

					}
					// if the response is yes, I clear the arraylist contents
					// and the combo and execute the task of project opening
					if ((response == JOptionPane.YES_OPTION)) {
						jComboBoxFiles.removeAllItems();
						contents.clear();

						jProgressBarTasks.setIndeterminate(true);
						jLabelProgress.setText("Opening project from "
								+ file.getName() + " ");

						OpenProjectTask openTask = new OpenProjectTask(file);
						openTask.execute();
					}
				}
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Problems opening project: "
					+ e.getMessage(), "Problems opening project.",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * It asks for confirmation when one is quitting the program
	 * 
	 * @param evt
	 */
	private void formWindowClosing(java.awt.event.WindowEvent evt) {
		int confirmed = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to quit?", "Confirm Quit",
				JOptionPane.YES_NO_OPTION);

		// Close if user confirmed
		if (confirmed == JOptionPane.YES_OPTION) {
			// Close frame
			this.dispose();
			System.exit(0);
		}
	}

	/**
	 * It opens the configure box to set the preferences of the program
	 * 
	 * @param evt
	 */
	private void jMenuItemConfigureActionPerformed(
			java.awt.event.ActionEvent evt) {

		jDialogConfigure.setBounds(0, 0, 520, 270);
		jDialogConfigure.setVisible(true);

		// I fill the text boxes of the configure dialog with the values saved
		// in the preferences
		fillsConfigureDialogValues();

	}

	/**
	 * Opens the help site url when clicking in the Help menu
	 * 
	 * @param evt
	 */
	private void jMenuItemHelpActionPerformed(java.awt.event.ActionEvent evt) {
		Desktop desktop = null;
		// check if desktop integration is possible
		if (!Desktop.isDesktopSupported()) {
			JOptionPane
					.showMessageDialog(
							this,
							"Can't communicate with browser! The help website will not be opened.",
							"The help website will not be opened",
							JOptionPane.WARNING_MESSAGE);

		}
		// if the integration is possible, we find the default browser
		else {

			desktop = Desktop.getDesktop();
			// if there is no default browser, we fire a warning
			if (!desktop.isSupported(Desktop.Action.BROWSE)) {
				JOptionPane
						.showMessageDialog(
								this,
								"There is no default web browser in the system! The help website will not be opened.",
								"The help website will not be opened",
								JOptionPane.WARNING_MESSAGE);
			}
			// we create a new uri and open the browser with it
			else {
				URI uri = null;
				try {
					uri = new URI(HELPURL);
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}

				// fire the browser
				try {
					desktop.browse(uri);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Opens the about box when clicking in the about menu
	 * 
	 * @param evt
	 */
	private void jMenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {
		jDialogAbout.setBounds(0, 0, 400, 400);
		jDialogAbout.setVisible(true);
	}

	/**
	 * Processes the action performed event Plot Calculations menu
	 * 
	 * @param evt
	 */
	private void plotCalcActionPerformed(java.awt.event.ActionEvent evt) {
		if (jComboBoxFiles.getSelectedIndex() >= 0) {

			if (jCheckBoxPeaks.isSelected() || jCheckBoxSpectFlux.isSelected()
					|| jCheckBoxThreshold.isSelected()) {

				// find the selected index of the comboBox. Since the combobox
				// is filled at the same order as the contents array list, the
				// indexes are the same
				int selectedIndex = jComboBoxFiles.getSelectedIndex();

				String fileName = contents.get(selectedIndex).getFile()
						.getAbsolutePath();

				// String extension = FileUtils.getExtension(fileName);
				// System.out.println("Extension = " + extension);

				jLabelProgress
						.setText("Plotting "
								+ contents.get(selectedIndex).getFile()
										.getName() + " ");

				ArrayList<Float> peaks = null;
				ArrayList<Float> spectralFlux = null;
				ArrayList<Float> threshold = null;

				if (jCheckBoxPeaks.isSelected()) {
					peaks = contents.get(selectedIndex).getPeaks();
				}
				if (jCheckBoxSpectFlux.isSelected()) {
					spectralFlux = contents.get(selectedIndex)
							.getSpectralFlux();
				}
				if (jCheckBoxThreshold.isSelected()) {
					threshold = contents.get(selectedIndex).getThreshold();
				}

				// System.out.println("Peaks.size = " + peaks.isEmpty() +
				// " ; flux.size = " + spectralFlux.isEmpty() +
				// " ; threshold.size = " + threshold.isEmpty());

				PlottingCalcTask plottingCalcTask = new PlottingCalcTask(
						spectralFlux, threshold, peaks, "Calculations - "
								+ contents.get(selectedIndex).getFile()
										.getName(), 800, 600, prefs.getInt(
								"WINDOWSIZE", WINDOWSIZE_DEFAULT), prefs
								.getInt("WINDOWSIZE", WINDOWSIZE_DEFAULT),
						prefs.getBoolean("PLAYONPLOT", PLAYONPLOT_DEFAULT),
						fileName);

				plottingCalcTask
						.addPropertyChangeListener(propertyChangeListenerPlot);

				plottingCalcTask.execute();

			} else {
				JOptionPane
						.showMessageDialog(
								this,
								"You need to choose, by using the checkboxes, at least one of the calculated results to be plotted.",
								"You must choose a parameter to plot",
								JOptionPane.WARNING_MESSAGE);
			}

		} else if (!contents.isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"You need to choose the file to plot from the Combo Box",
					"No File Chosen", JOptionPane.WARNING_MESSAGE);
		} else if (contents.isEmpty()) {
			JOptionPane
					.showMessageDialog(
							this,
							"In order to plot, you first need to import audio to the program.",
							"You need to import audio first",
							JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * This method plots the samples in the file selected in the combobox. This
	 * samples are too big to be saved in the program, so they are read from the
	 * file
	 * 
	 * @param evt
	 */
	private void plotSamplesActionPerformed(java.awt.event.ActionEvent evt) {
		// jButtonPlot.setEnabled(false);

		// if there is a file in the combobox
		if (jComboBoxFiles.getSelectedIndex() >= 0) {

			// find the selected index of the comboBox. Since the combobox is
			// filled
			// at the same order as the contents array list, the indexes are the
			// same
			int selectedIndex = jComboBoxFiles.getSelectedIndex();

			String fileName = contents.get(selectedIndex).getFile()
					.getAbsolutePath();

			String extension = FileUtils.getExtension(fileName);
			// System.out.println("Extension = " + extension);

			jLabelProgress.setText("Plotting "
					+ contents.get(selectedIndex).getFile().getName() + " ");

			AudioDecoder decoder = null;

			try {
				if (extension.equals(".mp3")) {
					decoder = new MP3Decoder(new FileInputStream(fileName));
				} else if (extension.equals(".wav")
						|| extension.equals(".wave")) {
					decoder = new WavDecoder(new FileInputStream(fileName));
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Can't decode " + fileName
						+ " because: " + e.getMessage(), "Can't decode",
						JOptionPane.WARNING_MESSAGE);
			}

			// the samples are too big to be kept in the class methods, so they
			// are read from the file
			ArrayList<Float> samplesFromFile = new ArrayList<Float>();

			SamplesReader samplesReader = new SamplesReader(decoder,
					WINDOWSIZE_DEFAULT);

			// gets all the samples from the file
			samplesFromFile = samplesReader.getAllSamples();

			// plots the samples in a background task
			PlottingSamplesTask plottingSamplesTask = new PlottingSamplesTask(
					samplesFromFile, "PCM DATA - " + fileName, 800, 600, prefs
							.getInt("WINDOWSIZE", WINDOWSIZE_DEFAULT), prefs
							.getBoolean("PLAYONPLOT", PLAYONPLOT_DEFAULT),
					fileName);
			plottingSamplesTask
					.addPropertyChangeListener(propertyChangeListenerPlot);
			plottingSamplesTask.execute();

			// I clean the variables
			decoder = null;
		} else if (contents.size() > 0) {
			JOptionPane.showMessageDialog(this,
					"You need to choose the file to plot from the Combo Box",
					"No File Chosen", JOptionPane.WARNING_MESSAGE);
		} else if (contents.size() == 0) {
			JOptionPane
					.showMessageDialog(
							this,
							"In order to plot, you first need to import audio to the programa",
							"You need to import audio first",
							JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Responds to the cancel button in the COnfigure Dialog Box.
	 * 
	 * @param evt
	 */
	private void jButtonConfigCancelActionPerformed(
			java.awt.event.ActionEvent evt) {
		fillsConfigureDialogValues();
		jDialogConfigure.dispose();
	}

	/**
	 * Responding to the Ok button of the Configure Box, it sets the preferences
	 * from the values written by the user
	 * 
	 * @param evt
	 */
	private void jButtonConfigOkActionPerformed(java.awt.event.ActionEvent evt) {
		setPreferencesFromConfigureDialog();
		jDialogConfigure.dispose();
	}

	/**
	 * Responding to the Default button of the Configure Dialog, it restores the
	 * default preferences and fills the text boxes with them.
	 * 
	 * @param evt
	 */
	private void jButtonConfigDefaultActionPerformed(
			java.awt.event.ActionEvent evt) {
		restoreDefaultPreferences();
		fillsConfigureDialogValues();
	}

	/**
	 * Updates the progressBar when importing an audio file
	 */
	PropertyChangeListener propertyChangeListenerImport = new PropertyChangeListener() {

		public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
			if ("progress".equals(propertyChangeEvent.getPropertyName())) {
				jProgressBarTasks.setIndeterminate(true);
			}
		}
	};
	/**
	 * Updates the progressBar when plotting
	 */
	PropertyChangeListener propertyChangeListenerPlot = new PropertyChangeListener() {

		public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
			if ("progress".equals(propertyChangeEvent.getPropertyName())) {
				jProgressBarTasks.setIndeterminate(true);
			}
		}
	};

	/**
	 * This task saves the project, i.e., the arraylist Contents, to a file
	 */
	class SaveProjectTask extends SwingWorker<Void, Void> {

		private File file;

		@Override
		protected Void doInBackground() throws Exception {
			try {
				FileOutputStream fos = new FileOutputStream(file
						.getAbsolutePath());
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(contents);

			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(mainFrame.this,
						"Can't save because " + file.getName()
								+ " was not found", "Can't save project",
						JOptionPane.WARNING_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(mainFrame.this,
						"Can't save because " + e.getMessage(),
						"Can't save project", JOptionPane.WARNING_MESSAGE);
			}

			JOptionPane.showMessageDialog(mainFrame.this,
					"Project saved successfully to " + file.getName());

			return null;
		}

		@Override
		protected void done() {
			super.done();

			jProgressBarTasks.setIndeterminate(false);
			jProgressBarTasks.setValue(100);

			/*JOptionPane.showMessageDialog(mainFrame.this,
					"Project saved successfully to " + file.getName());*/

			jProgressBarTasks.setValue(0);
			jLabelProgress.setText("");
		}

		/**
		 * Constructor that initiates this task, by passing the file where to
		 * save the project
		 * 
		 * @param file
		 *            the file where to save the file
		 */
		public SaveProjectTask(final File file) {
			this.file = file;
		}
	}

	/**
	 * This task is responsible for opening a project, i.e., for reading the
	 * contents of a .soundit file into an arraylist of object
	 */
	class OpenProjectTask extends SwingWorker<Void, Void> {

		private File file;

		private OpenProjectTask(final File file) {
			this.file = file;
		}

		@Override
		protected Void doInBackground() throws Exception {
			System.out.println("Open Project task");

			// reads the objects from a file
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());

			ObjectInputStream ois = new ObjectInputStream(fis);

			Object obj = ois.readObject();

			// casts the objects read into the arraylist
			contents = (ArrayList<AnalysisContent>) obj;

			System.out.println("Size contents = " + contents.size());

			// it fills the combobox with the filenames
			for (AnalysisContent content : contents) {
				jComboBoxFiles.addItem(content.getFile().getName());
			}

			return null;
		}

		@Override
		protected void done() {
			super.done();

			System.out.println("Open Project Task Done");

			jProgressBarTasks.setIndeterminate(false);
			jProgressBarTasks.setValue(100);

			JOptionPane.showMessageDialog(mainFrame.this,
					"Project imported successfully from " + file.getName());

			jProgressBarTasks.setValue(0);
			jLabelProgress.setText("");

		}
	}

	/**
	 * This task is responsible for importing the data from an audio file into
	 * the attributes of an object of the type Analysis content and adding this
	 * object to the attribute of the mainFrame
	 */
	class ImportTask extends SwingWorker<Void, Void> {

		private File file;
		/**
		 * This object keeps the calculations made with the data from the audio
		 * file imported
		 */
		private AnalysisContent content = new AnalysisContent();

		/**
		 * Instantiates this class by setting its File attribute
		 * 
		 * @param file
		 */
		public ImportTask(File file) {
			this.file = file;
		}

		/**
		 * Sets the file
		 * 
		 * @param file
		 *            the file
		 */
		public void setFile(final File file) {
			this.file = file;
		}

		@Override
		protected Void doInBackground() throws Exception {
			content.setFile(file);
			setProgress(10);
			String extension = FileUtils.getExtension(file.getAbsolutePath());

			System.out.println("Extension = " + extension);

			// first we set the decoder
			AudioDecoder decoder = null;
			setProgress(40);
			if (extension.equals(".mp3")) {
				decoder = new MP3Decoder(new FileInputStream(file
						.getAbsolutePath()));
			} else if (extension.equals(".wav") || extension.equals(".wave")) {
				decoder = new WavDecoder(new FileInputStream(file
						.getAbsolutePath()));
			}

			// then, using the decoder, we calculate the spectral difference
			SpectralDifference spectDiff = new SpectralDifference(decoder,
					prefs.getInt("WINDOWSIZE", WINDOWSIZE_DEFAULT), prefs
							.getInt("HOPSIZE", HOPSIZE_DEFAULT), true,
					SAMPLERATE_DEFAULT);

			content.setSpectralFlux(spectDiff.getSpectralDifference());
			System.out.println("SpectralFlux calculado");
			setProgress(60);
			// and, finally, from the spectral difference, we calculate the
			// peaks
			PeakDetector peaks = new PeakDetector(spectDiff
					.getSpectralDifference(), prefs.getInt("THRESHOLDWINDOW",
					THRESHOLD_WINDOW_DEFAULT), prefs.getFloat("MULTIPLIER",
					MULTIPLIER_DEFAULT));

			// calculates the peaks
			peaks.calcPeaks();
			System.out.println("Peaks calculados");

			setProgress(80);

			content.setThreshold(peaks.getThreshold());

			content.setPeaks(peaks.getPeaks());

			// I add the results processed from this file, to the arraylist of
			// contents
			contents.add(content);

			System.out.println("tudo adicionado");

			setProgress(100);

			// I clean the task variables:
			spectDiff = null;
			peaks = null;

			return null;
		}

		@Override
		protected void done() {
			super.done();
			/*
			 * progressMonitor.setProgress(0); progressMonitor.close();
			 */

			jProgressBarTasks.setIndeterminate(false);
			jProgressBarTasks.setValue(100);

			jComboBoxFiles.setSelectedItem(content.getFile().getName());

			JOptionPane.showMessageDialog(mainFrame.this,
					"Audio imported successfully from " + file.getName());

			jProgressBarTasks.setValue(0);
			jLabelProgress.setText("");

			// clean the variables
			this.file = null;
			this.content = null;
		}
	}

	/**
	 * THis task is responsible for plotting the samples (pcm audio data) from
	 * the file and, in the end, deleting this samples
	 */
	class PlottingSamplesTask extends SwingWorker<Void, Void> {

		private ArrayList<Float> samples;
		private String title;
		private int width;
		private int height;
		private int samplesPerPixel;
		private String fileName = null;
		private boolean play = false;
		private Color samplesColor = Color.RED;

		/**
		 * Constructor of this task, that receives its main parameters
		 * 
		 * @param samples
		 *            the arraylist of samples to plot
		 * @param title
		 *            the title of the graph window
		 * @param width
		 *            the width of the window
		 * @param height
		 *            the height of the window
		 * @param samplesPerPixel
		 *            the "resolution" of the graph
		 */
		public PlottingSamplesTask(ArrayList<Float> samples, String title,
				int width, int height, int samplesPerPixel) {
			this.samples = samples;
			this.title = title;
			this.width = width;
			this.height = height;
			this.samplesPerPixel = samplesPerPixel;
		}

		/**
		 * Constructor of this task, that receives the main parameters and also
		 * the parameters that allow the class to play the audio
		 * 
		 * @param samples
		 *            the arraylist of samples to plot
		 * @param title
		 *            the title of the graph window
		 * @param width
		 *            the width of the window
		 * @param height
		 *            the height of the window
		 * @param samplesPerPixel
		 *            the "resolution" of the graph
		 * @param play
		 *            a boolean that tells the task to play or not the audio
		 * @param fileName
		 *            the absolute path of the file from where to read the audio
		 */
		public PlottingSamplesTask(ArrayList<Float> samples, String title,
				int width, int height, int samplesPerPixel, boolean play,
				String fileName) {
			this.samples = samples;
			this.title = title;
			this.width = width;
			this.height = height;
			this.samplesPerPixel = samplesPerPixel;
			this.play = play;
			this.fileName = fileName;
		}

		@Override
		protected Void doInBackground() throws Exception {
			System.out.println("Plotting Samples starting");

			Plot plot = new Plot(title, width, height);

			plot.plot(samples, samplesPerPixel, samplesColor);

			if (play) {
				AudioDecoder decoder = null;

				String extension = FileUtils.getExtension(fileName);

				System.out.println("Extension = " + extension);

				if (extension.equals(".mp3")) {
					try {
						decoder = new MP3Decoder(new FileInputStream(fileName));

					} catch (FileNotFoundException ex) {
						JOptionPane.showMessageDialog(mainFrame.this,
								"Can't open the decoder because the file "
										+ fileName + " was not found!",
								"File not found.", JOptionPane.WARNING_MESSAGE);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(mainFrame.this,
								"It's no possible to decode " + fileName
										+ " because " + ex.getMessage(),
								"Can't decode", JOptionPane.WARNING_MESSAGE);
					}
				} else if (extension.equals(".wav")
						|| extension.equals(".wave")) {
					try {
						decoder = new WavDecoder(new FileInputStream(fileName));
					} catch (FileNotFoundException ex) {
						JOptionPane.showMessageDialog(mainFrame.this,
								"Can't open the decoder because the file "
										+ fileName + " was not found!",
								"File not found.", JOptionPane.WARNING_MESSAGE);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(mainFrame.this,
								"It's no possible to decode " + fileName
										+ " because " + ex.getMessage(),
								"Can't decode", JOptionPane.WARNING_MESSAGE);
					}
				}

				try {

					plot.PlayInPlot(samplesPerPixel, decoder);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(mainFrame.this,
							"It's not possible to play " + fileName
									+ " because " + ex.getMessage(),
							"Can't play", JOptionPane.WARNING_MESSAGE);
				}
			}

			return null;
		}

		@Override
		protected void done() {
			super.done();

			System.out.println("Plotting samples Task is done");

			jProgressBarTasks.setIndeterminate(false);
			jProgressBarTasks.setValue(0);
			jLabelProgress.setText("");

			this.samples.clear();
			System.out.println("Clear samples");
			/*
			 * try { this.finalize(); } catch (Throwable ex) {
			 * Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE,
			 * null, ex); }
			 */
		}
	}

	/**
	 * This task is responsible for plotting the calculated results from the
	 * arraylist of the objects AnalysisContent
	 */
	class PlottingCalcTask extends SwingWorker<Void, Void> {

		private ArrayList<Float> spectralFlux;
		private Color spectralColor = Color.BLUE;
		private ArrayList<Float> threshold;
		private Color thresholdColor = Color.GREEN;
		private ArrayList<Float> peaks;
		private Color peaksColor = Color.YELLOW;
		private String title;
		private int width;
		private int height;
		private int samplesPerPixel;
		private int hopSize;
		private String fileName = null;
		private boolean play = false;

		/**
		 * The constructor that receives the main parameters of this task
		 * 
		 * @param spectralFlux
		 *            an arraylist containing the spectralFlux (another name for
		 *            spectral difference) to plot
		 * @param threshold
		 *            an arraylist containing the threshold values to plot
		 * @param peaks
		 *            an arraylist containing the peaks values to plot
		 * @param title
		 *            the title of the graph
		 * @param width
		 *            the width of the graph
		 * @param height
		 *            the height of the graph
		 * @param samplesPerPixel
		 *            the "resolution" of the graph
		 * @param hopSize
		 *            the hopsize, i.e., the overlap, in the plot it is used as
		 *            the "resolution" of the playing audio
		 */
		public PlottingCalcTask(ArrayList<Float> spectralFlux,
				ArrayList<Float> threshold, ArrayList<Float> peaks,
				String title, int width, int height, int samplesPerPixel,
				int hopSize) {
			this.spectralFlux = spectralFlux;
			this.threshold = threshold;
			this.peaks = peaks;
			this.title = title;
			this.width = width;
			this.height = height;
			this.samplesPerPixel = samplesPerPixel;
			this.hopSize = hopSize;

		}

		/**
		 * The constructor that receives the main parameters of this task and
		 * also the parameters to play the audio
		 * 
		 * @param spectralFlux
		 *            an arraylist containing the spectralFlux (another name for
		 *            spectral difference) to plot
		 * @param threshold
		 *            an arraylist containing the threshold values to plot
		 * @param peaks
		 *            an arraylist containing the peaks values to plot
		 * @param title
		 *            the title of the graph
		 * @param width
		 *            the width of the graph
		 * @param height
		 *            the height of the graph
		 * @param samplesPerPixel
		 *            the "resolution" of the graph
		 * @param hopSize
		 *            the hopsize, i.e., the overlap, in the plot it is used as
		 *            the "resolution" of the playing audio
		 * @param play
		 *            a boolean that tells wether to play the audio or not
		 * @param fileName
		 *            the full path of the file from where to read the audio
		 */
		public PlottingCalcTask(ArrayList<Float> spectralFlux,
				ArrayList<Float> threshold, ArrayList<Float> peaks,
				String title, int width, int height, int samplesPerPixel,
				int hopSize, boolean play, String fileName) {
			this.spectralFlux = spectralFlux;
			this.threshold = threshold;
			this.peaks = peaks;
			this.title = title;
			this.width = width;
			this.height = height;
			this.samplesPerPixel = samplesPerPixel;
			this.hopSize = hopSize;
			this.play = play;
			this.fileName = fileName;
		}

		@Override
		protected Void doInBackground() throws Exception {
			/*
			 * System.out.println("Plotting Calculations starting (play = " +
			 * play + ") ; peaks : " + !peaks.isEmpty() + "; threshold: " +
			 * !threshold.isEmpty() + "; flux: " + !spectralFlux.isEmpty());
			 */

			System.out.println("Plotting task is starting");

			Plot plot = new Plot(title, width, height);

			System.out.println("Fim constructor0");

			if (spectralFlux == null && threshold == null && peaks == null) {
				JOptionPane.showMessageDialog(mainFrame.this,
						"Can't plot because all the calculations are empty",
						"Can't plot", JOptionPane.WARNING_MESSAGE);
			} else {
				System.out.println("Fim constructor");

				if (spectralFlux != null) {
					plot.plot(spectralFlux, 1, spectralColor);
					System.out.println("Plot spect");
				}
				if (threshold != null) {
					plot.plot(threshold, 1, thresholdColor);
					System.out.println("Plot Threshold");
				}
				if (peaks != null) {
					plot.plot(peaks, 1, peaksColor);
					System.out.println("Plot Peaks");
				}

				if (play) {

					AudioDecoder decoder = null;

					String extension = FileUtils.getExtension(fileName);

					System.out.println("Extension = " + extension);

					if (extension.equals(".mp3")) {
						try {
							decoder = new MP3Decoder(new FileInputStream(
									fileName));

						} catch (FileNotFoundException ex) {
							JOptionPane.showMessageDialog(mainFrame.this,
									"Can't open the decoder because the file "
											+ fileName + " was not found!",
									"File not found.",
									JOptionPane.WARNING_MESSAGE);
						} catch (Exception ex) {
							JOptionPane
									.showMessageDialog(mainFrame.this,
											"It's no possible to decode "
													+ fileName + " because "
													+ ex.getMessage(),
											"Can't decode",
											JOptionPane.WARNING_MESSAGE);
						}
					} else if (extension.equals(".wav")
							|| extension.equals(".wave")) {
						try {
							decoder = new WavDecoder(new FileInputStream(
									fileName));
						} catch (FileNotFoundException ex) {
							JOptionPane.showMessageDialog(mainFrame.this,
									"Can't open the decoder because the file "
											+ fileName + " was not found!",
									"File not found.",
									JOptionPane.WARNING_MESSAGE);
						} catch (Exception ex) {
							JOptionPane
									.showMessageDialog(mainFrame.this,
											"It's no possible to decode "
													+ fileName + " because "
													+ ex.getMessage(),
											"Can't decode",
											JOptionPane.WARNING_MESSAGE);
						}
					}

					try {

						plot.PlayInPlot(hopSize, decoder);

					} catch (Exception ex) {
						JOptionPane.showMessageDialog(mainFrame.this,
								"It's not possible to play " + fileName
										+ " because " + ex.getMessage(),
								"Can't play", JOptionPane.WARNING_MESSAGE);
					}
				}
			}

			return null;

		}

		@Override
		protected void done() {
			super.done();
			System.out.println("Plotting Calculation Task is done");

			jProgressBarTasks.setIndeterminate(false);
			jProgressBarTasks.setValue(0);
			jLabelProgress.setText("");

			/*
			 * try { this.finalize(); } catch (Throwable ex) {
			 * Logger.getLogger(mainFrame.class.getName()).log(Level.SEVERE,
			 * null, ex); }
			 */
		}
	}

	/**
	 * An auxiliar methos that checks if a combo box contais a particular object
	 * 
	 * @param combo
	 *            the combobox to analyse
	 * @param o
	 *            the object to check if it exists in the combo
	 * @return true/false wether it exists in the combo
	 */
	private boolean comboContains(JComboBox combo, Object o) {
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
	 * Fills the text boxes in the configure Dialog with the values saved in the
	 * preferences Note: the second parameter in the getInt/getFloat methods of
	 * the preferences is the default value
	 */
	private void fillsConfigureDialogValues() {
		jTextFieldHopSize.setText(Integer.toString(prefs.getInt("HOPSIZE",
				HOPSIZE_DEFAULT)));
		jTextFieldSamplesPerPixel.setText(Integer.toString(prefs.getInt(
				"WINDOWSIZE", WINDOWSIZE_DEFAULT)));
		jTextFieldMultiplier.setText(Float.toString(prefs.getFloat(
				"MULTIPLIER", MULTIPLIER_DEFAULT)));
		jTextFieldThresholdWindow.setText(Integer.toString(prefs.getInt(
				"THRESHOLDWINDOW", THRESHOLD_WINDOW_DEFAULT)));
		jComboBoxPlayOnPlot.setSelectedItem(Boolean.toString(prefs.getBoolean(
				"PLAYONPLOT", PLAYONPLOT_DEFAULT)));
	}

	/**
	 * Saves the preferences from the values filled in the text boxes
	 */
	private void setPreferencesFromConfigureDialog() {
		prefs.putInt("WINDOWSIZE", Integer.parseInt(jTextFieldSamplesPerPixel
				.getText()));
		prefs.putInt("HOPSIZE", Integer.parseInt(jTextFieldHopSize.getText()));
		prefs.putFloat("MULTIPLIER", Float.parseFloat(jTextFieldMultiplier
				.getText()));
		prefs.putInt("THRESHOLDWINDOW", Integer
				.parseInt(jTextFieldThresholdWindow.getText()));
		System.out.println("PLAYONPLOT "
				+ Boolean.parseBoolean(jComboBoxPlayOnPlot.getSelectedItem()
						.toString()));
		prefs.putBoolean("PLAYONPLOT", Boolean.parseBoolean(jComboBoxPlayOnPlot
				.getSelectedItem().toString()));
	}

	/**
	 * Restores the Default preferences and fills the values in the dialog
	 * accordingly
	 */
	private void restoreDefaultPreferences() {
		prefs.putInt("WINDOWSIZE", WINDOWSIZE_DEFAULT);
		prefs.putInt("HOPSIZE", HOPSIZE_DEFAULT);
		prefs.putFloat("MULTIPLIER", MULTIPLIER_DEFAULT);
		prefs.putInt("THRESHOLDWINDOW", THRESHOLD_WINDOW_DEFAULT);
		prefs.putBoolean("PLAYONPLOT", PLAYONPLOT_DEFAULT);
		fillsConfigureDialogValues();
	}

	/**
	 * Sets the default preferences. It only sets if the preference doens't
	 * already exist, i.e., on the first time the program is runned
	 */
	private void setDefaultPreferences() throws BackingStoreException {

		if (!arrayContainsString(prefs.keys(), "WINDOWSIZE")) {
			prefs.putInt("WINDOWSIZE", WINDOWSIZE_DEFAULT);
		}
		if (!arrayContainsString(prefs.keys(), "HOPSIZE")) {
			prefs.putInt("HOPSIZE", HOPSIZE_DEFAULT);
		}
		if (!arrayContainsString(prefs.keys(), "MULTIPLIER")) {
			prefs.putFloat("MULTIPLIER", MULTIPLIER_DEFAULT);
		}
		if (!arrayContainsString(prefs.keys(), "THRESHOLDWINDOW")) {
			prefs.putInt("THRESHOLDWINDOW", THRESHOLD_WINDOW_DEFAULT);
		}
		if (!arrayContainsString(prefs.keys(), "PLAYONPLOT")) {
			prefs.putBoolean("PLAYONPLOT", PLAYONPLOT_DEFAULT);
		}
	}

	/**
	 * An auxiliar method that checks if an array of strings contains a
	 * particular string
	 * 
	 * @param arr
	 *            the array where to look
	 * @param strToFind
	 *            the String to find
	 * @return true if the string is found, false otherwise
	 */
	private boolean arrayContainsString(String[] arr, String strToFind) {
		boolean contains = false;
		for (String element : arr) {
			if (element.equals(strToFind)) {
				contains = true;
			}
		}

		return contains;
	}

	/**
	 * The main method of this class. It is responsible for starting the program
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {
				new mainFrame().setVisible(true);
			}
		});
	}

	// Swing variables declaration
	private javax.swing.JButton jButtonClose;
	private javax.swing.JButton jButtonConfigCancel;
	private javax.swing.JButton jButtonConfigDefault;
	private javax.swing.JButton jButtonConfigOk;
	private javax.swing.JCheckBox jCheckBoxPeaks;
	private javax.swing.JCheckBox jCheckBoxSpectFlux;
	private javax.swing.JCheckBox jCheckBoxThreshold;
	javax.swing.JComboBox jComboBoxFiles;
	private javax.swing.JComboBox jComboBoxPlayOnPlot;
	private javax.swing.JDialog jDialogAbout;
	private javax.swing.JDialog jDialogConfigure;
	private javax.swing.JFileChooser jFileChooserImportAudio;
	private javax.swing.JFileChooser jFileChooserProject;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabelHopSize;
	private javax.swing.JLabel jLabelHopSize1;
	private javax.swing.JLabel jLabelHopSize2;
	private javax.swing.JLabel jLabelMultiplier;
	private javax.swing.JLabel jLabelMultiplier1;
	private javax.swing.JLabel jLabelPlayOnPlot;
	private javax.swing.JLabel jLabelProgress;
	private javax.swing.JLabel jLabelSamplesPerPixel;
	private javax.swing.JLabel jLabelSamplesPerPixel1;
	private javax.swing.JLabel jLabelThresholdWindow;
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
	private javax.swing.JMenuItem jMenuItemPlotCalc;
	private javax.swing.JMenuItem jMenuItemPlotSamples;
	private javax.swing.JMenuItem jMenuItemSave;
	private javax.swing.JMenu jMenuOptions;
	private javax.swing.JMenu jMenuPlot;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JProgressBar jProgressBarTasks;
	private javax.swing.JPopupMenu.Separator jSeparator1;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTextField jTextFieldHopSize;
	private javax.swing.JTextField jTextFieldMultiplier;
	private javax.swing.JTextField jTextFieldSamplesPerPixel;
	private javax.swing.JTextField jTextFieldThresholdWindow;
	private javax.swing.JToolBar jToolBarBottom;
	private javax.swing.JToolBar jToolBarTop;
	// End of Swing variables declaration
}
