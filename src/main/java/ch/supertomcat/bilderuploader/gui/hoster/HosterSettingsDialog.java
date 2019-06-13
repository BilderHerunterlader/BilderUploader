package ch.supertomcat.bilderuploader.gui.hoster;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.bilderuploader.hosterconfig.PasswordVariable;
import ch.supertomcat.bilderuploader.settings.SettingsManager;

/**
 * Hoster Settings Dialog
 */
public final class HosterSettingsDialog {
	/**
	 * Constructor
	 */
	private HosterSettingsDialog() {
	}

	/**
	 * Show Hoster Settings Dialog and save settings
	 * 
	 * @param hoster Hoster
	 * @param settingsManager Settings Manager
	 * @return True if settings were saved, false if dialog was cancelled
	 */
	public static boolean showSettingsDialogAndSave(Hoster hoster, SettingsManager settingsManager) {
		Map<String, String> settings = showSettingsDialog(hoster, settingsManager);
		if (settings != null) {
			settingsManager.setHosterSettings(hoster.getName(), settings);
			settingsManager.writeSettings(true);
			return true;
		}
		return false;
	}

	/**
	 * Show Hoster Settings Dialog
	 * 
	 * @param hoster Hoster
	 * @param settingsManager Settings Manager
	 * @return New Hoster Settings Values or null if dialog cancelled
	 */
	public static Map<String, String> showSettingsDialog(Hoster hoster, SettingsManager settingsManager) {
		Map<String, Object> values = settingsManager.getHosterSettingsValues(hoster.getName());
		return showSettingsDialog(hoster, values);
	}

	/**
	 * Show Hoster Settings Dialog
	 * 
	 * @param hoster Hoster
	 * @param hosterSettingsValues Current Hoster Settings Values
	 * @return New Hoster Settings Values or null if dialog cancelled
	 */
	public static Map<String, String> showSettingsDialog(Hoster hoster, Map<String, Object> hosterSettingsValues) {
		List<JComponent> components = new ArrayList<>();
		Map<String, JTextField> textFieldMap = new LinkedHashMap<>();

		List<PasswordVariable> passwordVariables = hoster.getPasswordVariable();
		for (PasswordVariable variable : passwordVariables) {
			JLabel lbl = new JLabel(variable.getVariableName());
			components.add(lbl);
			JTextField txt;
			if (variable.isEncrypted() != null && variable.isEncrypted()) {
				txt = new JPasswordField();
			} else {
				txt = new JTextField();
			}
			Object value = hosterSettingsValues.get(variable.getVariableName());
			if (value != null) {
				txt.setText(String.valueOf(value));
			}
			components.add(txt);
			textFieldMap.put(variable.getVariableName(), txt);
		}

		JComponent[] compArray = components.toArray(new JComponent[0]);
		int result = JOptionPane.showConfirmDialog(null, compArray, "Hoster Settings for " + hoster.getDisplayName(), JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			Map<String, String> settings = new LinkedHashMap<>();
			for (Map.Entry<String, JTextField> entry : textFieldMap.entrySet()) {
				settings.put(entry.getKey(), entry.getValue().getText());
			}
			return settings;
		}
		return null;
	}
}
