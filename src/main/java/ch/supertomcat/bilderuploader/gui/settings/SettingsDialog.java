package ch.supertomcat.bilderuploader.gui.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.supertomcat.bilderuploader.gui.MainWindow;
import ch.supertomcat.bilderuploader.settings.BUSettingsListener;
import ch.supertomcat.bilderuploader.settings.ProxyManager;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.settingsconfig.ConnectionSettings;
import ch.supertomcat.bilderuploader.settingsconfig.GUISettings;
import ch.supertomcat.bilderuploader.settingsconfig.LogLevelSetting;
import ch.supertomcat.bilderuploader.settingsconfig.LookAndFeelSetting;
import ch.supertomcat.bilderuploader.settingsconfig.ProgressDisplayMode;
import ch.supertomcat.bilderuploader.settingsconfig.ProxyMode;
import ch.supertomcat.bilderuploader.settingsconfig.Settings;
import ch.supertomcat.bilderuploader.settingsconfig.SizeDisplayMode;
import ch.supertomcat.bilderuploader.settingsconfig.UploadSettings;
import ch.supertomcat.bilderuploader.settingsconfig.WindowSettings;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.Localization;
import ch.supertomcat.supertomcatutils.gui.copyandpaste.JTextComponentCopyAndPaste;
import ch.supertomcat.supertomcatutils.gui.layout.GridBagLayoutUtil;

/**
 * Settings Dialog
 */
