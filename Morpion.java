import javax.swing.*;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
 
public class Morpion extends JFrame implements ActionListener {
 
	// 9 boutons de la grille
	private JButton[] cases = new JButton[9];
 
	// zone du haut : affiche le tour / le resultat
	private JLabel lblInfo = new JLabel("", SwingConstants.CENTER);
 
	// boutons du bas
	private JButton btRejouer = new JButton("Rejouer");
	private JButton btQuitter = new JButton("Quitter");
 
	// nom et prenom du joueur (saisis au demarrage)
	private String nom;
	private String prenom;
 
	// gestion du jeu
	private boolean tourX = true;   // true = c'est au joueur X de jouer
	private boolean partieFinie = false;
 
	public Morpion() {
 
		// saisie du nom et prenom au lancement
		this.nom = JOptionPane.showInputDialog(this, "Entrez votre nom :");
		this.prenom = JOptionPane.showInputDialog(this, "Entrez votre prenom :");
 
		if (this.nom == null) this.nom = "";
		if (this.prenom == null) this.prenom = "";
 
		this.setTitle("Morpion - " + this.prenom + " " + this.nom);
		this.setBounds(200, 200, 400, 480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
 
		// label d'info en haut
		this.lblInfo.setBounds(20, 10, 360, 30);
		this.lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
		this.add(this.lblInfo);
 
		// grille de 9 boutons (3x3)
		JPanel panelGrille = new JPanel();
		panelGrille.setLayout(new GridLayout(3, 3));
		panelGrille.setBounds(20, 50, 360, 330);
		this.add(panelGrille);
 
		for (int i = 0; i < 9; i++) {
			this.cases[i] = new JButton("");
			this.cases[i].setFont(new Font("Arial", Font.BOLD, 40));
			this.cases[i].addActionListener(this);
			panelGrille.add(this.cases[i]);
		}
 
		// boutons rejouer / quitter
		this.btRejouer.setBounds(20, 400, 170, 35);
		this.add(this.btRejouer);
		this.btRejouer.addActionListener(this);
 
		this.btQuitter.setBounds(210, 400, 170, 35);
		this.add(this.btQuitter);
		this.btQuitter.addActionListener(this);
 
		this.nouvellePartie();
 
		this.setVisible(true);
	}
 
	// remet la grille a zero pour une nouvelle partie
	private void nouvellePartie() {
		for (int i = 0; i < 9; i++) {
			this.cases[i].setText("");
			this.cases[i].setEnabled(true);
			this.cases[i].setBackground(null);
		}
		this.tourX = true;
		this.partieFinie = false;
		this.lblInfo.setText(this.prenom + ", a vous de jouer (X)");
	}
 
	// verifie s'il y a un gagnant, retourne "X", "O" ou "" si personne
	private String verifierGagnant() {
		int[][] lignesGagnantes = {
			{0,1,2}, {3,4,5}, {6,7,8}, // lignes
			{0,3,6}, {1,4,7}, {2,5,8}, // colonnes
			{0,4,8}, {2,4,6}           // diagonales
		};
 
		for (int[] ligne : lignesGagnantes) {
			String a = this.cases[ligne[0]].getText();
			String b = this.cases[ligne[1]].getText();
			String c = this.cases[ligne[2]].getText();
 
			if (!a.equals("") && a.equals(b) && b.equals(c)) {
				// on colore les 3 cases gagnantes
				this.cases[ligne[0]].setBackground(Color.green);
				this.cases[ligne[1]].setBackground(Color.green);
				this.cases[ligne[2]].setBackground(Color.green);
				return a;
			}
		}
		return "";
	}
 
	// verifie si la grille est pleine (match nul)
	private boolean grillePleine() {
		for (int i = 0; i < 9; i++) {
			if (this.cases[i].getText().equals("")) {
				return false;
			}
		}
		return true;
	}
 
	@Override
	public void actionPerformed(ActionEvent e) {
 
		// bouton rejouer
		if (e.getSource() == this.btRejouer) {
			this.nouvellePartie();
			return;
		}
 
		// bouton quitter
		if (e.getSource() == this.btQuitter) {
			int retour = JOptionPane.showConfirmDialog(this, "Voulez-vous quitter l'application ?", "Quitter", JOptionPane.YES_NO_OPTION);
			if (retour == 0) {
				this.dispose();
			}
			return;
		}
 
		// si la partie est finie, on ne fait plus rien sur la grille
		if (this.partieFinie) {
			return;
		}
 
		// on cherche sur quelle case le joueur a clique
		for (int i = 0; i < 9; i++) {
			if (e.getSource() == this.cases[i]) {
 
				// la case doit etre vide
				if (!this.cases[i].getText().equals("")) {
					return;
				}
 
				// on place le symbole du joueur
				if (this.tourX) {
					this.cases[i].setText("X");
				} else {
					this.cases[i].setText("O");
				}
 
				// on verifie s'il y a un gagnant
				String gagnant = this.verifierGagnant();
 
				if (!gagnant.equals("")) {
					this.partieFinie = true;
					for (int j = 0; j < 9; j++) {
						this.cases[j].setEnabled(false);
					}
					this.lblInfo.setText(this.prenom + ", le joueur " + gagnant + " a gagne !");
				}
				else if (this.grillePleine()) {
					this.partieFinie = true;
					this.lblInfo.setText("Match nul !");
				}
				else {
					// on change de joueur
					this.tourX = !this.tourX;
					if (this.tourX) {
						this.lblInfo.setText(this.prenom + ", a vous de jouer (X)");
					} else {
						this.lblInfo.setText("Au tour du joueur O");
					}
				}
 
				return;
			}
		}
	}
 
	public static void main(String[] args) {
		new Morpion();
	}
}