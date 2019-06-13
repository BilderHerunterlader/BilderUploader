package ch.supertomcat.bilderuploader.database.sqlite;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.supertomcat.bilderuploader.hoster.HosterManager;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.settings.SettingsManager;
import ch.supertomcat.bilderuploader.upload.FileUploadResult;
import ch.supertomcat.bilderuploader.upload.UploadFile;
import ch.supertomcat.bilderuploader.upload.UploadFileState;
import ch.supertomcat.supertomcatutils.database.sqlite.SQLiteDB;

/**
 * Class for Sqlite Database connections for Queue Database
 */
public class UploadQueueSQLiteDB extends SQLiteDB<UploadFile> {
	private final String selectAllEntriesSQL;

	private final String selectEntrySQL;

	private final String insertEntrySQL;

	private final String updateEntrySQL;

	private final String deleteEntrySQL;

	private final JAXBContext jaxbContext;

	private final Marshaller marshaller;

	private final Unmarshaller unmarshaller;

	/**
	 * Hoster Manager
	 */
	private final HosterManager hosterManager;

	/**
	 * Constructor
	 * 
	 * @param databaseFile Path to the database File
	 * @param settingsManager Settings Manager
	 * @param hosterManager Hoster Manager
	 * @throws JAXBException
	 */
	public UploadQueueSQLiteDB(String databaseFile, SettingsManager settingsManager, HosterManager hosterManager) throws JAXBException {
		super(databaseFile, "bu_uploadqueue", settingsManager.getSettings().isBackupDbOnStart());
		this.hosterManager = hosterManager;

		this.jaxbContext = JAXBContext.newInstance(FileUploadResult.class);
		this.marshaller = this.jaxbContext.createMarshaller();
		this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		this.unmarshaller = this.jaxbContext.createUnmarshaller();

		selectAllEntriesSQL = "SELECT * FROM " + tableName;

		selectEntrySQL = "SELECT * FROM " + tableName + " WHERE UploadID = ?";

		StringBuilder sbInsertEntry = new StringBuilder();
		sbInsertEntry.append("INSERT INTO ");
		sbInsertEntry.append(tableName);
		sbInsertEntry.append(" (");
		sbInsertEntry.append("File, ");
		sbInsertEntry.append("Hoster, ");
		sbInsertEntry.append("Size, ");
		sbInsertEntry.append("Added, ");
		sbInsertEntry.append("MimeType, ");
		sbInsertEntry.append("Status, ");
		sbInsertEntry.append("ErrorMessage, ");
		sbInsertEntry.append("FailedCount, ");
		sbInsertEntry.append("Deactivated, ");
		sbInsertEntry.append("ResultXML");
		sbInsertEntry.append(") VALUES (");
		sbInsertEntry.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
		sbInsertEntry.append(")");
		insertEntrySQL = sbInsertEntry.toString();

		StringBuilder sbUpdateEntry = new StringBuilder();
		sbUpdateEntry.append("UPDATE ");
		sbUpdateEntry.append(tableName);
		sbUpdateEntry.append(" SET ");
		sbUpdateEntry.append("File = ?, ");
		sbUpdateEntry.append("Hoster = ?, ");
		sbUpdateEntry.append("Size = ?, ");
		sbUpdateEntry.append("Added = ?, ");
		sbUpdateEntry.append("MimeType = ?, ");
		sbUpdateEntry.append("Status = ?, ");
		sbUpdateEntry.append("ErrorMessage = ?, ");
		sbUpdateEntry.append("FailedCount = ?, ");
		sbUpdateEntry.append("Deactivated = ?, ");
		sbUpdateEntry.append("ResultXML = ?");
		sbUpdateEntry.append(" WHERE UploadID = ?");
		updateEntrySQL = sbUpdateEntry.toString();

		deleteEntrySQL = "DELETE FROM " + tableName + " WHERE UploadID = ?";

		createDatabaseIfNotExist();
	}

