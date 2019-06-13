package ch.supertomcat.bilderuploader.gui.hoster;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import ch.supertomcat.bilderuploader.gui.ApplGUIConstants;
import ch.supertomcat.bilderuploader.hoster.HosterManager;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.supertomcatutils.gui.table.TableUtil;
import ch.supertomcat.supertomcatutils.gui.table.renderer.DefaultBooleanColorRowRenderer;
import ch.supertomcat.supertomcatutils.gui.table.renderer.DefaultStringColorRowRenderer;

/**
 * Panel for Hostclasses
 */
public class HosterPanel extends JPanel {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * TabelModel
	 */
	private HosterTableModel model;

	/**
	 * Table
	 */
	private JTable jtHoster;

	/**
	 * Special renderer for option buttons
	 */
	private HosterOptionsColumnRenderer ocr = new HosterOptionsColumnRenderer();

	/**
	 * Hoster Manager
	 */
	private HosterManager hosterManager;

	/**
	 * Constructor
	 * 
	 * @param hosterManager Hoster Manager
	 * @param settingsManager Settings Manager
	 */
	public HosterPanel(HosterManager hosterManager, SettingsManager settingsManager) {
		this.hosterManager = hosterManager;
		model = new HosterTableModel(settingsManager);
		jtHoster = new JTable(model);

		TableUtil.internationalizeColumns(jtHoster);

		jtHoster.setDefaultRenderer(Object.class, new DefaultStringColorRowRenderer());
		jtHoster.setDefaultRenderer(Boolean.class, new DefaultBooleanColorRowRenderer());
		jtHoster.getColumn("Settings").setCellRenderer(ocr);
		jtHoster.getColumn("Settings").setCellEditor(new OptionsCellEditor(model));

		jtHoster.getTableHeader().setReorderingAllowed(false);
		jtHoster.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtHoster.setGridColor(ApplGUIConstants.TABLE_GRID_COLOR);
		jtHoster.setRowHeight(TableUtil.calculateRowHeight(jtHoster, true, true));

		setLayout(new BorderLayout());

		JScrollPane jsp = new JScrollPane(jtHoster);
		add(jsp, BorderLayout.CENTER);

		loadHoster();
	}

	/**
	 * Load hosters
	 */
	private void loadHoster() {
		for (Hoster hoster : hosterManager.getHosts()) {
			model.addHoster(hoster);
		}
	}
}
