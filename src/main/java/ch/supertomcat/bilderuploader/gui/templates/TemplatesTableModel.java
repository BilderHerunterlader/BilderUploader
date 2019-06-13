package ch.supertomcat.bilderuploader.gui.templates;

import java.io.File;

import javax.swing.table.DefaultTableModel;

import ch.supertomcat.bilderuploader.templates.TemplateType;

/**
 * TableModel for Templates
 */
public class TemplatesTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public TemplatesTableModel() {
		this.addColumn("Name");
		this.addColumn("Type");
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case 0:
			case 1:
				return String.class;
			default:
				return super.getColumnClass(columnIndex);
		}
	}

	/**
	 * Add Template
	 * 
	 * @param template Template File
	 * @param templateType Template Type
	 */
	public void addTemplate(File template, TemplateType templateType) {
		Object[] rowData = new Object[2];
		rowData[0] = template;
		rowData[1] = templateType;
		addRow(rowData);
	}
}
