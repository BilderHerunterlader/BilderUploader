package ch.supertomcat.bilderuploader.gui.queue;

import javax.swing.table.DefaultTableModel;

import ch.supertomcat.bilderuploader.upload.UploadFile;

/**
 * TableModel for Queue
 */
public class QueueTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public QueueTableModel() {
		this.addColumn("File");
		this.addColumn("Hoster");
		this.addColumn("Size");
		this.addColumn("Added");
		this.addColumn("Progress");
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	/**
	 * Adds a row
	 * 
	 * @param uploadFile UploadFile
	 */
	protected void addRow(UploadFile uploadFile) {
		Object data[] = new Object[5];
		data[0] = uploadFile.getFile();
		data[1] = uploadFile.getHoster();
		data[2] = uploadFile.getSize();
		data[3] = uploadFile.getDateTimeAdded();
		data[4] = uploadFile;
		this.addRow(data);
	}

	@Override
	public void removeRow(int row) {
		/*
		 * This method is overridden to disable the fireTableRowsDeleted-Call, because
		 * this slows down deleting, when a lot of rows have to be deleted.
		 */
		dataVector.removeElementAt(row);
	}
}
