package ch.supertomcat.bilderuploader.gui.templates;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.gui.SpringUtilities;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.templates.TemplateManager;
import ch.supertomcat.bilderuploader.templates.UploadedFilesContainer;
import ch.supertomcat.bilderuploader.templates.filenameparser.TitleFilenameParser;
import ch.supertomcat.bilderuploader.templates.filenameparser.TitleFilenameParserContainer;
import ch.supertomcat.bilderuploader.templates.filenameparser.TitleFilenameParserManager;
import ch.supertomcat.bilderuploader.upload.UploadFile;
import ch.supertomcat.bilderuploader.util.BUUtil;
import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.supertomcatutils.clipboard.ClipboardUtil;
import ch.supertomcat.supertomcatutils.gui.Localization;
import ch.supertomcat.supertomcatutils.gui.combobox.renderer.FilenameComboBoxRenderer;
import ch.supertomcat.supertomcatutils.gui.copyandpaste.JTextComponentCopyAndPaste;
import ch.supertomcat.supertomcatutils.gui.dialog.FileDialogUtil;
import ch.supertomcat.supertomcatutils.io.FileUtil;
import de.sciss.syntaxpane.DefaultSyntaxKit;

/**
 * Dialog for selecting a template
 */
public class OutputGeneratorDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Date Format for save folder
	 */
	private static final DateTimeFormatter DATE_FORMAT_FOLDER = DateTimeFormatter.ofPattern("yyyy-MM");

	/**
	 * Date Format for save folder
	 */
	private static final DateTimeFormatter DATE_FORMAT_FILENAME = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

	/**
	 * Logger for this class
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Panel Template Selection
	 */
	private JPanel pnlTemplates = new JPanel(new SpringLayout());

	/**
	 * Main Template Label
	 */
	private JLabel lblMainTemplate = new JLabel(Localization.getString("MainTemplate"));

	/**
	 * Main Template ComboBox
	 */
	private JComboBox<File> cmbMainTemplate = new JComboBox<>();

	/**
	 * Footer Template Label
	 */
	private JLabel lblFooterTemplate = new JLabel(Localization.getString("FooterTemplate"));

	/**
	 * Footer Template ComboBox
	 */
	private JComboBox<File> cmbFooterTemplate = new JComboBox<>();

	/**
	 * Title Filename Parser CheckBox
	 */
	private JCheckBox cbTitleFilenameParser = new JCheckBox(Localization.getString("TitleFilenameParser"), false);

	/**
	 * Title Filename Parser ComboBox
	 */
	private JComboBox<File> cmbTitleFilenameParser = new JComboBox<>();

	/**
	 * Title Filename Parser Use All CheckBox
	 */
	private JCheckBox cbTitleFilenameParserUseAll = new JCheckBox(Localization.getString("TitleFilenameParserUseAll"), false);

	/**
	 * Output Text Area
	 */
	private JEditorPane txtOutput = new JEditorPane();

	/**
	 * Panel for Buttons
	 */
	private JPanel pnlButtons = new JPanel();

	/**
	 * Button
	 */
	private JButton btnGenerate = new JButton(Localization.getString("Generate"));

	/**
	 * Button
	 */
	private JButton btnCopy = new JButton(Localization.getString("Copy"));

	/**
	 * Button
	 */
	private JButton btnSave = new JButton(Localization.getString("Save"));

	/**
	 * Button
	 */
	private JButton btnSaveAs = new JButton(Localization.getString("Save") + "...");

	/**
	 * Button
	 */
	private JButton btnClose = new JButton(Localization.getString("Close"));

	/**
	 * Template Manager
	 */
	private final TemplateManager templateManager;

	/**
	 * Title Filename Parser Manager
	 */
	private final TitleFilenameParserManager titleFilenameParserManager;

	/**
	 * Settings Manager
	 */
	private final SettingsManager settingsManager;

	/**
	 * Folder for saving template output
	 */
	private File saveFolder = null;

	/**
	 * Files
	 */
	private final List<UploadFile> files;

	/**
	 * Uploaded Files Container of last generation
	 */
	private UploadedFilesContainer uploadedFilesContainer = null;

	/**
	 * Title Filename Parser Container of last generation
	 */
	private TitleFilenameParserContainer titleFilenameParserContainer = null;

	/**
	 * Constructor
	 * 
	 * @param owner Owner
	 * @param templateManager Template Manager
	 * @param titleFilenameParserManager Title Filename Parser Manager
	 * @param settingsManager Settings Manager
	 * @param files Files
	 */
	@SuppressWarnings("unchecked")
	public OutputGeneratorDialog(Window owner, TemplateManager templateManager, TitleFilenameParserManager titleFilenameParserManager, SettingsManager settingsManager, List<UploadFile> files) {
		super(owner, Localization.getString("OutputGenerator"));
		this.templateManager = templateManager;
		this.titleFilenameParserManager = titleFilenameParserManager;
		this.settingsManager = settingsManager;
		String saveFolderPath = settingsManager.getDirectorySettings().getGeneratorOutputSaveDirectory();
		if (saveFolderPath != null) {
			this.saveFolder = new File(saveFolderPath);
		}
		this.files = files;
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setLayout(new BorderLayout());

		FilenameComboBoxRenderer filenameComboBoxRenderer = new FilenameComboBoxRenderer();

		// Main Template
		cmbMainTemplate.setRenderer(filenameComboBoxRenderer);
		String strSelectedMainTemplate = settingsManager.getGUISettings().getSelectedMainTemplate();
		File selectedMainTemplate = null;
		for (File file : templateManager.getMainTemplateFiles()) {
			cmbMainTemplate.addItem(file);
			if (strSelectedMainTemplate != null && file.getName().equals(strSelectedMainTemplate)) {
				selectedMainTemplate = file;
			}
		}
		if (selectedMainTemplate != null) {
			cmbMainTemplate.setSelectedItem(selectedMainTemplate);
		}

		cmbMainTemplate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					File selectedMainTemplate = (File)e.getItem();
					if (selectedMainTemplate != null) {
						settingsManager.getGUISettings().setSelectedMainTemplate(selectedMainTemplate.getName());
						settingsManager.writeSettings(true);
					}
				}
			}
		});

		// Footer Template
		cmbFooterTemplate.setRenderer(filenameComboBoxRenderer);
		String strSelectedFooterTemplate = settingsManager.getGUISettings().getSelectedFooterTemplate();
		File selectedFooterTemplate = null;
		for (File file : templateManager.getFooterTemplateFiles()) {
			cmbFooterTemplate.addItem(file);
			if (strSelectedFooterTemplate != null && file.getName().equals(strSelectedFooterTemplate)) {
				selectedFooterTemplate = file;
			}
		}
		if (selectedFooterTemplate != null) {
			cmbFooterTemplate.setSelectedItem(selectedFooterTemplate);
		}

		cmbFooterTemplate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					File selectedFooterTemplate = (File)e.getItem();
					if (selectedFooterTemplate != null) {
						settingsManager.getGUISettings().setSelectedFooterTemplate(selectedFooterTemplate.getName());
						settingsManager.writeSettings(true);
					}
				}
			}
		});

		// Title Filename Parser
		cmbTitleFilenameParser.setRenderer(filenameComboBoxRenderer);
		String strSelectedTitleFilenameParser = settingsManager.getGUISettings().getSelectedTitleFilenameParser();
		File selectedTitleFilenameParser = null;
		for (File file : titleFilenameParserManager.getFilenameParserFiles()) {
			cmbTitleFilenameParser.addItem(file);
			if (strSelectedTitleFilenameParser != null && file.getName().equals(strSelectedTitleFilenameParser)) {
				selectedTitleFilenameParser = file;
			}
		}
		if (selectedTitleFilenameParser != null) {
			cmbTitleFilenameParser.setSelectedItem(selectedTitleFilenameParser);
		}

		cmbTitleFilenameParser.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					File selectedTitleFilenameParser = (File)e.getItem();
					if (selectedTitleFilenameParser != null) {
						settingsManager.getGUISettings().setSelectedTitleFilenameParser(selectedTitleFilenameParser.getName());
						settingsManager.writeSettings(true);
					}
				}
			}
		});

		cbTitleFilenameParser.setSelected(settingsManager.getGUISettings().isTitleFilenameParserEnabled());
		cbTitleFilenameParserUseAll.setSelected(settingsManager.getGUISettings().isTitleFilenameParserUseAll());

		cmbTitleFilenameParser.setEnabled(cbTitleFilenameParser.isSelected() && !cbTitleFilenameParserUseAll.isSelected());
		cbTitleFilenameParserUseAll.setEnabled(cbTitleFilenameParser.isSelected());

		cbTitleFilenameParser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmbTitleFilenameParser.setEnabled(cbTitleFilenameParser.isSelected() && !cbTitleFilenameParserUseAll.isSelected());
				cbTitleFilenameParserUseAll.setEnabled(cbTitleFilenameParser.isSelected());
				settingsManager.getGUISettings().setTitleFilenameParserEnabled(cbTitleFilenameParser.isSelected());
				settingsManager.writeSettings(true);
			}
		});

		cbTitleFilenameParserUseAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmbTitleFilenameParser.setEnabled(cbTitleFilenameParser.isSelected() && !cbTitleFilenameParserUseAll.isSelected());
				settingsManager.getGUISettings().setTitleFilenameParserUseAll(cbTitleFilenameParserUseAll.isSelected());
				settingsManager.writeSettings(true);
			}
		});

		pnlTemplates.add(lblMainTemplate);
		pnlTemplates.add(cmbMainTemplate);
		pnlTemplates.add(new JLabel());
		pnlTemplates.add(lblFooterTemplate);
		pnlTemplates.add(cmbFooterTemplate);
		pnlTemplates.add(new JLabel());

		if (cmbTitleFilenameParser.getItemCount() > 0) {
			pnlTemplates.add(cbTitleFilenameParser);
			pnlTemplates.add(cmbTitleFilenameParser);
			pnlTemplates.add(cbTitleFilenameParserUseAll);
			SpringUtilities.makeCompactGrid(pnlTemplates, 3, 3, 5, 5, 5, 5);
		} else {
			SpringUtilities.makeCompactGrid(pnlTemplates, 2, 3, 5, 5, 5, 5);
		}

		DefaultSyntaxKit.initKit();

		txtOutput.setContentType("text/plain");
		FontMetrics fontMetrics = txtOutput.getFontMetrics(txtOutput.getFont());
		int fontHeight = fontMetrics.getLeading() + fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
		txtOutput.setPreferredSize(new Dimension(120 * fontMetrics.charWidth('A'), 20 * fontHeight));

		btnGenerate.addActionListener(e -> generateTemplateOutput());
		pnlButtons.add(btnGenerate);

		btnCopy.addActionListener(e -> copyTemplateOutput());
		pnlButtons.add(btnCopy);

		btnSave.addActionListener(e -> saveTemplateOutput());
		pnlButtons.add(btnSave);

		btnSaveAs.addActionListener(e -> saveAsTemplateOutput());
		pnlButtons.add(btnSaveAs);

		btnClose.addActionListener(e -> dispose());
		pnlButtons.add(btnClose);

		add(pnlTemplates, BorderLayout.NORTH);
		add(new JScrollPane(txtOutput), BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(super.getOwner());

		JTextComponentCopyAndPaste.addCopyAndPasteMouseListener(txtOutput);

		// Enter und Escape (bevor setVisible(true)!)
		ActionMap am = getRootPane().getActionMap();
		InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		Object windowCloseKey = new Object();
		Object windowOkKey = new Object();
		KeyStroke windowCloseStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		Action windowCloseAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				btnClose.doClick();
			}
		};
		KeyStroke windowOkStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		Action windowOkAction = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				btnGenerate.doClick();
			}
		};
		im.put(windowCloseStroke, windowCloseKey);
		am.put(windowCloseKey, windowCloseAction);
		im.put(windowOkStroke, windowOkKey);
		am.put(windowOkKey, windowOkAction);

		setVisible(true);
	}

	/**
	 * Generate Template Output
	 */
	private void generateTemplateOutput() {
		File selectedMainTemplate = (File)cmbMainTemplate.getSelectedItem();
		if (selectedMainTemplate == null) {
			return;
		}
		File selectedFooterTemplate = (File)cmbFooterTemplate.getSelectedItem();
		try {
			Map<String, Object> vars = new LinkedHashMap<>();
			uploadedFilesContainer = new UploadedFilesContainer(files);
			vars.put("uploadedFiles", uploadedFilesContainer);

			List<TitleFilenameParser> titleFilenameParsers;
			if (cbTitleFilenameParser.isSelected()) {
				if (cbTitleFilenameParserUseAll.isSelected()) {
					titleFilenameParsers = titleFilenameParserManager.getTitleFilenameParsers();
				} else {
					titleFilenameParsers = Arrays.asList(titleFilenameParserManager.getTitleFilenameParser(((File)cmbTitleFilenameParser.getSelectedItem()).getName()));
				}
			} else {
				titleFilenameParsers = Collections.emptyList();
			}

			titleFilenameParserContainer = new TitleFilenameParserContainer(titleFilenameParsers);
			vars.put("titleFilenameParser", titleFilenameParserContainer);
			vars.put("escapeTool", new EscapeTool());
			vars.put("dateTool", new DateTool());
			vars.put("applicationName", ApplicationProperties.getProperty("ApplicationName"));
			vars.put("applicationShortName", ApplicationProperties.getProperty("ApplicationShortName"));
			vars.put("applicationVersion", ApplicationProperties.getProperty("ApplicationVersion"));

			if (selectedFooterTemplate != null) {
				vars.put("footerTemplate", "footers/" + selectedFooterTemplate.getName());
			}

			long start = System.nanoTime();
			String renderedTemplate = templateManager.renderTemplate(selectedMainTemplate.getName(), vars);
			long end = System.nanoTime();
			long duration = TimeUnit.MILLISECONDS.convert(end - start, TimeUnit.NANOSECONDS);
			logger.debug("Rendered Main Template {} in {}ms:\n{}", selectedMainTemplate, duration, renderedTemplate);

			txtOutput.setText(renderedTemplate);
			txtOutput.setCaretPosition(0);
		} catch (Exception e) {
			logger.error("Could not render template: {}", selectedMainTemplate.getAbsolutePath(), e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String stackTrace = sw.toString();
			txtOutput.setText(stackTrace);
			txtOutput.setCaretPosition(0);
		} finally {
			System.gc();
		}
	}

	private void saveToFile(File outputFolder, File outputFile) {
		try {
			Files.createDirectories(outputFolder.toPath());
		} catch (IOException e) {
			logger.error("Could not create directory: {}", outputFolder.getAbsolutePath(), e);
			JOptionPane.showMessageDialog(super.getOwner(), "Could not create directory: " + outputFolder.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try (FileOutputStream out = new FileOutputStream(outputFile); OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {
			writer.write(txtOutput.getText());
			writer.flush();
		} catch (IOException e) {
			logger.error("Could not save template output to file: {}", outputFile.getAbsolutePath(), e);
			JOptionPane.showMessageDialog(super.getOwner(), "Could not save template output to file: " + outputFile.getAbsolutePath(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Copy Template Output
	 */
	private void copyTemplateOutput() {
		ClipboardUtil.setClipboardContent(txtOutput.getText());
	}

	/**
	 * Save Template Output
	 */
	private void saveTemplateOutput() {
		String postType;
		File selectedMainTemplate = (File)cmbMainTemplate.getSelectedItem();
		if (selectedMainTemplate != null) {
			postType = FileUtil.getFilePrefix(selectedMainTemplate.getName());
		} else {
			postType = "Unkown";
		}

		String postTitle;
		if (titleFilenameParserContainer != null && !titleFilenameParserContainer.getTitles().isEmpty() && titleFilenameParserContainer.getTitles().get(0).getFormattedTitle() != null) {
			postTitle = titleFilenameParserContainer.getTitles().get(0).getFormattedTitle();
		} else if (uploadedFilesContainer != null && !uploadedFilesContainer.getFolderNames().isEmpty()) {
			postTitle = uploadedFilesContainer.getFolderNames().get(0);
		} else {
			postTitle = "Unkown";
		}

		LocalDateTime now = LocalDateTime.now();
		String formattedDate = now.format(DATE_FORMAT_FILENAME);

		String fileName = postType + "-" + formattedDate + "-" + postTitle + ".txt";
		fileName = BUUtil.correctFileString(fileName, true);

		String formattedDateFolder = now.format(DATE_FORMAT_FOLDER);

		if (saveFolder == null) {
			File chosenSaveFolder = FileDialogUtil.showFolderOpenDialog(super.getOwner(), saveFolder, null);
			if (chosenSaveFolder != null) {
				String saveFolderPath = chosenSaveFolder.getAbsolutePath();
				settingsManager.getDirectorySettings().setGeneratorOutputSaveDirectory(saveFolderPath);
				settingsManager.writeSettings(true);
				saveFolder = chosenSaveFolder;
			} else {
				return;
			}
		}

		File outputFolder = new File(saveFolder, formattedDateFolder);

		saveToFile(outputFolder, new File(outputFolder, fileName));
	}

	/**
	 * Save As Template Output
	 */
	private void saveAsTemplateOutput() {
		File chosenOutputFile = FileDialogUtil.showFileSaveDialog(super.getOwner(), settingsManager.getDirectorySettings().getLastUsedGeneratorOutputSaveDirectory(), null);
		if (chosenOutputFile != null) {
			File outputFolder = chosenOutputFile.getAbsoluteFile().getParentFile();
			settingsManager.getDirectorySettings().setLastUsedGeneratorOutputSaveDirectory(outputFolder.getAbsolutePath());
			settingsManager.writeSettings(true);
			saveToFile(outputFolder, chosenOutputFile);
		}
	}
}
