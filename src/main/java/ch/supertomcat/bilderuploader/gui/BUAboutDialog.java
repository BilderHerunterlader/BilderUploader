package ch.supertomcat.bilderuploader.gui;

import java.awt.Desktop;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;

import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.Localization;
import ch.supertomcat.supertomcatutils.gui.dialog.AboutDialog;

/**
 * BU About Dialog
 */
public class BUAboutDialog extends AboutDialog {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param owner Owner
	 */
	public BUAboutDialog(Window owner) {
		super(owner, Localization.getString("About"), Icons.getImage("/ch/supertomcat/bilderuploader/gui/icons/BilderUploader-16x16.png"));

		setVisible(true);
	}

	@Override
	protected void configureComponents() {
		super.configureComponents();

		JButton btnWebsite = new JButton(Localization.getString("Website"), Icons.getTangoIcon("apps/internet-web-browser.png", 16));
		pnlButtons.add(btnWebsite);
		btnWebsite.addActionListener(e -> {
			String url = ApplicationProperties.getProperty("WebsiteURL");
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (IOException | URISyntaxException ex) {
					logger.error("Could not open URL: {}", url, ex);
				}
			} else {
				logger.error("Could not open URL, because Desktop is not supported: {}", url);
			}
		});
	}

	@Override
	protected void fillApplicationPathsInformation(StringBuilder sbApplicationInfo) {
		super.fillApplicationPathsInformation(sbApplicationInfo);

		File profilePath = new File(ApplicationProperties.getProperty("ProfilePath"));

		File databasePath = new File(ApplicationProperties.getProperty("DatabasePath"));
		if (!databasePath.equals(profilePath)) {
			sbApplicationInfo.append("Database Path: ");
			sbApplicationInfo.append(databasePath.getAbsolutePath());
			sbApplicationInfo.append("\n");
		}

		File settingsPath = new File(ApplicationProperties.getProperty("SettingsPath"));
		if (!settingsPath.equals(profilePath)) {
			sbApplicationInfo.append("Settings Path: ");
			sbApplicationInfo.append(settingsPath.getAbsolutePath());
			sbApplicationInfo.append("\n");
		}

		File uploadLogPath = new File(ApplicationProperties.getProperty("UploadLogPath"));
		if (!uploadLogPath.equals(profilePath)) {
			sbApplicationInfo.append("UploadLog Path: ");
			sbApplicationInfo.append(uploadLogPath.getAbsolutePath());
			sbApplicationInfo.append("\n");
		}
	}

	@Override
	protected void fillApplicationLicenseInformation(StringBuilder sbApplicationInfo) {
		sbApplicationInfo.append("E-Mail: ");
		sbApplicationInfo.append(ApplicationProperties.getProperty("MailAddress"));
		sbApplicationInfo.append("\n\n");

		super.fillApplicationLicenseInformation(sbApplicationInfo);
	}
}
