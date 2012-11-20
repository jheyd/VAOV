package de.piratenpartei.id.frontend.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.piratenpartei.id.frontend.Client;

public class MyMenuBar extends JMenuBar implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8938821142026068736L;
	
	
	private GUI_Helper gh;
	private GUI v;
	
	private JMenuItem jmi_About;
	private JMenuItem jmi_Quit;
	private JMenuItem jmi_NewAccount;
	private JMenuItem jmi_ImportAccount;
	private JMenuItem jmi_ExportAccount;
	private JMenuItem jmi_RevokeAccount;
	
	public MyMenuBar(GUI g, GUI_Helper gh){
		this.gh = gh;
		this.v = g;
		
		// MenuBar items
		this.jm_MenuCreate();
		this.jm_HelpCreate();
	}

	private void jm_HelpCreate(){
		JMenu jm_Help = new JMenu("Help");
		this.add(jm_Help);
		
		jmi_About = new JMenuItem("About");
		jmi_About.addActionListener(this);
		jm_Help.add(jmi_About);
	}
	
	private void jm_MenuCreate(){
		JMenu jm_Menu = new JMenu("Menu");
		this.add(jm_Menu);
		
		jmi_Quit = new JMenuItem("Quit");
		jmi_Quit.addActionListener(this);
		jm_Menu.add(jmi_Quit);
		
		
		jmi_NewAccount = new JMenuItem("New Account");
		jmi_NewAccount.addActionListener(this);
		jm_Menu.add(jmi_NewAccount);
		
		
		jmi_ImportAccount = new JMenuItem("Import Account");
		jmi_ImportAccount.addActionListener(this);
		jm_Menu.add(jmi_ImportAccount);
		
		
		jmi_ExportAccount = new JMenuItem("Export Account");
		jmi_ExportAccount.addActionListener(this);
		jm_Menu.add(jmi_ExportAccount);
		
		
		jmi_RevokeAccount = new JMenuItem("Revoke Account");
		jmi_RevokeAccount.addActionListener(this);
		jm_Menu.add(jmi_RevokeAccount);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.jmi_NewAccount){
			String username = "bla";
			String pass = "foo";
			//javax.swing.JDialog d = new JDialog(v);
			//d.setVisible(true);
			gh.setAccount(Client.newAccount(username, pass.toCharArray()));

		}
		if(e.getSource() == this.jmi_ImportAccount){
			//TODO
			
		}
		if(e.getSource() == this.jmi_ExportAccount){
			//TODO
			
		}
		if(e.getSource() == this.jmi_RevokeAccount){
			//TODO
			
		}
		if(e.getSource() == this.jmi_Quit){
			//TODO
			
		}
		if(e.getSource() == this.jmi_About){
			//TODO
	
		}
	}
}