public class SettingsDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Logger for this class
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * TabbedPane
	 */
	private JTabbedPane tp = new JTabbedPane();

	/**
	 * Scrollpane
	 */
	private JScrollPane spConnection;

	/**
	 * Scrollpane
	 */
	private JScrollPane spGUI;

	/**
	 * Scrollpane
	 */
	private JScrollPane spUpload;

	/**
	 * Scrollpane
	 */
	private JScrollPane spOther;

	/**
	 * Panel
	 */
	private JPanel pnlConnection = new JPanel();

	/**
	 * Panel
	 */
	private JPanel pnlGUI = new JPanel();

	/**
	 * Panel
	 */
	private JPanel pnlUpload = new JPanel();

	/**
	 * Panel
	 */
	private JPanel pnlOther = new JPanel();

	/**
	 * Panel
	 */
	private JPanel pnlButtons = new JPanel();

	/**
	 * Label
	 */
	private JLabel lblConnectionCount = new JLabel(Localization.getString("ConnectionCount"));

	/**
	 * TextField
	 */
	private JTextField txtConnectionCount = new JTextField("", 3);

	/**
	 * Slider
	 */
	private JSlider sldConnectionCount = new JSlider();

	/**
	 * Label
	 */
	private JLabel lblConnectionCountPerHost = new JLabel(Localization.getString("ConnectionCountPerHost"));

	/**
	 * TextField
	 */
	private JTextField txtConnectionCountPerHost = new JTextField("", 3);

	/**
	 * Slider
	 */
	private JSlider sldConnectionCountPerHost = new JSlider();

	/**
	 * Panel
	 */
	private JPanel pnlLogDays = new JPanel();

	/**
	 * Label
	 */
	private JLabel lblSaveLogs = new JLabel(Localization.getString("Logs"));

	/**
	 * CheckBox
	 */
	private JCheckBox chkSaveLogs = new JCheckBox(Localization.getString("SaveLogs"), false);

	/**
	 * Label
	 */
	private JLabel lblAutoStartUploads = new JLabel(Localization.getString("Uploads"));

	/**
	 * CheckBox
	 */
	private JCheckBox chkAutoStartUploads = new JCheckBox(Localization.getString("AutoStartUploads"), false);

	/**
	 * Label
	 */
	private JLabel lblLanguage = new JLabel(Localization.getString("Language"));

	/**
	 * ComboBox
	 */
	private JComboBox<String> cmbLanguage = new JComboBox<>();

	/**
	 * Label
	 */
	private JLabel lblSizeView = new JLabel(Localization.getString("SizeView"));

	/**
	 * ComboBox
	 */
	private JComboBox<SizeDisplayMode> cmbSizeView = new JComboBox<>();

	/**
	 * Label
	 */
	private JLabel lblProgressView = new JLabel(Localization.getString("ProgressView"));

	/**
	 * ComboBox
	 */
	private JComboBox<ProgressDisplayMode> cmbProgressView = new JComboBox<>();

	/**
	 * Label
	 */
	private JLabel lblUploadRate = new JLabel(Localization.getString("ProgressView"));

	/**
	 * CheckBox
	 */
	private JCheckBox chkUploadRate = new JCheckBox(Localization.getString("UploadRate"));

	/**
	 * CheckBox
	 */
	private JCheckBox cbAuth = new JCheckBox(Localization.getString("ProxyAuthentication"), false);

	/**
	 * Label
	 */
	private JLabel lblProxyName = new JLabel(Localization.getString("ProxyName"));

	/**
	 * TextField
	 */
	private JTextField txtProxyName = new JTextField("127.0.0.1", 20);

	/**
	 * Label
	 */
	private JLabel lblProxyPort = new JLabel(Localization.getString("ProxyPort"));

	/**
	 * TextField
	 */
	private JTextField txtProxyPort = new JTextField("80", 20);

	/**
	 * Label
	 */
	private JLabel lblProxyUser = new JLabel(Localization.getString("ProxyUser"));

	/**
	 * TextField
	 */
	private JTextField txtProxyUser = new JTextField("", 20);

	/**
	 * Label
	 */
	private JLabel lblProxyPassword = new JLabel(Localization.getString("ProxyPassword"));

	/**
	 * TextField
	 */
	private JPasswordField txtProxyPassword = new JPasswordField("", 20);

	/**
	 * ButtonGroup
	 */
	private ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * RadioButton
	 */
	private JRadioButton rbNoProxy = new JRadioButton(Localization.getString("ProxyDirectConnection"), true);

	/**
	 * RadioButton
	 */
	private JRadioButton rbHTTP = new JRadioButton(Localization.getString("ProxyHTTP"), false);

	/**
	 * Label
	 */
	private JLabel lblLAF = new JLabel(Localization.getString("LookAndFeel"));

	/**
	 * ComboBox
	 */
	private JComboBox<LookAndFeelSetting> cmbLAF = new JComboBox<>();

	/**
	 * Label
	 */
	private JLabel lblMainWindowSizePos = new JLabel(Localization.getString("MainWindow"));

	/**
	 * Checkbox
	 */
	private JCheckBox chkMainWindowSizePos = new JCheckBox(Localization.getString("WindowSizePos"), false);

	/**
	 * Label
	 */
	private JLabel lblDebugLevel = new JLabel("Debug-Level");

	/**
	 * ComboBox
	 */
	private JComboBox<LogLevelSetting> cmbLogLevel = new JComboBox<>();

	/**
	 * Panel
	 */
	private JPanel pnlMaxFailedCount = new JPanel();

	/**
	 * Label
	 */
	private JLabel lblMaxFailedCount = new JLabel(Localization.getString("Uploads"));

	/**
	 * TextField
	 */
	private JTextField txtMaxFailedCount = new JTextField("");

	/**
	 * Button
	 */
	private JButton btnMaxFailedCountPlus = new JButton("", Icons.getTangoIcon("actions/list-add.png", 16));

	/**
	 * Button
	 */
	private JButton btnMaxFailedCountMinus = new JButton("", Icons.getTangoIcon("actions/list-remove.png", 16));

	/**
	 * Label
	 */
	private JLabel lblConnectTimeout = new JLabel(Localization.getString("ConnectTimeout"));

	/**
	 * TextField
	 */
	private JTextField txtConnectTimeout = new JTextField("30000");

	/**
	 * Label
	 */
	private JLabel lblSocketTimeout = new JLabel(Localization.getString("SocketTimeout"));

	/**
	 * TextField
	 */
	private JTextField txtSocketTimeout = new JTextField("30000");

	/**
	 * Label
	 */
	private JLabel lblConnectionRequestTimeout = new JLabel(Localization.getString("ConnectionRequestTimeout"));

	/**
	 * TextField
	 */
	private JTextField txtConnectionRequestTimeout = new JTextField("30000");

	/**
	 * Label
	 */
	private JLabel lblBackupDB = new JLabel(Localization.getString("Backup"));

	/**
	 * CheckBox
	 */
	private JCheckBox chkBackupDB = new JCheckBox(Localization.getString("BackupDB"), false);

	/**
	 * Label
	 */
	private JLabel lblSaveTableColumnSizes = new JLabel(Localization.getString("Tables"));

	/**
	 * CheckBox
	 */
	private JCheckBox chkSaveTableColumnSizes = new JCheckBox(Localization.getString("SaveTableColumnSizes"), false);

	/**
	 * Label
	 */
	private JLabel lblSaveTableSortOrders = new JLabel(Localization.getString("Tables"));

	/**
	 * CheckBox
	 */
	private JCheckBox chkSaveTableSortOrders = new JCheckBox(Localization.getString("SaveTableSortOrders"), false);

	/**
	 * Label
	 */
	private JLabel lblUploadsCompleteNotification = new JLabel(Localization.getString("Notification"));

	/**
	 * CheckBox
	 */
	private JCheckBox chkUploadsCompleteNotification = new JCheckBox(Localization.getString("UploadsCompleteNotification"), false);

	/**
	 * Label
	 */
	private JLabel lblAutoRetryAfterUploadsComplete = new JLabel(Localization.getString("Uploads"));

	/**
	 * CheckBox
	 */
	private JCheckBox chkAutoRetryAfterUploadsComplete = new JCheckBox(Localization.getString("AutoRetryAfterUploadsComplete"), false);

	/**
	 * Label
	 */
	private JLabel lblUserAgent = new JLabel(Localization.getString("UserAgent"));

	/**
	 * TextField
	 */
	private JTextField txtUserAgent = new JTextField("");

	/**
	 * Button
	 */
	private JButton btnSave = new JButton(Localization.getString("SaveAndApply"), Icons.getTangoIcon("actions/document-save.png", 16));

	/**
	 * Button
	 */
	private JButton btnReset = new JButton(Localization.getString("Reset"), Icons.getTangoIcon("actions/edit-undo.png", 16));

	/**
	 * Button
	 */
	private JButton btnCancel = new JButton(Localization.getString("Cancel"), Icons.getTangoIcon("emblems/emblem-unreadable.png", 16));

	/**
	 * GridBagLayout
	 */
	private GridBagLayout gbl = new GridBagLayout();

	/**
	 * GridBagLayout
	 */
	private GridBagLayout gblConnection = new GridBagLayout();

	/**
	 * GridBagLayout
	 */
	private GridBagLayout gblGUI = new GridBagLayout();

	/**
	 * GridBagLayout
	 */
	private GridBagLayout gblUpload = new GridBagLayout();

	/**
	 * GridBagLayout
	 */
	private GridBagLayout gblOther = new GridBagLayout();

	/**
	 * GridBagLayoutUtil
	 */
	private GridBagLayoutUtil gblt = new GridBagLayoutUtil(5, 10, 5, 5);

	private final BUSettingsListener settingsListener = new BUSettingsListener() {

		@Override
		public void settingsChanged() {
			init();
		}

		@Override
		public void lookAndFeelChanged() {
			// Nothing to do
		}
	};

	/**
	 * Settings Manager
	 */
	private final SettingsManager settingsManager;

	/**
	 * Proxy Manager
	 */
	private final ProxyManager proxyManager;

	/**
	 * Owner
	 */
	private final MainWindow owner;

	/**
	 * Constructor
	 * 
	 * @param owner Owner
	 * @param settingsManager Settings Manager
	 * @param proxyManager Proxy Manager
	 */
	public SettingsDialog(MainWindow owner, SettingsManager settingsManager, ProxyManager proxyManager) {
		this.settingsManager = settingsManager;
		this.proxyManager = proxyManager;
		this.owner = owner;
		setTitle(Localization.getString("Settings"));
		setModal(true);
		setIconImage(Icons.getApplImage("BU.png"));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		setLayout(gbl);
		pnlConnection.setLayout(gblConnection);
		pnlGUI.setLayout(gblGUI);
		pnlUpload.setLayout(gblUpload);
		pnlOther.setLayout(gblOther);

		spConnection = new JScrollPane(pnlConnection);
		spGUI = new JScrollPane(pnlGUI);
		spUpload = new JScrollPane(pnlUpload);
		spOther = new JScrollPane(pnlOther);

		txtConnectionCount.setEditable(false);
		txtConnectionCountPerHost.setEditable(false);

		btnCancel.setMnemonic(KeyEvent.VK_C);
		pnlButtons.add(btnSave);
		pnlButtons.add(btnReset);
		pnlButtons.add(btnCancel);
		btnSave.addActionListener(e -> {
			applySettings();
			owner.setMessage(Localization.getString("SavingSettings"));
			boolean b = settingsManager.writeSettings(true);
			if (b) {
				owner.setMessage(Localization.getString("SettingsSaved"));
			} else {
				owner.setMessage(Localization.getString("SettingsSaveFailed"));
			}
			settingsManager.removeSettingsListener(settingsListener);
			dispose();
		});

		btnReset.addActionListener(e -> init());
		btnCancel.addActionListener(e -> {
			settingsManager.removeSettingsListener(settingsListener);
			dispose();
		});

		sldConnectionCount.setSnapToTicks(true);
		sldConnectionCount.setMajorTickSpacing(10);
		sldConnectionCount.setMinorTickSpacing(1);
		sldConnectionCount.setPaintTicks(true);
		sldConnectionCount.setPaintLabels(true);
		sldConnectionCount.setMinimum(0);
		sldConnectionCount.setMaximum(50);
		sldConnectionCount.addChangeListener(e -> {
			if (sldConnectionCount.getValue() == 0) {
				sldConnectionCount.setValue(1);
			} else {
				txtConnectionCount.setText(String.valueOf(sldConnectionCount.getValue()));
			}
		});

		sldConnectionCountPerHost.setSnapToTicks(true);
		sldConnectionCountPerHost.setMajorTickSpacing(10);
		sldConnectionCountPerHost.setMinorTickSpacing(1);
		sldConnectionCountPerHost.setPaintTicks(true);
		sldConnectionCountPerHost.setPaintLabels(true);
		sldConnectionCountPerHost.setMinimum(0);
		sldConnectionCountPerHost.setMaximum(50);
		sldConnectionCountPerHost.addChangeListener(e -> txtConnectionCountPerHost.setText(String.valueOf(sldConnectionCountPerHost.getValue())));

		txtConnectionCountPerHost.setToolTipText(Localization.getString("MaxConnectionCountToolTip"));
		sldConnectionCountPerHost.setToolTipText(Localization.getString("MaxConnectionCountToolTip"));

		pnlLogDays.add(new JLabel(Localization.getString("LogDaysT")));

		btnMaxFailedCountPlus.addActionListener(e -> {
			int val = Integer.parseInt(txtMaxFailedCount.getText());
			if (val < 20) {
				val += 1;
				txtMaxFailedCount.setText(String.valueOf(val));
			}
		});
		btnMaxFailedCountMinus.addActionListener(e -> {
			int val = Integer.parseInt(txtMaxFailedCount.getText());
			if (val > 0) {
				val -= 1;
				txtMaxFailedCount.setText(String.valueOf(val));
			}
		});

		txtMaxFailedCount.setEditable(false);
		txtMaxFailedCount.setColumns(5);
		pnlMaxFailedCount.add(new JLabel(Localization.getString("MaxFailedCountT1")));
		pnlMaxFailedCount.add(txtMaxFailedCount);
		pnlMaxFailedCount.add(btnMaxFailedCountPlus);
		pnlMaxFailedCount.add(btnMaxFailedCountMinus);
		pnlMaxFailedCount.add(new JLabel(Localization.getString("MaxFailedCountT2")));
		pnlMaxFailedCount.setToolTipText(Localization.getString("MaxFailedCountToolTip"));

		tp.setFocusable(false);
		tp.setTabPlacement(JTabbedPane.TOP);
		tp.addTab(Localization.getString("SettingsGUI"), Icons.getTangoIcon("apps/preferences-system-windows.png", 22), spGUI);
		tp.addTab(Localization.getString("SettingsConnection"), Icons.getTangoIcon("status/network-idle.png", 22), spConnection);
		tp.addTab(Localization.getString("SettingsUpload"), Icons.getTangoIcon("actions/go-down.png", 22), spUpload);
		tp.addTab(Localization.getString("SettingsOther"), Icons.getTangoIcon("categories/preferences-system.png", 22), spOther);

		buttonGroup.add(rbNoProxy);
		buttonGroup.add(rbHTTP);
		txtProxyPassword.setEchoChar('*');

		cmbSizeView.addItem(SizeDisplayMode.AUTO_CHANGE_SIZE);
		cmbSizeView.addItem(SizeDisplayMode.ONLY_B);
		cmbSizeView.addItem(SizeDisplayMode.ONLY_KIB);
		cmbSizeView.addItem(SizeDisplayMode.ONLY_MIB);
		cmbSizeView.addItem(SizeDisplayMode.ONLY_GIB);
		cmbSizeView.addItem(SizeDisplayMode.ONLY_TIB);
		cmbSizeView.setRenderer(new SizeDisplayModeComboBoxRenderer());

		cmbProgressView.addItem(ProgressDisplayMode.PROGRESSBAR_PERCENT);
		cmbProgressView.addItem(ProgressDisplayMode.PROGRESSBAR_SIZE);
		cmbProgressView.setRenderer(new ProgressDisplayModeComboBoxRenderer());

		cmbLanguage.addItem(Localization.getString("German"));
		cmbLanguage.addItem(Localization.getString("English"));
		cmbLanguage.setToolTipText(Localization.getString("LanguageTooltip"));

		for (LookAndFeelSetting lookAndFeel : SettingsManager.getLookAndFeels()) {
			cmbLAF.addItem(lookAndFeel);
		}
		cmbLAF.setRenderer(new LookAndFeelComboBoxRenderer());

		cmbLogLevel.addItem(LogLevelSetting.TRACE);
		cmbLogLevel.addItem(LogLevelSetting.DEBUG);
		cmbLogLevel.addItem(LogLevelSetting.INFO);
		cmbLogLevel.addItem(LogLevelSetting.WARN);
		cmbLogLevel.addItem(LogLevelSetting.ERROR);
		cmbLogLevel.addItem(LogLevelSetting.FATAL);
		cmbLogLevel.setSelectedItem(LogLevelSetting.INFO);

		chkSaveLogs.setToolTipText(Localization.getString("SaveLogsTooltip"));

		init();

		rbNoProxy.addChangeListener(e -> proxySettingChanged());
		rbHTTP.addChangeListener(e -> proxySettingChanged());
		cbAuth.addChangeListener(e -> {
			lblProxyUser.setEnabled(cbAuth.isSelected());
			lblProxyPassword.setEnabled(cbAuth.isSelected());
			txtProxyUser.setEnabled(cbAuth.isSelected());
			txtProxyPassword.setEnabled(cbAuth.isSelected());
		});

		GridBagConstraints gbc = new GridBagConstraints();

		// Connection
		int i = 0;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, rbNoProxy, pnlConnection);
		gbc = gblt.getGBC(1, i, 4, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, rbHTTP, pnlConnection);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, lblProxyName, pnlConnection);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, txtProxyName, pnlConnection);
		gbc = gblt.getGBC(2, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, lblProxyPort, pnlConnection);
		gbc = gblt.getGBC(3, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, txtProxyPort, pnlConnection);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, cbAuth, pnlConnection);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, lblProxyUser, pnlConnection);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, txtProxyUser, pnlConnection);
		gbc = gblt.getGBC(2, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, lblProxyPassword, pnlConnection);
		gbc = gblt.getGBC(3, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, txtProxyPassword, pnlConnection);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, lblConnectionCount, pnlConnection);
		gbc = gblt.getGBC(1, i, 3, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, sldConnectionCount, pnlConnection);
		gbc = gblt.getGBC(5, i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, txtConnectionCount, pnlConnection);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, lblConnectionCountPerHost, pnlConnection);
		gbc = gblt.getGBC(1, i, 3, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, sldConnectionCountPerHost, pnlConnection);
		gbc = gblt.getGBC(5, i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, txtConnectionCountPerHost, pnlConnection);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, lblConnectTimeout, pnlConnection);
		gbc = gblt.getGBC(1, i, 3, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, txtConnectTimeout, pnlConnection);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, lblSocketTimeout, pnlConnection);
		gbc = gblt.getGBC(1, i, 3, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, txtSocketTimeout, pnlConnection);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, lblConnectionRequestTimeout, pnlConnection);
		gbc = gblt.getGBC(1, i, 3, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, txtConnectionRequestTimeout, pnlConnection);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, lblUserAgent, pnlConnection);
		gbc = gblt.getGBC(1, i, 3, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblConnection, gbc, txtUserAgent, pnlConnection);

		// GUI
		i = 0;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, lblLAF, pnlGUI);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, cmbLAF, pnlGUI);
		// i++;
		gbc = gblt.getGBC(2, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, lblLanguage, pnlGUI);
		gbc = gblt.getGBC(3, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, cmbLanguage, pnlGUI);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, lblSizeView, pnlGUI);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, cmbSizeView, pnlGUI);
		// i++;
		gbc = gblt.getGBC(2, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, lblProgressView, pnlGUI);
		gbc = gblt.getGBC(3, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, cmbProgressView, pnlGUI);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, lblUploadRate, pnlGUI);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, chkUploadRate, pnlGUI);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, lblMainWindowSizePos, pnlGUI);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, chkMainWindowSizePos, pnlGUI);
		gbc = gblt.getGBC(2, i, 1, 1, 0.0, 0.0);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, lblSaveTableColumnSizes, pnlGUI);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, chkSaveTableColumnSizes, pnlGUI);
		gbc = gblt.getGBC(2, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, lblSaveTableSortOrders, pnlGUI);
		gbc = gblt.getGBC(3, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, chkSaveTableSortOrders, pnlGUI);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, lblUploadsCompleteNotification, pnlGUI);
		gbc = gblt.getGBC(1, i, 3, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblGUI, gbc, chkUploadsCompleteNotification, pnlGUI);

		// Upload
		i = 0;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblUpload, gbc, lblSaveLogs, pnlUpload);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblUpload, gbc, chkSaveLogs, pnlUpload);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblUpload, gbc, lblAutoStartUploads, pnlUpload);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblUpload, gbc, chkAutoStartUploads, pnlUpload);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblUpload, gbc, lblAutoRetryAfterUploadsComplete, pnlUpload);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblUpload, gbc, chkAutoRetryAfterUploadsComplete, pnlUpload);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblUpload, gbc, lblMaxFailedCount, pnlUpload);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblUpload, gbc, pnlMaxFailedCount, pnlUpload);

		// Other
		i = 0;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblOther, gbc, lblBackupDB, pnlOther);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblOther, gbc, chkBackupDB, pnlOther);
		i++;
		gbc = gblt.getGBC(0, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblOther, gbc, lblDebugLevel, pnlOther);
		gbc = gblt.getGBC(1, i, 1, 1, 0.0, 0.0);
		GridBagLayoutUtil.addItemToPanel(gblOther, gbc, cmbLogLevel, pnlOther);

		gbc = new GridBagConstraints();
		gbc = gblt.getGBC(0, 0, 1, 1, 1.0, 0.8);
		GridBagLayoutUtil.addItemToDialog(gbl, gbc, tp, this);
		gbc = gblt.getGBC(0, 1, 1, 1, 0.1, 0.0);
		GridBagLayoutUtil.addItemToDialog(gbl, gbc, pnlButtons, this);

		JTextComponentCopyAndPaste.addCopyAndPasteMouseListener(txtMaxFailedCount);
		JTextComponentCopyAndPaste.addCopyAndPasteMouseListener(txtProxyName);
		JTextComponentCopyAndPaste.addCopyAndPasteMouseListener(txtProxyPort);
		JTextComponentCopyAndPaste.addCopyAndPasteMouseListener(txtProxyUser);
		JTextComponentCopyAndPaste.addCopyAndPasteMouseListener(txtConnectTimeout);
		JTextComponentCopyAndPaste.addCopyAndPasteMouseListener(txtSocketTimeout);
		JTextComponentCopyAndPaste.addCopyAndPasteMouseListener(txtConnectionRequestTimeout);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				settingsManager.removeSettingsListener(settingsListener);
				dispose();
			}
		});

		settingsManager.addSettingsListener(settingsListener);

		pack();
		setLocationRelativeTo(owner);

		setVisible(true);
	}

	/**
	 * Initialize
	 */
	private void init() {
		Settings settings = settingsManager.getSettings();

		ConnectionSettings connectionSettings = settings.getConnectionSettings();

		txtConnectionCount.setText(String.valueOf(connectionSettings.getMaxConnections()));
		sldConnectionCount.setValue(connectionSettings.getMaxConnections());

		txtConnectionCountPerHost.setText(String.valueOf(connectionSettings.getMaxConnectionsPerHost()));
		sldConnectionCountPerHost.setValue(connectionSettings.getMaxConnectionsPerHost());

		txtConnectTimeout.setText(String.valueOf(connectionSettings.getConnectTimeout()));
		txtSocketTimeout.setText(String.valueOf(connectionSettings.getSocketTimeout()));
		txtConnectionRequestTimeout.setText(String.valueOf(connectionSettings.getConnectionRequestTimeout()));

		txtUserAgent.setText(connectionSettings.getUserAgent());

		txtProxyName.setText(proxyManager.getProxyname());
		txtProxyPort.setText(String.valueOf(proxyManager.getProxyport()));
		txtProxyUser.setText(proxyManager.getProxyuser());
		txtProxyPassword.setText(proxyManager.getProxypassword());

		ProxyMode mode = proxyManager.getMode();
		if (mode == ProxyMode.DIRECT_CONNECTION) {
			rbNoProxy.setSelected(true);
		} else if (mode == ProxyMode.PROXY) {
			rbHTTP.setSelected(true);
		}
		boolean b = proxyManager.isAuth();
		if (b && (mode != ProxyMode.DIRECT_CONNECTION)) {
			cbAuth.setSelected(true);
		}

		lblProxyName.setEnabled(!rbNoProxy.isSelected());
		lblProxyPort.setEnabled(!rbNoProxy.isSelected());
		txtProxyName.setEnabled(!rbNoProxy.isSelected());
		txtProxyPort.setEnabled(!rbNoProxy.isSelected());
		cbAuth.setEnabled(!rbNoProxy.isSelected());

		lblProxyUser.setEnabled(cbAuth.isSelected());
		lblProxyPassword.setEnabled(cbAuth.isSelected());
		txtProxyUser.setEnabled(cbAuth.isSelected());
		txtProxyPassword.setEnabled(cbAuth.isSelected());

		UploadSettings uploadSettings = settings.getUploadSettings();

		txtMaxFailedCount.setText(String.valueOf(uploadSettings.getMaxFailedCount()));
		chkSaveLogs.setSelected(uploadSettings.isSaveLogs());
		chkAutoStartUploads.setSelected(uploadSettings.isAutoStartUploads());
		chkAutoRetryAfterUploadsComplete.setSelected(uploadSettings.isAutoRetryAfterUploadsComplete());

		GUISettings guiSettings = settings.getGuiSettings();

		String lang = guiSettings.getLanguage();
		if (lang.equals("de_DE")) {
			cmbLanguage.setSelectedIndex(0);
		} else {
			cmbLanguage.setSelectedIndex(1);
		}

		chkSaveTableColumnSizes.setSelected(guiSettings.isSaveTableColumnSizes());

		chkSaveTableSortOrders.setSelected(guiSettings.isSaveTableSortOrders());

		cmbSizeView.setSelectedItem(guiSettings.getSizeDisplayMode());

		cmbProgressView.setSelectedItem(guiSettings.getProgressDisplayMode());

		chkUploadRate.setSelected(guiSettings.isUploadRate());

		cmbLAF.setSelectedItem(guiSettings.getLookAndFeel());

		chkUploadsCompleteNotification.setSelected(guiSettings.isUploadsCompleteNotification());

		WindowSettings mainWindowSettings = guiSettings.getMainWindow();
		chkMainWindowSizePos.setSelected(mainWindowSettings.isSave());

		chkBackupDB.setSelected(settings.isBackupDbOnStart());

		cmbLogLevel.setSelectedItem(settings.getLogLevel());
	}

	/**
	 * Apply Settings
	 */
	private void applySettings() {
		settingsManager.removeSettingsListener(settingsListener);
		owner.setMessage(Localization.getString("ApplyingSettings"));

		Settings settings = settingsManager.getSettings();

		ConnectionSettings connectionSettings = settings.getConnectionSettings();

		connectionSettings.setMaxConnections(sldConnectionCount.getValue());
		connectionSettings.setMaxConnectionsPerHost(sldConnectionCountPerHost.getValue());

		int connectTimeout = parseTimeoutSetting(txtConnectTimeout, connectionSettings.getConnectTimeout());
		int socketTimeout = parseTimeoutSetting(txtSocketTimeout, connectionSettings.getSocketTimeout());
		int connectionRequestTimeout = parseTimeoutSetting(txtConnectionRequestTimeout, connectionSettings.getConnectionRequestTimeout());
		connectionSettings.setConnectTimeout(connectTimeout);
		connectionSettings.setSocketTimeout(socketTimeout);
		connectionSettings.setConnectionRequestTimeout(connectionRequestTimeout);

		connectionSettings.setUserAgent(txtUserAgent.getText());

		proxyManager.setAuth(cbAuth.isSelected());
		if (rbNoProxy.isSelected()) {
			proxyManager.setMode(ProxyMode.DIRECT_CONNECTION);
		} else if (rbHTTP.isSelected()) {
			proxyManager.setMode(ProxyMode.PROXY);
		}
		proxyManager.setProxyname(txtProxyName.getText());
		try {
			proxyManager.setProxyport(Integer.parseInt(txtProxyPort.getText()));
		} catch (NumberFormatException nfe) {
			txtProxyPort.setText(String.valueOf(proxyManager.getProxyport()));
		}
		proxyManager.setProxyuser(txtProxyUser.getText());
		proxyManager.setProxypassword(String.valueOf(txtProxyPassword.getPassword()));
		proxyManager.writeToSettings();

		UploadSettings uploadSettings = settings.getUploadSettings();

		uploadSettings.setMaxFailedCount(Integer.parseInt(txtMaxFailedCount.getText()));
		uploadSettings.setSaveLogs(chkSaveLogs.isSelected());
		uploadSettings.setAutoStartUploads(chkAutoStartUploads.isSelected());
		uploadSettings.setAutoRetryAfterUploadsComplete(chkAutoRetryAfterUploadsComplete.isSelected());

		GUISettings guiSettings = settings.getGuiSettings();

		if (cmbLanguage.getSelectedIndex() == 0) {
			guiSettings.setLanguage("de_DE");
		} else {
			guiSettings.setLanguage("en_EN");
		}

		guiSettings.setSaveTableColumnSizes(chkSaveTableColumnSizes.isSelected());

		guiSettings.setSaveTableSortOrders(chkSaveTableSortOrders.isSelected());

		guiSettings.setSizeDisplayMode((SizeDisplayMode)cmbSizeView.getSelectedItem());
		guiSettings.setProgressDisplayMode((ProgressDisplayMode)cmbProgressView.getSelectedItem());

		guiSettings.setUploadRate(chkUploadRate.isSelected());

		settingsManager.setLookAndFeel((LookAndFeelSetting)cmbLAF.getSelectedItem());

		guiSettings.setUploadsCompleteNotification(chkUploadsCompleteNotification.isSelected());

		WindowSettings mainWindowSettings = guiSettings.getMainWindow();
		mainWindowSettings.setSave(chkMainWindowSizePos.isSelected());

		settings.setBackupDbOnStart(chkBackupDB.isSelected());

		settingsManager.setLogLevel((LogLevelSetting)cmbLogLevel.getSelectedItem());

		owner.setMessage(Localization.getString("SettingsApplied"));
		settingsManager.addSettingsListener(settingsListener);
	}

	private void proxySettingChanged() {
		boolean b1 = rbNoProxy.isSelected();
		boolean b2 = rbHTTP.isSelected();
		boolean b = false;
		if (b1) {
			b = false;
		} else if (b2) {
			b = true;
		}
		lblProxyName.setEnabled(b);
		lblProxyPort.setEnabled(b);
		txtProxyName.setEnabled(b);
		txtProxyPort.setEnabled(b);
		cbAuth.setEnabled(!(b1));
		if (b1) {
			cbAuth.setSelected(false);
		}
	}

	private int parseTimeoutSetting(JTextField textField, int defaultValue) {
		try {
			int val = Integer.parseInt(textField.getText());
			if (val <= 1000) {
				textField.setText(String.valueOf(defaultValue));
				return defaultValue;
			} else {
				return val;
			}
		} catch (NumberFormatException nfe) {
			logger.error(nfe.getMessage(), nfe);
			textField.setText(String.valueOf(defaultValue));
			return defaultValue;
		}
	}
}
