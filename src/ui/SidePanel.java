package alina.sim.ui;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Cette classe implémente le panneau à droite de la simulation.
 * Elle contient le panneau de contrôle et affiche une barre de défilement verticale lorsque le panneau
 * devient trop grand.
 * 
 * @author alina petrescu
 * @version 2.0
 */
@SuppressWarnings("serial")
public class SidePanel extends JScrollPane {
    private final JPanel view;

    /**
     * Cette méthode (constructeur public) permet d'initialiser le panneau.
     */
    public SidePanel() {
        super(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_NEVER);
        view = new JPanel(new MigLayout("wrap 1, fillx"));

        setViewportView(view);
    }
    
    /**
     * Cette méthode permet de d'ajouter le panneau et de lui ajouter un titre.
     */
    public void addPanel(JPanel panel, String title) {
        panel.setBorder(createBorder(title));
        view.add(panel, "growx");
        view.revalidate();
    }

    /**
     * Cette méthode permet de supprimer le panneau.
     */
    public void removePanel(JPanel panel) {
    	panel.setVisible(false);
        view.remove(panel);
        view.revalidate();
    }
    
    /**
     * Cette méthode permet de créer les bordures.
     */
    private static Border createBorder(String title) {
//        Border border = BorderFactory.createEtchedBorder();
        Border border = BorderFactory.createEtchedBorder(Color.BLUE, Color.RED);
        return BorderFactory.createTitledBorder(border, title);
    }
}
