package alina.sim.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import alina.sim.Simulation;
import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyFixedViewport;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.util.Range;
import net.miginfocom.swing.MigLayout;

//Dessine le graphique via la méthode update() qui donne l'état
/**
 * Cette classe dessine le graphique et les courbes correspondantes à la simulation.
 * 
 * @author alina petrescu
 * @version 2.0
 */
@SuppressWarnings("serial")
public class PlotFrameAng extends JFrame {
    private ITrace2D angTrace;

    /**
     * Cette méthode (constructeur public) initialise le panneau de dessin.
     */
    public PlotFrameAng() {
        initInterface();
        setBounds(0, 0, 800, 400);
        setVisible(true);
    }

    /**
     * Cette méthode permet de mettre les couleurs, les unités et les bonnes légandes des axes.
     */
    private void initInterface() {
        angTrace = new Trace2DLtd(200, "ang");
        angTrace.setColor(Color.RED);
        angTrace.setPhysicalUnits("s", "°");

        Chart2D chart = new Chart2D();
        chart.addTrace(angTrace);

        chart.getAxisX().setPaintGrid(true);
        chart.getAxisY().setPaintGrid(true);
//        chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(new Range(-90, +90)));
        chart.getAxisY().setRangePolicy(new RangePolicyFixedViewport(new Range(-200, +200)));

        chart.setGridColor(Color.LIGHT_GRAY);
        chart.setUseAntialiasing(true);
        chart.setMinimumSize(new Dimension(512, 256));

        setLayout(new MigLayout("fill"));
        add(chart, "grow");
    }

    /**
     * Cette méthode permet de réinitialiser le panneau du graphique.
     */
    public void reset() {
        angTrace.removeAllPoints();
        repaint();
    }

    /**
     * Cette méthode permet d'ajouter les points sur le graphique.
     * @param state
     */
    public void update(Simulation.State state) {
        double time = state.t;

        angTrace.addPoint(time, Math.toDegrees(state.theta));
        System.out.println("theta=" + Math.toDegrees(state.theta));
        System.out.println("*********************");
    }
}
