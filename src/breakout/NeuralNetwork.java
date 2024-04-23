package breakout;

import utils.GameController;

public class NeuralNetwork implements GameController{

    

    @Override
    public int nextMove(int[] currentState) {
        // TODO Auto-generated method stub
		//return (int) (Math.random()*5); //mesma coisa que o RandomController
        //return 2;//sempre vai para a direita
        return 1;//sempre vai para a esquerda
	
    }
    
}