	@Override
	protected synchronized boolean createDatabaseIfNotExist() {
		// Create table if not exist
		StringBuilder sbCreateTable = new StringBuilder();
		sbCreateTable.append("CREATE TABLE IF NOT EXISTS ");
		sbCreateTable.append(tableName);
		sbCreateTable.append(" (");
		sbCreateTable.append("UploadID INTEGER PRIMARY KEY AUTOINCREMENT, ");
		sbCreateTable.append("File TEXT NOT NULL, ");
		sbCreateTable.append("Hoster TEXT NOT NULL, ");
		sbCreateTable.append("Size BIGINT NOT NULL, ");
		sbCreateTable.append("Added BIGINT NOT NULL, ");
		sbCreateTable.append("MimeType TEXT NOT NULL, ");
		sbCreateTable.append("Status TEXT NOT NULL, ");
		sbCreateTable.append("ErrorMessage TEXT NOT NULL, ");
		sbCreateTable.append("FailedCount INTEGER NOT NULL, ");
		sbCreateTable.append("Deactivated BOOLEAN NOT NULL, ");
		sbCreateTable.append("ResultXML BLOB");
		sbCreateTable.append(")");
		String createTableSQL = sbCreateTable.toString();

		try (Connection con = getDatabaseConnection()) {
			con.setAutoCommit(true);
			try (Statement statement = con.createStatement()) {
				statement.executeUpdate(createTableSQL);
			}
			return true;
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("Could not create database: {}", tableName, e);
			return false;
		}
	}

	@Override
	protected UploadFile convertResultSetToObject(ResultSet result) throws SQLException, JAXBException {
		int id = result.getInt("UploadID");
		String filePath = result.getString("File");
		String hosterName = result.getString("Hoster");
		Hoster hoster = hosterManager.getHostByName(hosterName);
		long size = result.getLong("Size");
		long added = result.getLong("Added");
		String mimeType = result.getString("MimeType");
		UploadFileState status = UploadFileState.getByName(result.getString("Status"));
		String errMsg = result.getString("ErrorMessage");
		int failedCount = result.getInt("FailedCount");
		boolean deactivated = result.getBoolean("Deactivated");
		byte[] fileUploadResultBytes = result.getBytes("ResultXML");
		FileUploadResult fileUploadResult = null;
		if (fileUploadResultBytes != null) {
			fileUploadResult = (FileUploadResult)unmarshaller.unmarshal(new ByteArrayInputStream(fileUploadResultBytes));
		}

		return new UploadFile(id, new File(filePath), hoster, size, added, mimeType, status, errMsg, failedCount, deactivated, fileUploadResult);
	}

	@Override
	public synchronized List<UploadFile> getAllEntries() {
		List<UploadFile> files = new ArrayList<>();

		try (Connection con = getDatabaseConnection()) {
			try (PreparedStatement statement = con.prepareStatement(selectAllEntriesSQL)) {
				try (ResultSet rs = statement.executeQuery()) {
					while (rs.next()) {
						files.add(convertResultSetToObject(rs));
					}
					return files;
				}
			}
		} catch (SQLException | ClassNotFoundException | JAXBException e) {
			logger.error("Could not get files from database '{}'", tableName, e);
			JOptionPane.showMessageDialog(null, "Message: " + e.getMessage(), "Database-Error", JOptionPane.ERROR_MESSAGE);
			return new ArrayList<>();
		}
	}

	@Override
	public synchronized UploadFile getEntry(int id) {
		try (Connection con = getDatabaseConnection()) {
			try (PreparedStatement statement = con.prepareStatement(selectEntrySQL)) {
				try (ResultSet rs = statement.executeQuery()) {
					if (!rs.first()) {
						logger.error("Could not find UploadFile in database: {}", id);
						return null;
					}
					return convertResultSetToObject(rs);
				}
			}
		} catch (SQLException | ClassNotFoundException | JAXBException e) {
			logger.error("Could not get UploadFile from database '{}': {}", tableName, id, e);
			return null;
		}
	}

