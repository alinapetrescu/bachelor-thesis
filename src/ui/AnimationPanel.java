package alina.sim.ui;

import alina.sim.Simulation;
import javax.swing.*;
import java.awt.*;

/**
 * Cette classe dessine l'état du système, la simulation (chariot, tige, boule...)
 * ainsi que les forces associées.
 *
 * @author alina petrescu
 * @version 2.0
 */
@SuppressWarnings("serial")
public class AnimationPanel extends JPanel {
	/**
	 * L'échelle des longueurs, i.e. le nombre de pixels utilisés pour 
	 * représenter une longueur réelle d'un mètre.
	 */
    public static final int SCALE = 100; // 1 m = SCALE pixels 
    /**
     * La largeur initiale du chariot.
     */
    public static final double CART_WIDTH = 0.4; //en mètres
    /**
     * La hauteur initiale du chariot.
     */
    public static final double CART_HEIGHT = 0.2; // en mètres

    private Simulation.State state = new Simulation.State();

    /**
     * Constructeur public sans arguments qui donne les dimensions par défaut
     * au panneau de l'animation.
     */
    public AnimationPanel() {
        setMinimumSize(new Dimension(512, 256));
    }

    /**
     * Cette méthode affiche l'état du système à chaque prochaine itération.
     * @param state l'état courant de la simulaton
     */
    public void update(Simulation.State state) {
        this.state = state;
        repaint();
    }

    /**
     * Cette méthode met tout d'abord les paramètre de l'animation à l'échelle de la simulation.
     * Elle place ensuite le pendule inverse au milieu du panneau et y associe ses forces avec
     * différentes couleurs.
     * Cette méthode permet aussi de "suivre" le pendule s'il sort du panneau.
     * 
     * @param g le "pinceau" avec lequel on dessine l'animation
     */
    private void paintAnimation(Graphics2D g) {
//    	System.out.println("x=" + state.x);
        int x = (int) (SCALE * state.x);
        int vx = (int) (SCALE * state.vx);
        int ax = (int) (SCALE * state.ax);

        int l = (int) (SCALE * state.l);
        //le rayon du cercle qui représente la masse m
        int r = (int) (SCALE * 0.1 * Math.pow(state.m + 1, 1.0 / 3.0));

        int cartW = (int) (SCALE * CART_WIDTH * (1 + state.M/ 10));
        int cartH = (int) (SCALE * CART_HEIGHT);

        //on place le pinceau g au centre du panneau AnimationPanel
        g.translate(getWidth()/2, getHeight()/2);
        
        g.scale(1, -1);

        int w = getWidth();
        
        //la caméra "se déplace" pour suivre le chariot
        if(x>w/2 && x<=w)
        {
        	x = x - w;
        }else if(x > w)
        {
        	if((x / (w/2)) % 2 == 0)
        		x = x%	(w/2);
        	else 
        		x=x%(w/2) -w/2;
        }else if(x<w/2 && x>=w)
        {
        	x = x + w;
        }else if(x < w)
        {
        	if((x / (w/2)) % 2 == 0)
        		x = x%	(w/2);
        	else 
        		x=x%(w/2) +w/2;
        }
        
        g.translate(x, 0);
         
        //on dessine le chariot
        g.setColor(Color.BLUE);
        g.fillRect(-cartW/2, -cartH, cartW, 2*cartH);
        //g.fillOval(-cartW/2, -cartH, cartW, 2*cartH);
        
        //on représente la norme avec signe de vx comme droite horizontale cyan
        g.translate(0, cartH/2);
        g.setColor(Color.CYAN);
        g.drawLine(0, 0, vx, 0);
        
        //on représente la norme avec signe d'ax comme droite horizontale pink, à la suite de cyan
        g.setColor(Color.PINK);
        g.drawLine(vx, 1, vx + ax, 1);
        
        //on dessine la tige
        g.translate(0, cartH/20);
        g.rotate(state.theta);
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(0, 0, 0, l);
        
        //
        g.setStroke(new BasicStroke(1));
        g.setColor(Color.YELLOW);
        g.drawArc(-l, -l, 2*l, 2*l, -90, (int) (-0.1 * Math.toDegrees(state.vAng)));
        
        //on dessine le pendule (la masse m)
        g.translate(0, l);
        g.setColor(Color.GREEN);
        g.fillOval(-r / 2, -r / 2, r, r);

    }

    @Override
    /**
     * Cette méthode dessine les composantes de l'animation.
     * 
     * @param g le "pinceau" avec lequel on dessine l'animation
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setBackground(Color.BLACK);
        g2.clearRect(0, 0, getWidth(), getHeight());

        paintAnimation(g2);

        g2.dispose();
    }
}
