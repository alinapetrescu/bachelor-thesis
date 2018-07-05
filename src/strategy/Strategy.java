package alina.sim.strategy;

import alina.sim.Simulation;

import javax.swing.*;

/**
 * Interface publique quoi doit être importée dans toutes les stratégies afin d'y redéfinir les méthodes
 * @author alina petrescu
 * @version 1.0
 */
public interface Strategy {
    
	/**
     * Cette méthode permet de remettre les valeurs du début.
     */
	void reset();

	/**
	 * La méthode react reçoit en paramètre l'état actuel du système.
	 * Le but de cette méthode est de calculer et de retourner la force à appliquer 
     * par le "moteur" du chariot pour tenter de garder le pendule en équilibre.
     * 
	 * @param state
	 * @return
	 */
    double react(Simulation.State state);
    
    /**
     * Cette méthode retourne le panneau de la simulation.
     * @return
     */
    JPanel getPanel();
}
