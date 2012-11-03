package de.piratenpartei.id.frontend.gui;


import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextPane;
import javax.swing.JList;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private GUI_Helper gh;
	private Listeners l;
	private JPanel panelMain;
	private JButton btnVoteNo;	
	private JButton btnAbstain;
	private JButton btnVoteYes;
	private JTextPane textPane;
	private JTextPane textPane_1;
	private JButton btnChange;
	private JList<String> list;
	private MyMenuBar menuBar;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			GUI window = new GUI();
			window.setVisible(true);
			window.setLayout();
			window.setListeners();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		gh = new GUI_Helper();
		l = new Listeners(gh);

		setBounds(200, 200, 900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// content panel for everything except the MenuBar
		panelMain = new JPanel();
		getContentPane().add(panelMain, BorderLayout.CENTER);
		
		btnVoteNo = new JButton("Vote No");
		btnAbstain = new JButton("Abstain");
		btnVoteYes = new JButton("Vote Yes");
		textPane = new JTextPane();
		textPane_1 = new JTextPane();
		btnChange = new JButton("Change");
		list = new JList<String>();
		menuBar = new MyMenuBar(gh,l);
		setJMenuBar(menuBar);
		
		pack();
	}

	private void setListeners() {
		btnVoteNo.addMouseListener(l.getBtnVoteNoMouseListener());
		btnAbstain.addMouseListener(l.getBtnAbstainMouseListener());
		btnVoteYes.addMouseListener(l.getBtnVoteYesMouseListener());
	}

	private void setLayout() {
		GroupLayout gl_panel = new GroupLayout(panelMain);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(list, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnChange, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnVoteYes, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnAbstain, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnVoteNo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(textPane_1, Alignment.LEADING))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(list, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
						.addComponent(textPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(textPane_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnChange)
							.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
							.addComponent(btnVoteYes)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAbstain)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnVoteNo)))
					.addContainerGap())
		);
		panelMain.setLayout(gl_panel);
	}

}
