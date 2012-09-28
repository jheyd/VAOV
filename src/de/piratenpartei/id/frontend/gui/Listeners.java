package de.piratenpartei.id.frontend.gui;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import de.piratenpartei.id.frontend.YesNoVote;
import de.piratenpartei.id.vote.IllegalFormatException;
import de.piratenpartei.id.vote.KeyException;
import de.piratenpartei.id.vote.VerificationException;

public class Listeners {
	GUI_Helper gh;
	
	public Listeners(GUI_Helper gh) {
		this.gh = gh;
	}

	public MouseListener getBtnVoteNoMouseListener(){
		MouseListener ml = new MouseListener() {
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
		
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gh.vote(new YesNoVote(false,false));
			}
		};
		return ml;
	}
	
	public MouseListener getBtnVoteYesMouseListener(){
		MouseListener ml = new MouseListener() {
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
		
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gh.vote(new YesNoVote(true,false));
			}
		};
		return ml;
	}
	
	public MouseListener getBtnAbstainMouseListener(){
		MouseListener ml = new MouseListener() {
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
		
			@Override
			public void mouseClicked(MouseEvent arg0) {
				gh.vote(new YesNoVote(false,true));
			}
		};
		return ml;
	}
	
	public MouseListener getMntmQuitMouseListener(){
		MouseListener ml = new MouseListener() {
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
		
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO
			}
		};
		return ml;
	}
	
	public MouseListener getMntmAboutMouseListener(){
		MouseListener ml = new MouseListener() {
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
		
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO
			}
		};
		return ml;
	}

	public MouseListener getMnMenuMouseListener() {
		MouseListener ml = new MouseListener() {
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
		
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		};
		return ml;
	}

}