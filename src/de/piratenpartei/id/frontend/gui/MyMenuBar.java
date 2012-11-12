package de.piratenpartei.id.frontend.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class MyMenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;
	private GUI_Helper gh;
	private Listeners l;
	
	public MyMenuBar(GUI_Helper gh, Listeners l){
		this.gh = gh;
		this.l = l;
		
		// MenuBar items
		this.mnMenuCreate();
		this.mnHelpCreate();
	}

	private void mnHelpCreate(){
		JMenu mnHelp = new JMenu("Help");
		this.add(mnHelp);
		JPopupMenu popup = new JPopupMenu();
		mnHelp.setComponentPopupMenu(popup);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		popup.add(mntmAbout);
		mntmAbout.addMouseListener(l.getMntmQuitMouseListener());
	}
	
	private void mnMenuCreate(){
		JMenu mnMenu = new JMenu("Menu");
		JPopupMenu popup = new JPopupMenu();
		mnMenu.setComponentPopupMenu(popup);
		this.add(mnMenu);
		mnMenu.addMouseListener(l.getMnMenuMouseListener());
		
		
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addMouseListener(l.getMntmQuitMouseListener());
		popup.add(mntmQuit);
		
		
		JMenuItem mntmNewAccount = new JMenuItem("New Account");
		mntmQuit.addMouseListener(l.getMntmQuitMouseListener());
		popup.add(mntmNewAccount);
		
		
		JMenuItem mntmImportAccount = new JMenuItem("Import Account");
		mntmQuit.addMouseListener(l.getMntmQuitMouseListener());
		popup.add(mntmImportAccount);
		
		
		JMenuItem mntmExportAccount = new JMenuItem("Export Account");
		mntmQuit.addMouseListener(l.getMntmQuitMouseListener());
		popup.add(mntmExportAccount);
		
		
		JMenuItem mntmRevokeAccount = new JMenuItem("Revoke Account");
		mntmQuit.addMouseListener(l.getMntmQuitMouseListener());
		popup.add(mntmRevokeAccount);
	}
}
