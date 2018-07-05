package alina.sim;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Cette classe permet de stocker dans des fichiers texte les résultats du calcul numérique
 * obtenus durant la simulation, à savoir: la position, la vitesse et l'accélération du chariot,
 * ainsi que l'angle theta pour chaque moment courant. 
 * 
 * @author alina petrescu
 * @version 1.0
 */
public class OutputFiles {

	/**
	 * Le nombre de fichiers texte à créer.
	 */
	private final int SIZE = 4;
	/**
	 * La liste d'objets PrintWriter à connecter aux fichiers texte.
	 */
	private List<PrintWriter> pwList = new ArrayList<>(SIZE);
	/**
	 * Le tableau avec les noms des fichiers texte.
	 */
	private String[] fileNames = {"xFile", "vxFile", "axFile", "thetaFile"};

	/**
	 * Constructeur public SANS arguments qui associe à chaque fichier 
	 * texte à créer un objet de type PrintWriter qui permet au 
	 * programme d'y stocker les données voulues.
	 */
	public OutputFiles() {
		for(int i=0; i < SIZE; i++) {
			try {
				pwList.add(i, new PrintWriter(new FileWriter(new File(fileNames[i]+".txt"))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 *  Cette méthode écrit dans chaque fichier texte une nouvelle ligne
	 *  contenant la valeur correspondante de l'état courant de la simulation
	 *  et le temps représentant le moment de l'appel de la méthode.
	 * 
	 * @param state l'état courant de la simulation
	 */
	public void update(Simulation.State state) {
		double[] states = {state.x, state.vx, state.ax, state.theta};
		for(int i=0; i < pwList.size(); i++) {
			pwList.get(i).printf("%10.3f     %10.3f%n", state.t, states[i]);
		}
	}
	
	/**
	 * Cette méthode permet de remplacer les fichiers texte ouverts en écriture
	 * par des nouveaux fichiers vides et dans lesquels l'écriture commence à 
	 * nouveau tout au début.
	 */
	public void reset() {
		for(int i=0; i < pwList.size(); i++) {
			pwList.get(i).close();
			try {
				pwList.set(i, new PrintWriter(new FileWriter(new File(fileNames[i]+".txt"))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Cette méthode permet de fermer les canaux de communication entre 
	 * le programme et les fichiers texte ouverts en écriture.
	 */
	public void close() {
		for(int i=0; i < pwList.size(); i++) {
			pwList.get(i).close();
		}
	}
}