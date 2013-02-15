package de.piratenpartei.id.frontend.view;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import de.piratenpartei.id.frontend.control.Control;
import de.piratenpartei.id.frontend.control.Util;
import de.piratenpartei.id.frontend.model.Pair;
import de.piratenpartei.id.frontend.model.Vote;

public class View extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2716891312327036374L;
	
	
	private Control control;
	private JPanel pn_Main;
	private JButton btn_VoteNo;	
	private JButton btn_Abstain;
	private JButton btn_VoteYes;
	private JTextPane textPane;
	private JTextPane textPane_1;
	private JButton btn_Change;
	private JList<String> list;
	private MyMenuBar menuBar;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			View window = new View();
			window.setVisible(true);
			window.setLayout();
			window.setSize(500,500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public View() {
		control = new Control();

		setBounds(200, 200, 900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//LayoutManager des JFrames, um die Elemente beim Fenstervergroessern links oben zu halten
		setLayout(new FlowLayout(FlowLayout.LEFT)); 

		// content panel for everything except the MenuBar
		pn_Main = new JPanel();
		getContentPane().add(pn_Main, BorderLayout.CENTER);

		btn_VoteNo = new JButton("Vote No");
		btn_Abstain = new JButton("Abstain");
		btn_VoteYes = new JButton("Vote Yes");
		textPane = new JTextPane();
		textPane_1 = new JTextPane();
		btn_Change = new JButton("Change");
		list = new JList<String>();
		menuBar = new MyMenuBar(control,this);
		setJMenuBar(menuBar);
	}

	private void setLayout() {
		// DONT TOUCH
		GroupLayout gl_panel = new GroupLayout(pn_Main);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(list, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(textPane, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btn_Change, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btn_VoteYes, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btn_Abstain, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btn_VoteNo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
							.addComponent(btn_Change)
							.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
							.addComponent(btn_VoteYes)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btn_Abstain)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btn_VoteNo)))
					.addContainerGap())
		);
		pn_Main.setLayout(gl_panel);
	}
	
	public void setButtonsEnabled(boolean votingEnabled){
		this.btn_Abstain.setEnabled(votingEnabled);
		this.btn_VoteNo.setEnabled(votingEnabled);
		this.btn_VoteYes.setEnabled(votingEnabled);
		this.btn_Change.setEnabled(!votingEnabled);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.btn_Abstain){
			//control.voteWithLoadedAccount(new Vote(new boolean[]{false,false},Util.getIniIDFromJListSelectedValue(this.list.getSelectedValue())));
			this.setButtonsEnabled(false);
		}
		if(e.getSource() == this.btn_VoteYes){
			//control.voteWithLoadedAccount(new Vote(new boolean[]{true,false},Util.getIniIDFromJListSelectedValue(this.list.getSelectedValue())));
			this.setButtonsEnabled(false);
		}
		if(e.getSource() == this.btn_VoteNo){
			//control.voteWithLoadedAccount(new Vote(new boolean[]{false,true},Util.getIniIDFromJListSelectedValue(this.list.getSelectedValue())));
			this.setButtonsEnabled(false);
		}
		if(e.getSource() == this.btn_Change){
			this.setButtonsEnabled(true);
		}
		
	}

	public Pair<String,char[]> queryAccount() {
		// TODO
		String username = "foo";
		char[] pass = "bar".toCharArray();
		return new Pair<String,char[]>(username,pass);
	}
}
