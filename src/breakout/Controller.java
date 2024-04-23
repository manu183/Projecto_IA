package breakout;

import utils.GameController;

public class Controller implements GameController {

    @Override
    public int nextMove(int[] currentState) {
        // TODO Auto-generated method stub
		//return (int) (Math.random()*5); //mesma coisa que o RandomController
        //return 2;//vai sempre para a direita
        /*for(int i=0; i<currentState.length; i++) {
            System.out.print(currentState[i] + " ");
        }*/
        System.out.println(currentState.length);
        return 1;//vai sempre para a esquerda
    }

    
}