	private byte[] convertFileUploadResultToByteArray(FileUploadResult fileUploadResult) throws JAXBException {
		if (fileUploadResult != null) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			marshaller.marshal(fileUploadResult, out);
			return out.toByteArray();
		}
		return null;
	}

	/**
	 * Insert Entry
	 * 
	 * @param entry File
	 * @param statement Prepared Statement
	 * @throws SQLException
	 * @throws JAXBException
	 */
	private synchronized void insertEntry(UploadFile entry, PreparedStatement statement) throws SQLException, JAXBException {
		statement.setString(1, entry.getFile().getAbsolutePath());
		statement.setString(2, entry.getHoster().getName());
		statement.setLong(3, entry.getSize());
		statement.setLong(4, entry.getDateTimeAdded());
		statement.setString(5, entry.getMimeType());
		statement.setString(6, entry.getStatus().name());
		statement.setString(7, entry.getErrMsg());
		statement.setInt(8, entry.getFailedCount());
		statement.setBoolean(9, entry.isDeactivated());
		byte[] fileUploadResultBytes = convertFileUploadResultToByteArray(entry.getFileUploadResult());
		statement.setBytes(10, fileUploadResultBytes);

		int rowsAffected = statement.executeUpdate();
		if (rowsAffected <= 0) {
			logger.error("Could not insert UploadFile into database '{}'. No rows affected: {}", tableName, entry.getFile().getAbsolutePath());
		} else {
			// Set ID on entry
			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int id = generatedKeys.getInt(1);
					entry.setId(id);
					logger.debug("Set ID for inserted UploadFile: ID={}, ContainerURL={}", id, entry.getFile().getAbsolutePath());
				} else {
					logger.error("Could not get generated ID of UploadFile from database '{}': {}", tableName, entry.getFile().getAbsolutePath());
				}
			}
		}
	}

	@Override
	public synchronized boolean insertEntry(UploadFile entry) {
		try (Connection con = getDatabaseConnection()) {
			con.setAutoCommit(true);
			try (PreparedStatement statement = con.prepareStatement(insertEntrySQL, Statement.RETURN_GENERATED_KEYS)) {
				insertEntry(entry, statement);
			}
			return true;
		} catch (SQLException | ClassNotFoundException | JAXBException e) {
			logger.error("Could not insert UploadFile into database '{}': {}", tableName, entry.getFile().getAbsolutePath(), e);
			return false;
		}
	}

	@Override
	public synchronized boolean insertEntries(List<UploadFile> entries) {
		boolean result = true;
		try (Connection con = getDatabaseConnection()) {
			con.setAutoCommit(true);
			try (PreparedStatement statement = con.prepareStatement(insertEntrySQL, Statement.RETURN_GENERATED_KEYS)) {
				for (UploadFile entry : entries) {
					try {
						insertEntry(entry, statement);
					} catch (SQLException | JAXBException e) {
						logger.error("Could not insert UploadFile into database '{}': {}", tableName, entry.getFile().getAbsolutePath(), e);
						result = false;
					}
				}
			}
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("Could not insert UploadFiles into database '{}'", tableName, e);
			result = false;
		}
		return result;
	}

	@Override
	public synchronized boolean updateEntry(UploadFile entry) {
		if (entry.getId() <= 0) {
			logger.warn("Could not update UploadFile in database, because the UploadFile has no valid ID: {}. Adding it to database instead.", entry.getId());
			return insertEntry(entry);
		}

		try (Connection con = getDatabaseConnection()) {
			con.setAutoCommit(true);
			try (PreparedStatement statement = con.prepareStatement(updateEntrySQL)) {
				statement.setString(1, entry.getFile().getAbsolutePath());
				statement.setString(2, entry.getHoster().getName());
				statement.setLong(3, entry.getSize());
				statement.setLong(4, entry.getDateTimeAdded());
				statement.setString(5, entry.getMimeType());
				statement.setString(6, entry.getStatus().name());
				statement.setString(7, entry.getErrMsg());
				statement.setInt(8, entry.getFailedCount());
				statement.setBoolean(9, entry.isDeactivated());
				byte[] fileUploadResultBytes = convertFileUploadResultToByteArray(entry.getFileUploadResult());
				statement.setBytes(10, fileUploadResultBytes);
				statement.setInt(11, entry.getId());
				statement.executeUpdate();
			}
			return true;
		} catch (SQLException | ClassNotFoundException | JAXBException e) {
			logger.error("Could not update UploadFile in database '{}' with ID {}: {}", tableName, entry.getId(), entry.getFile().getAbsolutePath(), e);
			return false;
		}
	}

	@Override
	public synchronized boolean updateEntries(List<UploadFile> entries) {
		boolean result = true;
		for (UploadFile entry : entries) {
			if (!updateEntry(entry)) {
				result = false;
			}
		}
		return result;
	}

	@Override
	public synchronized boolean deleteEntry(UploadFile entry) {
		if (entry.getId() <= 0) {
			logger.error("Could not delete UploadFile in database, because the UploadFile has no valid ID: {}", entry.getId());
			return false;
		}

		try (Connection con = getDatabaseConnection()) {
			con.setAutoCommit(true);
			try (PreparedStatement statement = con.prepareStatement(deleteEntrySQL)) {
				statement.setInt(1, entry.getId());
				statement.executeUpdate();
			}
			logger.debug("Deleted entry with ID {}: {}", entry.getId(), entry.getFile().getAbsolutePath());
			return true;
		} catch (SQLException | ClassNotFoundException e) {
			logger.error("Could not delete UploadFile into database '{}' with ID {}: {}", tableName, entry.getId(), entry.getFile().getAbsolutePath(), e);
			return false;
		}
	}

	@Override
	public synchronized boolean deleteEntries(List<UploadFile> entries) {
		boolean result = true;
		for (UploadFile entry : entries) {
			if (!deleteEntry(entry)) {
				result = false;
			}
		}
		return result;
	}
}
