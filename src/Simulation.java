package alina.sim;

import alina.sim.strategy.SimpleStrategy;
import alina.sim.strategy.Strategy;

/**
 * Cette classe regroupe les informations pertinentes concernant la simulation.
 * En outre, elle implémente les lois de la physique appliquées au système étudié.
 * En fonction de l'état du système à un moment donné, elle permet d'obtenir l'état
 * du système après un certain intervalle de temps.
 * 
 * @author alina petrescu
 * @version 2.0
 */
public class Simulation {
	/**
	 * L'accélération gravitationnelle.
	 */
    public static final double g = 9.81;
    /**
     * L'état de départ de la simulation.
     */
    public State startState;
    /**
     * L'état courant de la simulation.
     */
    public State state;
    /**
     * La stratégie utilisée pour la simulation.
     */
    public Strategy strategy;
    /**
     * La "précision" du calcul numérique (i.e. le nombre de 
     * sous-intervalles temporaires égaux dt pour 1 ms).
     */
    private static int precision = 1000;

    /**
     * Le constructeur SANS arguments de la classe simulation.
     */
    public Simulation() {
        this.startState = new State();
        this.strategy = new SimpleStrategy();
        reset();
    }
    
    /**
     * Le constructeur AVEC arguments de la classe simulation.
     * 
     * @param startState l'état de départ de la simulation
     * @param strategy la stratégie choisie pour la simulation
     * @param precision la précision de la simulation
     */
    public Simulation(State startState, Strategy strategy, int precision) {
		this.startState = new State (startState);
		this.state = new State (startState);
		this.strategy = strategy;
		Simulation.precision = precision;
	}

    /**
     * Cette méthode permet de faire avancer la simulation durant un intervalle de temps (très petit). 
     * Elle utilise les valeurs de l'état actuel du système pour calculer et stocker les nouvelles
     * valeurs de l'état, après un laps de temps très court donné en paramètre.
     * En fait, cette méthode résout numériquement le système d'équations différentielles ordinaires
     * qui gouvernent le mouvement du pendule inversé.
     * 
     * @param dt le laps de temps entre deux calculs successifs de l'état du système (en s)
     */
	public void solveStep(double dt) {
        if (state.failed)
            return;

        state.started = true;
        
        //On avance le temps avec le laps dt
        state.t += dt;

        //En fonction de l'état actuel du système, la méthode react implémentée dans la stratégie
        //choisie pour la simulation, calcule et retourne la "bonne" valeur de la force de stabilisation du pendule.
        double fx = strategy.react(state);

        double sin = Math.sin(state.theta);
        double cos = Math.cos(state.theta);
        double M = state.M, m = state.m, l = state.l, vAng2 = state.vAng*state.vAng;

        //La nouvelle valeur de l'accélération horizontale du chariot est calculée à partir
        //du principe de D'Alembert
        state.ax = (m*g*sin*cos - m*l*vAng2*sin + fx) / (M + m*sin*sin);
        state.vx += state.ax * dt;
        state.x += state.vx * dt;

        //La nouvelle valeur de l'accélération angulaire du pendule est calculée à partir
        //du principe de D'Alembert
        state.aAng = ((M+m)*g*sin - m*l*vAng2*sin*cos + fx*cos) / l / (M + m*sin*sin);
        state.vAng += state.aAng * dt;
        state.theta += state.vAng * dt;

        if (state.theta > Math.PI)
            state.theta -= 2*Math.PI;
        if (state.theta < -Math.PI)
            state.theta += 2*Math.PI;
    }
    
    /**
     * Cette méthode utilise la méthode solveStep pour permettre de faire avancer 
     * la simulation durant un intervalle de temps donné en paramètre.
     * 
     * @param nb_ms l'intervalle de temps (en ms)
     */
    public void solve(long nb_ms) {
    	//le pas de temps "infinitésimal" en secondes
        double dt = 0.001 / precision;
        for (int i = 0; i < nb_ms * precision; i++)
            solveStep(dt);
    }
    
