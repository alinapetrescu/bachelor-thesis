package alina.sim.ui;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JPanel;
import alina.sim.strategy.Strategy;

/**
 * Cette classe affiche en bas à droite ce que la stratégie sélectionnée nous permet de faire.
 * 
 * @author alina petrescu
 * @version 1.0
 */
@SuppressWarnings("serial")
public class StrategyPanel extends JPanel {
	private final CardLayout layout;
	
	/**
	 * Cette méthode (constructeur public) nous affiche les éléments de la stratégie.
	 * @param strategies
	 */
	public StrategyPanel(Strategy[] strategies) {
		this.layout = new CardLayout();
		this.setLayout(this.layout);
		this.setBackground(Color.YELLOW);
		for (int i = 0; i < strategies.length; i++)
			add(strategies[i].getPanel(), "X" + i);
	}
	
	/**
	 * Cette méthode permet de sélectionner la stratégie.
	 * @param index
	 */
	public void show(int index) {
		layout.show(this, "X" + index);
	}
}
