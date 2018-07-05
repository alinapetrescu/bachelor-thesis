package alina.sim.strategy;

import alina.sim.Simulation;

import javax.swing.*;

//Stratégie qui ne fait pas grand chose, elle retourne toujours une force de 0
public class NullStrategy implements Strategy {
    private final JPanel panel;

    public NullStrategy() {
        panel = new JPanel();
    }

    @Override
    public void reset() {
    }

    @Override
    public double react(Simulation.State state) {
        return 0;
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
