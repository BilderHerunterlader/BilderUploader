package ch.supertomcat.bilderuploader.gui;

import java.awt.Window;
import java.io.File;

import javax.swing.JButton;

import ch.supertomcat.supertomcatutils.application.ApplicationProperties;
import ch.supertomcat.supertomcatutils.gui.Icons;
import ch.supertomcat.supertomcatutils.gui.Localization;
import ch.supertomcat.supertomcatutils.gui.dialog.about.AboutDialog;

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
		btnWebsite.addActionListener(e -> openURL(ApplicationProperties.getProperty("WebsiteURL")));
	}

	@Override
	protected void fillProgramInformation() {
		super.fillProgramInformation();
		pnlProgram.addProgramContactInformation(ApplicationProperties.getProperty("MailAddress"));
	}

	@Override
	protected void fillApplicationPathsInformation() {
		super.fillApplicationPathsInformation();
		File profilePath = new File(ApplicationProperties.getProperty("ProfilePath"));
		pnlProgram.addProgramFolderInformation("Database Path:", ApplicationProperties.getProperty("DatabasePath"), profilePath);
		pnlProgram.addProgramFolderInformation("Settings Path:", ApplicationProperties.getProperty("SettingsPath"), profilePath);
		pnlProgram.addProgramFolderInformation("UploadLog Path:", ApplicationProperties.getProperty("UploadLogPath"), profilePath);
	}
}
