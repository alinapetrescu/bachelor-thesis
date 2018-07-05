package alina.sim.strategy;

import alina.sim.Simulation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

//Stratégie d'essai où on essaie de diriger le chariot avec un slider mais pas terrible
public class ManualStrategy implements Strategy {
    private double f = 0; //la force à appliquer sur le chariot
    private final JPanel panel;
    private final JSlider forceSlider;
    private final JLabel forceLabel;

    public ManualStrategy() {
        panel = new JPanel();
        
        forceSlider = new JSlider(-10, 10, 0);
        forceSlider.setForeground(Color.RED);
        forceSlider.setMajorTickSpacing(5);
        forceSlider.setMinorTickSpacing(1);
        forceSlider.setPaintTicks(true);
        forceSlider.setPaintLabels(true);
        forceSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        forceSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				if (ce.getSource() == forceSlider) {
					f = forceSlider.getValue();
					forceLabel.setText("Force : " + f);
				}
			}
		});
        
        forceLabel = new JLabel("Force : " + forceSlider.getValue());
        forceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        initPanel();
    }

    private void initPanel() {
        panel.setLayout(new MigLayout("wrap 1", "fill"));
        panel.add(forceLabel, "growx");
        panel.add(forceSlider, "growx, pushx");
    }

    @Override
    public void reset() {
    }

    @Override
    public double react(Simulation.State state) {
        return f;
    }
    @Override
    public JPanel getPanel() {
        return panel;
    }
}
