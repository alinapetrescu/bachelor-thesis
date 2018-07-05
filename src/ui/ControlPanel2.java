package alina.sim.ui;

import alina.sim.Simulation;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Cette classe sert à faire pause/play, ralentir ou accélérer le temps, définir les paramètres de départ du système (angle de départ, masse, ...).
 *
 * @author alina petrescu
 * @version 1.0
 */
public class ControlPanel2 extends JPanel {
    private final MainPanel mainPanel;
    private JSlider precisionSlider, angleSlider, massSlider, lengthSlider, speedSlider;
    private JLabel precisionLabel, angleLabel, massLabel, lengthLabel, speedLabel;
    private JButton resetButton, playButton, stepButton;
    private JPanel slidersPanel;
    private JPanel buttonsPanel;
    private java.awt.Font police = new java.awt.Font("Arial", java.awt.Font.BOLD, 12);
    private ChangeListener sliderListener;
    int vals[] = new int[5];

    /**
     * Cette méthode initialise les composantes.
     * @param mainPanel
     */
    public ControlPanel2(MainPanel mainPanel) {
       this.mainPanel = mainPanel;
        this.initSliders();
        this.initButtons();
        this.initInterface();
    }
    
    /**
     * Cette méthode initalise tous les sliders du SidePanel.
     */
    private void initSliders() {
    	sliderListener = new ChangeListener() {
    		JSlider activSlider;
    		
			@Override
			public void stateChanged(ChangeEvent ce) {
				activSlider = (JSlider)ce.getSource();
				if (activSlider == precisionSlider){
					if (mainPanel.isStarted()){
						precisionSlider.setValue(vals[0]);
					}else{
						precisionLabel.setText("Precision [steps/ms] : " + precisionSlider.getValue());
						vals[0] = precisionSlider.getValue();
					}
					
				}else if (activSlider == angleSlider) {
					if (mainPanel.isStarted()){
						angleSlider.setValue(vals[1]);
					}else{
						angleLabel.setText("Angle [degrees] : " + angleSlider.getValue());
						vals[1] = angleSlider.getValue();
					}
					
				}else if (activSlider == massSlider) {
					if (mainPanel.isStarted()){
						massSlider.setValue(vals[2]);
					}else{
						massLabel.setText("Mass [x 100 g] : " + massSlider.getValue());
						vals[2] = massSlider.getValue();
					}
					
				}else if (activSlider == lengthSlider) {
					if (mainPanel.isStarted()){
						lengthSlider.setValue(vals[3]);
					}else{
						lengthLabel.setText("Length [x 100 mm] : " + lengthSlider.getValue());
						vals[3] = lengthSlider.getValue();
					}
					
				}else if (activSlider == speedSlider) {
					if (mainPanel.isStarted()){
						speedSlider.setValue(vals[4]);
					}else{
						speedLabel.setText("Speed : " + speedSlider.getValue());
						vals[4] = speedSlider.getValue();
					}
				}
			}
		};
    	
		precisionSlider = new JSlider(0, 100, 10);
		vals[0] = precisionSlider.getValue();
        precisionSlider.setForeground(Color.RED);
        precisionSlider.setMajorTickSpacing(50);
        precisionSlider.setMinorTickSpacing(10);
        precisionSlider.setPaintTicks(true);
        precisionSlider.setPaintLabels(true);
        precisionSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        precisionSlider.addChangeListener(sliderListener);
		
        angleSlider = new JSlider(-90, 90, 0);
        vals[1] = angleSlider.getValue();
        angleSlider.setForeground(Color.RED);
        angleSlider.setMajorTickSpacing(30);
        angleSlider.setMinorTickSpacing(10);
        angleSlider.setPaintTicks(true);
        angleSlider.setPaintLabels(true);
        angleSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        angleSlider.addChangeListener(sliderListener);
        
        //massSlider = new JSlider(100, 10000, 100);
        massSlider = new JSlider(0, 100, 10);
        vals[2] = massSlider.getValue();
        massSlider.setForeground(Color.RED);
        massSlider.setMajorTickSpacing(25);
        massSlider.setMinorTickSpacing(5);
        massSlider.setPaintTicks(true);
        massSlider.setPaintLabels(true);
        massSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        massSlider.addChangeListener(sliderListener);
        
        //lengthSlider = new JSlider(100, 2000, 500);
        lengthSlider = new JSlider(0, 20, 5);
        vals[3] = lengthSlider.getValue();
        lengthSlider.setForeground(Color.RED);
        lengthSlider.setMajorTickSpacing(5);
        lengthSlider.setMinorTickSpacing(1);
        lengthSlider.setPaintTicks(true);
        lengthSlider.setPaintLabels(true);
        lengthSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        lengthSlider.addChangeListener(sliderListener);
        
        speedSlider = new JSlider(0, 8, 4);
        vals[4] = speedSlider.getValue();
        speedSlider.setForeground(Color.RED);
        speedSlider.setMajorTickSpacing(2);
        speedSlider.setMinorTickSpacing(1);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);
        speedSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        speedSlider.addChangeListener(sliderListener);
        
