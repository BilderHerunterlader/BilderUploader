package ch.supertomcat.bilderuploader.gui.hoster;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.settings.BUSettingsListener;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.Localization;

/**
 * TableModel for Hosts
 */
public class HosterTableModel extends DefaultTableModel implements BUSettingsListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Panels
	 */
	private List<JPanel> panels = new ArrayList<>();

	/**
	 * Settings Manager
	 */
	private final SettingsManager settingsManager;

	/**
	 * Constructor
	 * 
	 * @param settingsManager Settings Manager
	 */
	public HosterTableModel(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
		this.settingsManager.addSettingsListener(this);
		this.addColumn("Name");
		this.addColumn("Version");
		this.addColumn("DisplayName");
		this.addColumn("Settings");
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 3 && this.getValueAt(row, 3) instanceof JPanel) {
			return true;
		}
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
			case 1:
			case 2:
				return String.class;
			case 3:
			default:
				return super.getColumnClass(columnIndex);
		}
	}

	/**
	 * Add Hoster
	 * 
	 * @param hoster Hoster
	 */
	public void addHoster(Hoster hoster) {
		Object[] rowData = new Object[4];
		rowData[0] = hoster.getName();
		rowData[1] = hoster.getVersion();
		rowData[2] = hoster.getDisplayName();
		JPanel optionPanel = createOptionPanelForHoster(hoster);
		panels.add(optionPanel);
		rowData[3] = optionPanel != null ? optionPanel : "";
		addRow(rowData);
	}

	/**
	 * @param hoster Hoster
	 * @return Option-Panel for host or null if not available
	 */
	private JPanel createOptionPanelForHoster(Hoster hoster) {
		if (hoster.getPasswordVariable().isEmpty()) {
			return null;
		}

		JButton btn = new JButton(Localization.getString("Settings"), Icons.getTangoIcon("categories/preferences-system.png", 16));
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HosterSettingsDialog.showSettingsDialogAndSave(hoster, settingsManager);
			}
		});
		JPanel pnl = new JPanel();
		pnl.add(btn);
		return pnl;
	}

	/**
	 * Returns the ComboBox
	 * 
	 * @param index Index
	 * @return ComboBox
	 */
	public JPanel getOptionPanel(int index) {
		return panels.get(index);
	}

	@Override
	public void settingsChanged() {
		// Nothing to do
	}

	@Override
	public void lookAndFeelChanged() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (JPanel panel : panels) {
					if (panel != null) {
						SwingUtilities.updateComponentTreeUI(panel);
					}
				}
			}
		});
	}
}
