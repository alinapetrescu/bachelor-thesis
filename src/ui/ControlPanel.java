package alina.sim.ui;

import alina.sim.Simulation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Cette classe permet de démarrer ou de mettre en pause la simulation, ainsi que de l'accélérer ou de la ralentir.
 * Elle définit entre autre les paramètres de départ du système (angle de départ, masse du chariot, masse du pendule, etc.).
 *
 * @author alina petrescu
 * @version 2.0
 */
@SuppressWarnings("serial")
public class ControlPanel extends JPanel {
	private final MainPanel mainPanel;
	private final List<JSlider> sliders;
	private final List<JLabel> labels;
	private final List<String> prefixes;
	
	//Les champs ci-dessous correspondent aux indices de la liste des sliders
	private static final int PRECISION = 0, ANGLE = 1, C_MASS = 2, P_MASS = 3, LENGTH = 4, SPEED = 5;

	private JButton resetButton, playButton, stepButton;
	private java.awt.Font police = new java.awt.Font("Arial", java.awt.Font.BOLD, 12);

	/**
	 * Constructeur public avec un argument qui initialise le panneau de contrôle.
	 * 
	 * @param mainPanel le panneau de la fenêtre principale
	 */
	public ControlPanel(MainPanel mainPanel) {
		super(new MigLayout("wrap 1, fillx"));
		this.mainPanel = mainPanel;
		this.sliders = new ArrayList<>(5);
		this.labels = new ArrayList<>(5);
		this.prefixes = new ArrayList<>(5);

		this.initSliders();
		this.initButtons();
	}

