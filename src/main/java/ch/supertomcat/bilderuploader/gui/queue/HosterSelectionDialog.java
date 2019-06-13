package ch.supertomcat.bilderuploader.gui.queue;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ch.supertomcat.bilderuploader.gui.renderer.HosterComboBoxRenderer;
import ch.supertomcat.bilderuploader.hosterconfig.Hoster;
import ch.supertomcat.supertomcatutils.gui.Localization;

/**
 * Hoster Selection Dialog
 */
public class HosterSelectionDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;

	private JComboBox<Hoster> cmbHoster = new JComboBox<>();

	/**
	 * Panel fuer Buttons
	 */
	private JPanel pnlButtons = new JPanel();

	/**
	 * OK
	 */
	private JButton btnOK = new JButton(Localization.getString("OK"));

	/**
	 * Cancel
	 */
	private JButton btnCancel = new JButton(Localization.getString("Cancel"));

	private boolean canceled = true;

	/**
	 * Constructor
	 * 
	 * @param comp Component
	 * @param hosters Hosters
	 * @param defaultHoster Default Hoster or null
	 */
	public HosterSelectionDialog(Frame comp, List<Hoster> hosters, Hoster defaultHoster) {
		super(comp, true);
		init(hosters, defaultHoster);
	}

	/**
	 * Constructor
	 * 
	 * @param comp Component
	 * @param hosters Hosters
	 * @param defaultHoster Default Hoster or null
	 */
	public HosterSelectionDialog(Dialog comp, List<Hoster> hosters, Hoster defaultHoster) {
		super(comp, true);
		init(hosters, defaultHoster);
	}

	@SuppressWarnings("unchecked")
	private void init(List<Hoster> hosters, Hoster defaultHoster) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(Localization.getString("ChooseHoster"));
		setLayout(new BorderLayout());

		for (Hoster hoster : hosters) {
			cmbHoster.addItem(hoster);
		}

		if (defaultHoster != null) {
			cmbHoster.setSelectedItem(defaultHoster);
		}

		cmbHoster.setRenderer(new HosterComboBoxRenderer());

		add(cmbHoster, BorderLayout.CENTER);
		add(pnlButtons, BorderLayout.SOUTH);
		pnlButtons.add(btnOK);
		pnlButtons.add(btnCancel);
		btnOK.addActionListener(this);
		btnCancel.addActionListener(this);

		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOK) {
			canceled = false;
			dispose();
		} else if (e.getSource() == btnCancel) {
			canceled = true;
			dispose();
		}
	}

	/**
	 * Get-Method
	 * 
	 * @return Encoding
	 */
	public Hoster getChosenHoster() {
		if (canceled) {
			return null;
		}
		return (Hoster)cmbHoster.getSelectedItem();
	}
}
