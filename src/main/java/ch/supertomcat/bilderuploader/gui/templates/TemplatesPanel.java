package ch.supertomcat.bilderuploader.gui.templates;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import ch.supertomcat.bilderuploader.gui.ApplGUIConstants;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.templates.TemplateManager;
import ch.supertomcat.bilderuploader.templates.TemplateType;
import ch.supertomcat.supertomcatutils.gui.table.TableUtil;
import ch.supertomcat.supertomcatutils.gui.table.renderer.DefaultBooleanColorRowRenderer;
import ch.supertomcat.supertomcatutils.gui.table.renderer.DefaultStringColorRowRenderer;
import ch.supertomcat.supertomcatutils.gui.table.renderer.FilenameColorRowRenderer;

/**
 * Panel for templates
 */
public class TemplatesPanel extends JPanel {
	/**
	 * UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * TabelModel
	 */
	private TemplatesTableModel model = new TemplatesTableModel();

	/**
	 * Table
	 */
	private JTable jtTemplates = new JTable(model);

	/**
	 * Template Manager
	 */
	private TemplateManager templateManager;

	/**
	 * Constructor
	 * 
	 * @param templateManager Template Manager
	 * @param settingsManager Settings Manager
	 */
	public TemplatesPanel(TemplateManager templateManager, SettingsManager settingsManager) {
		this.templateManager = templateManager;

		TableUtil.internationalizeColumns(jtTemplates);

		jtTemplates.setDefaultRenderer(Object.class, new DefaultStringColorRowRenderer());
		jtTemplates.setDefaultRenderer(Boolean.class, new DefaultBooleanColorRowRenderer());
		jtTemplates.getColumn("Name").setCellRenderer(new FilenameColorRowRenderer());

		jtTemplates.getTableHeader().setReorderingAllowed(false);
		jtTemplates.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jtTemplates.setGridColor(ApplGUIConstants.TABLE_GRID_COLOR);
		jtTemplates.setRowHeight(TableUtil.calculateRowHeight(jtTemplates, true, true));

		setLayout(new BorderLayout());

		JScrollPane jsp = new JScrollPane(jtTemplates);
		add(jsp, BorderLayout.CENTER);

		loadTemplates();
	}

	/**
	 * Load hosters
	 */
	private void loadTemplates() {
		for (File templateFile : templateManager.getMainTemplateFiles()) {
			model.addTemplate(templateFile, TemplateType.MAIN);
		}

		for (File templateFile : templateManager.getFooterTemplateFiles()) {
			model.addTemplate(templateFile, TemplateType.FOOTER);
		}

		for (File templateFile : templateManager.getIncludeTemplateFiles()) {
			model.addTemplate(templateFile, TemplateType.INCLUDE);
		}
	}
}