	/**
	 * Cette méthode initalise tous les sliders. Elle mets les étiquettes, les valeurs des curseurs,
	 * ainsi que l'espacement entre chaque "tick" des curseurs.
	 * De plus, elle leur ajoutent les écouteurs ainsi que les événements associés à chacun
	 * des sliders.
	 */
	private void initSliders() {
		prefixes.add("Simulation precision [steps/ms]");
		sliders.add(new JSlider(0, 20, 10));
		sliders.get(PRECISION).setMajorTickSpacing(10);
		sliders.get(PRECISION).setMinorTickSpacing(1);

		prefixes.add("Angle [degrees]");
		sliders.add(new JSlider(-90, 90, 22));
		sliders.get(ANGLE).setMajorTickSpacing(30);
		sliders.get(ANGLE).setMinorTickSpacing(10);

		prefixes.add("Cart mass [kg]");
		sliders.add(new JSlider(1, 100, 50));
		sliders.get(C_MASS).setMajorTickSpacing(25);
		sliders.get(C_MASS).setMinorTickSpacing(5);
		
		prefixes.add("Pendulum mass [kg]");
		sliders.add(new JSlider(0, 100, 10));
		sliders.get(P_MASS).setMajorTickSpacing(25);
		sliders.get(P_MASS).setMinorTickSpacing(5);

		prefixes.add("Length [m]");
		sliders.add(new JSlider(0, 20, 5));
		sliders.get(LENGTH).setMajorTickSpacing(5);
		sliders.get(LENGTH).setMinorTickSpacing(1);

		prefixes.add("Speed [%]");
		sliders.add(new JSlider(-3, 2, 0));
		sliders.get(SPEED).setMajorTickSpacing(1);
		sliders.get(SPEED).setMinorTickSpacing(1);

		ChangeListener sliderListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				int i = sliders.indexOf(ce.getSource());
				if (i < 0)
					return;

				updateLabel(i);

				if (i != SPEED)
					mainPanel.reset();
				else
					mainPanel.setSpeed(getValue(SPEED));
			}
		};

		for (int i = 0; i < sliders.size(); i++) {
			JSlider slider = sliders.get(i);
			slider.setForeground(Color.RED);

			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			slider.setAlignmentX(Component.LEFT_ALIGNMENT);
			slider.addChangeListener(sliderListener);

			JLabel label = new JLabel();
			labels.add(label);
			updateLabel(i);
			label.setFont(police);

			Hashtable<Integer, JLabel> table = new Hashtable<>();
			int min = slider.getMinimum();
			int max = slider.getMaximum();
			int step = slider.getMajorTickSpacing();
			
			if (i == C_MASS) {
				String s = formatValue(i, transformValue(i, min));
				table.put(min, new JLabel(s));
				min = 0;
			}
			
			for (int x = min; x <= max; x += step) {
				String s = formatValue(i, transformValue(i, x));
				table.put(x, new JLabel(s));
			}

			slider.setLabelTable(table);
		}

		for (int i = 0; i < sliders.size(); i++) {
			add(labels.get(i), "gaptop 10px");
			add(sliders.get(i), "growx");
		}
	}

	/**
	 * Cette méthode initialise tous les boutons.
	 * Elle leur met les étiquettes et leur ajoute les écouteurs et les événements 
	 * associés à chacun des boutons.
	 */
	private void initButtons() {
		resetButton = new JButton("Reset");
		playButton = new JButton("PLAY");
		stepButton = new JButton("Step");

		add(resetButton, "split 3, gaptop 20px, growx, push");
		add(playButton , "growx, push");
		add(stepButton, "growx, push");

		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				mainPanel.reset();
			}
		});

		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				if (mainPanel.isRunning())
					mainPanel.pause();
				else
					mainPanel.play();
			}
		});

		stepButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				mainPanel.updateGUI_Step();
			}
		});
	}

	/**
	 * Cette méthode permet de mettre à jour la valeur du curseur lorsque celui-ci est bougé.
	 *  
	 * @param slider le curseur "courant" 
	 */
	private void updateLabel(int slider) {
		String label = prefixes.get(slider) + " : " + formatValue(slider, getValue(slider));
		labels.get(slider).setText(label);
	}

	/**
	 * Cette méthode affiche PAUSE (à la place de PLAY) lorsque le bouton PLAY a été appuyé 
	 * pour démarrer la simulation.
	 * De plus, cette méthode "désactive" (met en gris) tous les sliders sauf celui du SPEED.
	 */
	public void play() {
		playButton.setText("PAUSE");

		for (JSlider slider : sliders)
			slider.setEnabled(false);

		sliders.get(SPEED).setEnabled(true);
	}

	/**
	 * Cette méthode affiche PLAY (à la place de PAUSE) lorsque le bouton PAUSE a été appuyé 
	 * pour mettre en pause la simulation.
	 * De plus, cette méthode réactive tous les sliders.
	 */
	public void pause() {
		playButton.setText("PLAY");
		if (!mainPanel.isStarted())
			for (JSlider slider : sliders)
				slider.setEnabled(true);
	}

	/**
	 * Cette méthode retourne la précision.
	 */
	public int getPrecision() {
		return (int) getValue(PRECISION);
	}

	/**
	 * Cette méthode met en place la valeur de la précision.
	 * 
	 * @param precision la précision de la simulation
	 */
	public void setPrecision(int precision) {
		sliders.get(0).setValue(precision / 100);
	}

	/**
	 * Cette méthode transforme la valeur d'un curseur en valeur "réelle" pour la simulation.
	 * 
	 * @param indexSlider l'indice du curseur courant
	 * @param value la valeur du curseur courant
	 * @return
	 */
	private double transformValue(int indexSlider, int value) {
		double val = value;
		switch (indexSlider) {
		case PRECISION:
			val *= 100;
			break;

		case ANGLE:
			val = -val;
			break;

		case C_MASS:
		case P_MASS:
		case LENGTH:
//			System.out.println("valini=" + val);
			val *= 0.1;
//			System.out.println("valfin=" + val);
			break;

		case SPEED:
			val = Math.pow(2, value);
			break;
		}
		return val;
	}

	/**
	 * Cette méthode met un format spécifique à chaque curseur.
	 * 
	 * @param indexSlider l'indice du curseur courant
	 * @param value la valeur du curseur courant
	 * @return le nouveau format de chaque curseur
	 */
	private String formatValue(int indexSlider, double value) {
		String format = "";

		switch (indexSlider) {
		case PRECISION:
		case ANGLE:
			format = "%.0f";
			break;

		case P_MASS:
		case C_MASS:
		case LENGTH:
			format = "%.1f";
			break;

		case SPEED:
			if (value >= 1)
				format = "%.0f";
			else if (value >= 0.5)
				format = "%.1f";
			else if (value >= 0.25)
				format = "%.2f";
			else
				format = "%.3f";
			break;
		}

		return String.format(format, value);
	}

	/**
	 * Retourne la valeur courante transformée du curseur courant.
	 * 
	 * @param indexSlider l'indice du curseur courant
	 * @return 
	 */
	public double getValue(int indexSlider) {
		int value = sliders.get(indexSlider).getValue();
		return transformValue(indexSlider, value);
	}

	/**
	 * Cette méthode permet de retourner les valeurs "normales" des curseurs.
	 * 
	 * @return l'état courant du système
	 */
	public Simulation.State getState() {
		Simulation.State state = new Simulation.State();
		state.theta = Math.toRadians(getValue(ANGLE));
		state.M = getValue(C_MASS);
		state.m = getValue(P_MASS);
		state.l = getValue(LENGTH);
		return state;
	}
}
