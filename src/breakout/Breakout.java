package breakout;

import javax.swing.JFrame;

import pacman.RandomController;
import utils.FeedforwardNeuralNetwork;
import utils.GameController;
import utils.GeneticAlgorithm;

public class Breakout extends JFrame {

	private static final long serialVersionUID = 1L;

	public Breakout(GameController network, int i) {
		add(new BreakoutBoard(network, true, i));
		setTitle("Breakout");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		pack();
		setVisible(true);
		System.out.println();
	}

	public static void main(String[] args) {
		//Breakout game = new Breakout(new RandomController(), 999999999);
		//Breakout game = new Breakout(new RandomController(), 0); //O classe que implementa o GameController é a NeuralNetwork
		FeedforwardNeuralNetwork nn = FeedforwardNeuralNetwork.FNNfromFile(GeneticAlgorithm.INPUT_DIM, GeneticAlgorithm.HIDDEN_DIM, GeneticAlgorithm.OUTPUT_DIM, "scores/best.txt");
		Breakout nnGame = new Breakout(nn, GeneticAlgorithm.SEED);
		//Breakout game = new Breakout(new FeedforwardNeuralNetwork(7, 5, 2), 0);
	}

}