        precisionLabel = new JLabel("Precision [steps/ms] : " + precisionSlider.getValue());
        precisionLabel.setFont(police);
        angleLabel = new JLabel("Angle [degrees] : " + angleSlider.getValue());
        angleLabel.setFont(police);
        massLabel = new JLabel("Mass [x 100 g] : " + massSlider.getValue());
        massLabel.setFont(police);
        lengthLabel = new JLabel("Length [x 100 mm] : " + lengthSlider.getValue());
        lengthLabel.setFont(police);
        speedLabel = new JLabel("Speed : " + speedSlider.getValue());
        speedLabel.setFont(police);
        
        slidersPanel = new JPanel();
        BoxLayout layout = new BoxLayout(slidersPanel, BoxLayout.Y_AXIS);
        slidersPanel.setLayout(layout);
        slidersPanel.add(precisionLabel);
        slidersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        slidersPanel.add(precisionSlider);
        slidersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        slidersPanel.add(angleLabel);
        slidersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        slidersPanel.add(angleSlider);
        slidersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        slidersPanel.add(massLabel);
        slidersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        slidersPanel.add(massSlider);
        slidersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        slidersPanel.add(lengthLabel);
        slidersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        slidersPanel.add(lengthSlider);
        slidersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        slidersPanel.add(speedLabel);
        slidersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        slidersPanel.add(speedSlider);
        slidersPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        ChangeListener listener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (!mainPanel.isStarted())
                    mainPanel.reset();
            }
        };

        angleSlider.addChangeListener(listener);
        massSlider.addChangeListener(listener);
        lengthSlider.addChangeListener(listener);
        precisionSlider.addChangeListener(listener);

        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                double speed = Math.pow(2, speedSlider.getValue() - 4);
                mainPanel.setSpeed(speed);
            }
        });
    }


    /**
     * Cette méthode initialise tous les boutons du SidePanel.
     */
    private void initButtons() {
        resetButton = new JButton("Reset");
        playButton = new JButton("PLAY");
        stepButton = new JButton("Step");
        
        buttonsPanel = new JPanel();
        buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
        
        buttonsPanel.add(resetButton);
        buttonsPanel.add(playButton);
        buttonsPanel.add(stepButton);

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
     * Cette méthode met en place le ControlPanel du SidePanel.
     */
    private void initInterface() {
        setLayout(new MigLayout("wrap 1"));
        add(slidersPanel);
        add(buttonsPanel);
    }

    /**
     * Cette méthode affiche PAUSE lorsque la simulation est en cours.
     */
    public void play() {
        playButton.setText("PAUSE");
    }

    /**
     * Cette méthode affiche PLAY lorsque la simulation est arrêtée.
     */
    public void pause() {
        playButton.setText("PLAY");
    }

    /**
     * Cette méthode retourne la précision.
     */
    public int getPrecision() {
        return precisionSlider.getValue();
    }

    /**
     * Cette méthode met en place la valeur de la précision.
     */
    public void setPrecision(int precision) {
        precisionSlider.setValue(precision);
    }

    /**
     * Cette méthode permet de retourner les valeurs "normales" des sliders.
     * @return l'état du système
     */
    public Simulation.State getState() {
        Simulation.State state = new Simulation.State();
        state.theta = Math.toRadians(-angleSlider.getValue());
//        state.m = massSlider.getValue() / 1000.0;	//si le slider indique en grammes
//        state.l = lengthSlider.getValue() / 1000.0; //si le slider indique en mm
        state.m = massSlider.getValue() / 10.0; //si le slider indique en centaines de grammes
        state.l = lengthSlider.getValue() / 10.0; //si le slider indique en centaines de mm
        return state;
    }

    /**
     * Cette méthode permet de mettre des valeurs "normales" aux sliders.
     * @param state qui permet de retourner l'état du système.
     */
    public void setState(Simulation.State state) {
        angleSlider.setValue((int) -Math.toDegrees(state.theta));
//        massSlider.setValue((int) (state.m * 1000)); //si le slider indique en grammes
//        lengthSlider.setValue((int) (state.l * 1000)); //si le slider indique en mm
        massSlider.setValue((int) (state.m * 10)); //si le slider indique en centaines de grammes
        lengthSlider.setValue((int) (state.l * 10)); //si le slider indique en centaines de mm
    }
}
