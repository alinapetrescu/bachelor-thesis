package alina.sim.strategy;

import alina.sim.Simulation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.Dimension;

/**
 * Cette classe implémente la stratégie par défaut de la simulation.
 * Cette stratégie est basée sur le "PID controller" (proportionnel-intégrateur-dérivateur)
 * qui calcule l'erreur afin de la minimiser.
 * @author alina
 * @version 1.0
 */
public class SimpleStrategy implements Strategy {
    private final JPanel panel;
    private JSlider kpSlider, kdSlider;
    private JLabel kpLabel, kdLabel;
    private Font police = new Font("Arial", Font.BOLD, 12);
    private final int valKpMin = -10, valKpMax = 10, valKpIni = -7;
    private final int valKdMin = -10, valKdMax = 10, valKdIni = -4;

    public SimpleStrategy() {
        panel = new JPanel();
        initPanel();
    }

    private void initPanel() {
        kpSlider = new JSlider(valKpMin, valKpMax, valKpIni);
        kpSlider.setForeground(Color.RED);
        kpSlider.setMajorTickSpacing(10);
        kpSlider.setMinorTickSpacing(2);
        kpSlider.setPaintTicks(true);
        kpSlider.setPaintLabels(true);
        kpSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        kpSlider.addChangeListener(sliderListener);
        
        kdSlider = new JSlider(valKdMin, valKdMax, valKdIni);
        kdSlider.setForeground(Color.RED);
        kdSlider.setMajorTickSpacing(10);
        kdSlider.setMinorTickSpacing(2);
        kdSlider.setPaintTicks(true);
        kdSlider.setPaintLabels(true);
        kdSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        kdSlider.addChangeListener(sliderListener);

        
        kpLabel = new JLabel("Proportional : " + valKpIni);
        kpLabel.setFont(police);
        kdLabel = new JLabel("Derivative : " + valKdIni);
        kdLabel.setFont(police);

        panel.setLayout(new MigLayout("wrap 1"));
        panel.add(kpLabel);
        panel.add(kpSlider);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(kdLabel);
        panel.add(kdSlider);
    }
    
    ChangeListener sliderListener = new ChangeListener() {
		@Override
		public void stateChanged(ChangeEvent ce) {
			if(ce.getSource()==kpSlider){
				kpLabel.setText("Proportional : " + kpSlider.getValue());
			}else if (ce.getSource()==kdSlider) {
				kdLabel.setText("Derivative : " + kdSlider.getValue());
			}		
		}
	};

    @Override
    public void reset() {
    }

    @Override
    public double react(Simulation.State state) {
        double kp = kpSlider.getValue() * 10;
        double kd = kdSlider.getValue();
        double f = kp * state.theta + kd * state.vAng;

        return f;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}