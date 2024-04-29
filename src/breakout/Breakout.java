package breakout;

import javax.swing.JFrame;

import utils.Commons;
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
		FeedforwardNeuralNetwork nn = FeedforwardNeuralNetwork.FNNfromFile(Commons.BREAKOUT_STATE_SIZE, 5,
				Commons.BREAKOUT_NUM_ACTIONS, "scores/breakout/397063.txt");
		Breakout nnGame = new Breakout(nn, GeneticAlgorithm.SEED);
	}

}
