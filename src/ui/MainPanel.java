package alina.sim.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.Timer;

import alina.sim.OutputFiles;
import alina.sim.Simulation;
//import alina.sim.strategy.ManualStrategy;
//import alina.sim.strategy.ManualStrategy;
//import alina.sim.strategy.SimpleStrategy;
import alina.sim.strategy.Strategy;
import net.miginfocom.swing.MigLayout;

import java.lang.management.*;

/**
 * Cette classe représente le contenu de la fenêtre principale et correspond à un panneau
 * swing qui regroupe les autres containers intermédiaires et objets grapiques atomiques (widgets).
 * 
 * @author alina petrescu
 * @version 2.0
 */
@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	/**
	 * La fréquence de la simulation (en nombre de cadres par seconde).
	 */
    public static final int FRAMES_PER_SECOND = 50;
    /**
	 * Regroupement des informations pertinentes concernant la simulation.
	 */
    private final Simulation simulation;
    /**
	 * Tableau avec les stratégies pour la stabilisation du pendule.
	 */
    private final Strategy[] strategies;
    /**
	 * Container intermédiaire qui regroupe 
	 */
    private final SidePanel sidePanel;
    /**
	 * Container intermédiaire qui affiche l'animation (chariot + pendule).
	 */
    private final AnimationPanel animationPanel;
    /**
	 * Container intermédiaire qui affiche les courbes graphiques.
	 */
    private final PlotPanel plotPanel;
    
    ////private final PlotFrameAng plotFrameAng; //add
    /**
	 * Container intermédiaire qui regroupe 
	 */
    private final ControlPanel controlPanel;
    /**
	 * Combo box qui permet de sélectionner la stratégie voulue.
	 */
    private final JComboBox<String> strategyBox;
    /**
	 * Container intermédiaire qui permet de paramétrer la stratégie choisie.
	 */
    private final StrategyPanel strategyPanel;
    /**
     * Le moteur de l'animation.
     */
    private final Timer timer;
    /**
     * La vitesse de déroulement de la simulation.
     * (speed = 1 correspond au déroulement en temps réel)
     */
    private double speed = 1;
    /**
     * La valeur du temps (le timestamp) correspondant à l'émission du dernier événement
     * actionEvent lancé par la timer pendant le déroulement de la simulation (avec le champ
     * running ayant la valeur true).
     */
    private long timeLastFrame;
    /**
	 * Marqueur booléen qui permet de savoir si la simulation est en marche ou pas.
	 * S'il a la valeur false (suite à un clic sur le bouton PAUSE, les événements ActionEvent  
	 * émis par le timer restent sans effets.
	 */
    private boolean running;
    
    static long tAbs = 0;
    static long start = 0, stop = 0;
    ThreadMXBean mxb = null;
    long tBeforeSolve = 0, tAfterSolve = 0;
    
    private static OutputFiles outputFiles;

    public static OutputFiles getOutputFiles() {
		return outputFiles;
	}

	/**
     * Ce contructeur public initialise les panneaux et permet de choisir la stratégie voulue.
     * 
     * @param strategies tableau de toutes les stratégies disponibles
     */
    public MainPanel(Strategy[] strategies) {
        simulation = new Simulation();
        this.strategies = strategies;

        sidePanel = new SidePanel();
        animationPanel = new AnimationPanel();
        plotPanel = new PlotPanel();
        
        ////plotFrameAng = new PlotFrameAng(); //add
        
        controlPanel = new ControlPanel(this);
        strategyPanel = new StrategyPanel(strategies);
        
        String[] names = new String[strategies.length];
        for (int i = 0; i < strategies.length; i++)
        	names[i] = strategies[i].getClass().getSimpleName();
        strategyBox = new JComboBox<>(names);

        timer = new Timer(1000 / FRAMES_PER_SECOND, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//System.out.println("deltaTAbs = " + (System.nanoTime() - tAbs)*1.E-6);
            	//tAbs = System.nanoTime();
                long time = e.getWhen();
                long tIni = System.nanoTime();
                if (running){
                    updateGUI(time - timeLastFrame);
//                    
//                    System.out.println("tTimer = " + (System.nanoTime() - tIni)*1.E-6);
                }
                
                timeLastFrame = time;
            }
        });
        timer.start();
        
        outputFiles = new OutputFiles();

        initInterface();
        reset();
    }//fin du constructeur

    /**
     * Cette méthode place tous la panneaux d'affichage et initialise toute l'interface graphique.
     * De plus, elle ajoute l'écouteur correpondant à la stratégie sélectionnée.
     */
    private void initInterface() {
        setLayout(new MigLayout("fill, wrap 2"));
        add(animationPanel, "grow, push");
        add(sidePanel, "grow, spany, pushx 22");
        add(plotPanel, "grow, push");
        
        JPanel strategySelectPanel = new JPanel();
        strategySelectPanel.add(strategyBox);
        strategySelectPanel.setBackground(Color.CYAN);
        
        strategyBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = strategyBox.getSelectedIndex();
				setStrategy(i);
			}
		});

        sidePanel.addPanel(controlPanel, "Control");
        sidePanel.addPanel(strategySelectPanel, "Select Strategy");
        sidePanel.addPanel(strategyPanel, "Strategy");
        strategyBox.setSelectedIndex(0);
    }

    /**
     * Cette méthode permet de lancer la simulation.
     */
    public void play() {
        running = true;
        controlPanel.play();
    }

    /**
     * Cette méthode permet de mettre en pause la simulation.
     */
    public void pause() {
        running = false;
        controlPanel.pause();
    }

    /**
     * Cette méthode permet de réinitialiser les paramètres de la simulation.
     */
    public void reset() {
        simulation.setPrecision(controlPanel.getPrecision());
        simulation.reset(controlPanel.getState());

        animationPanel.update(controlPanel.getState());
        plotPanel.reset();
        
        outputFiles.reset();
        
        pause();
    }

    /**
     * Cette méthode permet de mettre à jour l'interface graphique (à savoir, l'animation
     * et les graphiques), après l'avancement de la simulation durant un intervalle de temps 
     * donné en paramètre. 
     * 
     * @param nb_ms nombre de millisecondes d'attente
     */
    public void updateGUI(long nb_ms) {
    	//Après l'appel de la méthode solve, certains champs de l'objet 
    	//appelant simulation changent de valeur
     	mxb = ManagementFactory.getThreadMXBean();   	
    	tBeforeSolve = System.nanoTime();
    	start = mxb.getCurrentThreadCpuTime();
        simulation.solve((long) (nb_ms * speed));
        stop = mxb.getCurrentThreadCpuTime();
        tAfterSolve = System.nanoTime();
//    	System.out.println("durée = " + (stop - start)*1.E-6);
        Simulation.State state = simulation.getState();
        //On met à jour les graphiques
        long tBeforeUpdatePanel = System.nanoTime();
        plotPanel.update(state);
//        plotFrameAng.update(state); //add
        //On met à jour l'animation (chariot + pendule)
        long tAfterUpdatePanel = System.nanoTime();
        animationPanel.update(state);
        long tAfterAnimationPanel = System.nanoTime();

//        System.out.println("tSolve = " + (tAfterSolve - tBeforeSolve)*1.E-6);
//        System.out.println("tTot = " + (tAfterAnimationPanel - tBeforeSolve)*1.E-6);
//        System.out.println("***********");
        
        outputFiles.update(state);
        
        if (state.failed)
            pause();
    }

    /**
     * Cette méthode permet de mettre à jour l'interface graphique après
     * l'avancement de la simulation durant un intervalle de temps (très petit),
     * à savoir le laps (ou le pas) de temps écoulé entre deux cadres successifs.
     */
    public void updateGUI_Step() {
        pause();
        updateGUI(1000 / FRAMES_PER_SECOND);
    }

    /**
     * Cette méthode permet de changer la vitesse de déroulement de la simulation.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Cette méthode permet de choisir la stratégie voulue.
     */
    public void setStrategy(int i) {
        simulation.setStrategy(strategies[i]);
        strategyPanel.show(i);
    }

    /**
     * Cette méthode permet de savoir si la simulation est en marche ou pas.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Cette méthode permet de savoir si la simulation est lancée ou pas.
     */
    public boolean isStarted() {
        return simulation.isStarted();
    }
}
