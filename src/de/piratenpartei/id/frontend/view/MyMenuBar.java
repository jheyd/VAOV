package de.piratenpartei.id.frontend.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import de.piratenpartei.id.frontend.control.Control;
import de.piratenpartei.id.frontend.control.Util;
import de.piratenpartei.id.frontend.model.Pair;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.PrivateAccount;

public class MyMenuBar extends JMenuBar implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8938821142026068736L;
	
	
	private Control control;
	private View view;
	
	private JMenuItem jmi_About;
	private JMenuItem jmi_Quit;
	private JMenuItem jmi_NewAccount;
	private JMenuItem jmi_ImportAccount;
	private JMenuItem jmi_ExportAccount;
	private JMenuItem jmi_RevokeAccount;
	
	public MyMenuBar(Control c, View v){
		this.control = c;
		this.view = v;
		
		// MenuBar items
		this.jm_MenuCreate();
		this.jm_HelpCreate();
	}

	private void jm_HelpCreate(){
		JMenu jm_Help = new JMenu("Help");
		this.add(jm_Help);
		
		jmi_About = new JMenuItem("About");
		jmi_About.addActionListener(view);
		jm_Help.add(jmi_About);
	}
	
	private void jm_MenuCreate(){
		JMenu jm_Menu = new JMenu("Menu");
		this.add(jm_Menu);
		
		jmi_Quit = new JMenuItem("Quit");
		jmi_Quit.addActionListener(view);
		jm_Menu.add(jmi_Quit);
		
		
		jmi_NewAccount = new JMenuItem("New Account");
		jmi_NewAccount.addActionListener(view);
		jm_Menu.add(jmi_NewAccount);
		
		
		jmi_ImportAccount = new JMenuItem("Import Account");
		jmi_ImportAccount.addActionListener(view);
		jm_Menu.add(jmi_ImportAccount);
		
		
		jmi_ExportAccount = new JMenuItem("Export Account");
		jmi_ExportAccount.addActionListener(view);
		jm_Menu.add(jmi_ExportAccount);
		
		
		jmi_RevokeAccount = new JMenuItem("Revoke Account");
		jmi_RevokeAccount.addActionListener(view);
		jm_Menu.add(jmi_RevokeAccount);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == this.jmi_NewAccount){
			Pair<String,char[]> pair = view.queryAccount();
			String username = pair.first;
			char[] pass = pair.second;
			//control.newAccount(username, pass); TODO
			Util.overwriteChar(pass);
		}
		if(e.getSource() == this.jmi_ImportAccount){
			Pair<String,char[]> pair = view.queryAccount();
			String username = pair.first;
			char[] pass = pair.second;

			JFileChooser jfc = new JFileChooser();
	    	jfc.setMultiSelectionEnabled(false);
	    	jfc.showOpenDialog(view);
			File f = jfc.getSelectedFile();
			PrivateAccount pa = null;
			try {
				// TODO load pa from f
				pa = new PrivateAccount();
				pa.store(username, pass);
			}
			catch (KeyException e1) { e1.printStackTrace();	}
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
