package alina.sim.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

import alina.sim.Simulation;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.util.Range;
import net.miginfocom.swing.MigLayout;

/**
 * Cette classe dessine le graphique et les courbes correspondantes à la simulation.
 * 
 * @author alina petrescu
 * @version 2.0
 */
@SuppressWarnings("serial")
public class PlotPanel extends JPanel {
    private ITrace2D xTrace, vxTrace, axTrace, angTrace;
    private GraphicPanel tabGP[] = new GraphicPanel[4];

    /**
     * Constructeur public qui initialise le panneau de dessin.
     */
    public PlotPanel() { 
        initInterface();
        setLayout(new GridLayout(2, 2));    
        for (int i = 0; i < tabGP.length; i++) {
			add(tabGP[i]);
		}
    }

    /**
     * Cette méthode permet de mettre les couleurs, les unités et les bonnes légendes des axes.
     */
    private void initInterface() {
    	//on crée une nouvelle "trace" avec la taille du buffer de max 200 points
    	//qui seront affichés et le nom affiché x
        xTrace = new Trace2DLtd(200, "x");
        xTrace.setColor(Color.BLUE);
        xTrace.setPhysicalUnits("s", "m");
        tabGP[0] = new GraphicPanel(xTrace, -5, 5);

        vxTrace = new Trace2DLtd(200, "vx");
        vxTrace.setColor(Color.GREEN);
        vxTrace.setPhysicalUnits("s", "m/s");
        tabGP[1] = new GraphicPanel(vxTrace, -10, 10);

        axTrace = new Trace2DLtd(200, "ax");
        axTrace.setColor(Color.BLACK);
        axTrace.setPhysicalUnits("s", "m/s²");
        tabGP[2] = new GraphicPanel(axTrace, -10, 10);

        angTrace = new Trace2DLtd(200, "ang");
        angTrace.setColor(Color.RED);
        angTrace.setPhysicalUnits("s", "°");
        tabGP[3] = new GraphicPanel(angTrace, -200, 200);
    }

    /**
     * Cette méthode permet de réinitialiser le panneau du graphique.
     */
    public void reset() {
        for (int i = 0; i < tabGP.length; i++) {
			tabGP[i].trace.removeAllPoints();
		}
        repaint();
    }

    /**
     * Cette méthode permet d'ajouter les points sur le graphique.
     * @param state l'état courant de la simulation
     */
    public void update(Simulation.State state) {
        double time = state.t;
        
        tabGP[0].update(time, state.x);
        tabGP[1].update(time, state.vx);
        tabGP[2].update(time, state.ax/1000);
        tabGP[3].update(time, Math.toDegrees(state.theta));
    }
    
//****************************************************************
    /**
     * Cette classe interne permet d'afficher les quatre panneaux intermédiaires 
     * correspondants chacun à une courbe mathématique différente de la simulation.
     * 
     * @author alina petrescu
     * @version 1.0
     */
    class GraphicPanel extends JPanel {
    	ITrace2D trace;

    	/**
    	 * Constructeur public permettant de mettre une valeur minimale et maximale
    	 * à chaque graphique du panneau PlotPanel.
    	 * 
    	 * @param trace la courbe qui sera dessinée sur chaque graphique
    	 * @param minRange la valeur minimum du graphique
    	 * @param maxRange la valeur maximum du graphique
    	 */
    	public GraphicPanel(ITrace2D trace, double minRange, double maxRange) {
    		this.trace = trace;
    		initInterface(minRange, maxRange);
    	}

    	/**
    	 * Cette méthode permet 
    	 */
    	private void initInterface(double minRange, double maxRange) {
    		Chart2D chart = new Chart2D();
    		chart.addTrace(trace);

    		chart.getAxisX().setPaintGrid(true);
    		chart.getAxisY().setPaintGrid(true);
    		chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(new Range(minRange, maxRange)));

    		chart.setGridColor(Color.LIGHT_GRAY);
    		chart.setUseAntialiasing(true);
    		chart.setMinimumSize(new Dimension(200, 100));

    		setLayout(new MigLayout("fill"));
    		add(chart, "grow");
    	}

    	/**
    	 * Cette méthode permet de réinitialiser le panneau du graphique.
    	 */
    	public void reset() {
    		trace.removeAllPoints();
    		repaint();
    	}

    	/**
    	 * Cette méthode permet d'ajouter les points sur le graphique.
    	 * 
    	 * @param state
    	 */
    	public void update(double time, double val) {
    		trace.addPoint(time, val);
    	}
    }  
}
