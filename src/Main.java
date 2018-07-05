package alina.sim;

import alina.sim.strategy.ManualStrategy;
import alina.sim.strategy.NullStrategy;
import alina.sim.strategy.SimpleStrategy;
import alina.sim.strategy.Strategy;
import alina.sim.ui.MainPanel;

import java.awt.event.WindowAdapter;
import javax.swing.*;

import java.awt.event.WindowEvent;

/*TODO : Changer la couleur de fond si la simulation s'écrase
				Fusionner NullStrategy avec l'interface Strategy
				Parler de toutes les librairies non Oracle dans le rapport
*/

/**
 * Cette classe est la classe principale avec laquelle démarre le projet. 
 * A l'exécution de la méthode main(), le système de fenêtres imbriquées est créé
 * et l'utilisateur peut commencer à interagir avec l'interface graphique.
 * 
 * @author alina petrescu
 * @version 2.0
 */
public class Main {
	
	/**
	 * Cette méthode définit l'état initial de l'interface graphique.
	 */
    public static void startInterface() {
    	//Tableau avec les stratégies disponibles à l'exécution
    	Strategy[] strategies = {new NullStrategy(), new ManualStrategy(), new SimpleStrategy()};
    	//Container de premier niveau (fenêtre principale)
        JFrame frame = new JFrame();
        //Titre de la fenêtre principale
        frame.setTitle("Simulation d'un pendule inversé");
        //Panneau personnalisé qui permet l'agencement des éléments graphiques
        MainPanel panel = new MainPanel(strategies);
        //On place le panneau personnalisé dans la zone active de la fenêtre principale
        frame.setContentPane(panel);
        //On impose que la JVM s'arrête à la fermeture de la fenêtre principale
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent we) {
        		MainPanel.getOutputFiles().close();
        	}
		});
        
        //On demande à l'environnement graphique de choisir une taille adéquate
        //pour la fenêtre principale (par rapport à son contenu)
        frame.pack();
        //On rend la fenêtre principale visible
        frame.setVisible(true);
    }
    
    /**
     * Cette méthode est le point d'entrée du projet.
     * Elle est la première méthode à être exécutée par la JVM.
     * 
     * @param args tableau de strings (qui peut stocker les paramètres donnés sur la ligne de commande)
     */
    public static void main(String[] args) {
    	//Création d'un thread anonyme qui s'exécute de manière synchrone avec 
    	//le thread qui traite les événements et qui assure la mise à jour de la GUI
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                startInterface();
            }
        });
    }
}