	/**
	 * Cette méthode remet l'état courant de la simulation à sa valeur de départ et
	 * réinitialise la stratégie.
	 */
    public void reset() {
        state = new State(startState);

        if (strategy != null)
            strategy.reset();
        //System.out.println(state.l);
//        System.out.println(state.m);
    }

    /**
     * Cette méthode remet l'état de départ et l'état courant de la simulation à la 
     * valeur donnée comme argument.
     * @param state cet état devient l'état de départ et l'état courant de la simulation
     */
    public void reset(State state) {
        startState = new State(state);
        reset();
    }

    /**
     * Cette méthode retourne l'état courant du pendule.
     * @return l'état courant du pendule
     */
    public State getState() {
        return new State(state);
    }

    /**
     * Cette méthode permet de changer la précision du calcul numérique.
     * @param precision la nouvelle précision du calcul numérique
     */
    public void setPrecision(int precision) {
        Simulation.precision = precision;
    }

    /**
     * Cette méthode permet de savoir si la simulation est en marche ou pas.
     * @return vrai si la simulation est en marche et faux autrement
     */
    public boolean isStarted() {
        return state.started;
    }

    /**
     * Cette méthode retourne la stratégie utilisée par la simulation.
     * @return la stratégie utilisée par la simulation
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Cette méthode permet de changer la stratégie utilisée par la simulation.
     * @param strategy la nouvelle stratégie
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
        //String s = strategy.toString();
        //System.out.println(s.substring(19, s.indexOf('@')));
        reset();
    }

    /**
     * Cette classe interne correspond à l'état du système à un instant donné
     * (et permet de stocker des informations comme la position, l'angle, les vitesses, les accélérations, ...).
     * Elle est publique et statique et peut être accédée à l'extérieur de la classe englobante 
     * grâce à la syntaxe Simulation.State.
     */
    public static class State {
        /**
    	 * Le temps courant (s).
    	 */
        public double t;
        /**
    	 * Marqueur booléen pour savoir si la simulation est démarrée ou pas.
    	 */
        public boolean started;
        /**
    	 * Marqueur booléen pour savoir si la simulation a échoué ou pas.
    	 */
        public boolean failed;
        /**
    	 * La masse du pendule inversé (kg).
    	 */
        public double m;
        /**
    	 * La masse du chariot (kg).
    	 */
        public double M;
        /**
    	 * La longueur de la tige du pendule (m).
    	 */
        public double l;
        /**
    	 * L'angle formé entre la verticale et la tige du pendule (°).
    	 */
        public double theta;
        /**
    	 * La vitesse angulaire (°/s).
    	 */
        public double vAng;
        /**
    	 * L'accélération angulaire (°/s^2).
    	 */
        public double aAng;
        /**
    	 * L'abscisse du (centre de masse) du chariot (m).
    	 */
        public double x;
        /**
    	 * La vitesse horizontale du chariot (m/s).
    	 */
        public double vx;
        /**
    	 * L'accélération horizontale du chariot (m/s^2).
    	 */
        public double ax;
        /**
    	 * L'ordonnée du (centre de masse) du chariot (m).
    	 */
        public double y;
        /**
    	 * La vitesse verticale du chariot (m/s).
    	 */
        public double vy;
        /**
    	 * L'accélération verticale du chariot (m/s^2).
    	 */
        public double ay;
        
        /**
         * Constructeur sans arguments (qui laisse tous les champs initialisés avec les valeurs par défaut implicites).
         */
        public State() {
        }

        /**
         * Constructeur par recopie (qui crée un clône de son argument).
         * @param state l'état qui est clôné
         */
        public State(State state) {
            this.t = state.t;

            this.started = state.started;
            this.failed = state.failed;

            this.M = state.M;
            this.m = state.m;
            this.l = state.l;

            this.theta = state.theta;
            this.vAng = state.vAng;
            this.aAng = state.aAng;

            this.x = state.x;
            this.vx = state.vx;
            this.ax = state.ax;

            this.y = state.y;
            this.vy = state.vy;
            this.ay = state.ay;
        }
    }
}