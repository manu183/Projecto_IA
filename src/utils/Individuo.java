package utils;

import java.util.Arrays;
import java.util.Collections;

public class Individuo implements Comparable<Individuo> {
    private FeedforwardNeuralNetwork fnn;
    private double fitness;

    public Individuo(FeedforwardNeuralNetwork fnn, double fitness) {
        this.fnn = fnn;
        this.fitness = fitness;
    }

    public FeedforwardNeuralNetwork getFNN() {
        return fnn;
    }

    public void setFNN(FeedforwardNeuralNetwork nn) {
        this.fnn = nn;
    }


    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public String toString() {
        return "Individuo [fitness=" + fitness + "]"/*+"\n Rede neuronal: \n" + fnn.toString()*/;
    }

    @Override
    public int compareTo(Individuo o) {
        return (int)(this.getFitness() - o.getFitness());
    }

    
    public static void main(String[] args) {
        Individuo i = new Individuo(new FeedforwardNeuralNetwork(7, 5, 2), 4);
        Individuo i2 = new Individuo(new FeedforwardNeuralNetwork(7, 5, 2), 6);
        Individuo i3 = new Individuo(new FeedforwardNeuralNetwork(7, 5, 2), 7);
        Individuo i4 = new Individuo(new FeedforwardNeuralNetwork(7, 5, 2), 1);
        Individuo i5 = new Individuo(new FeedforwardNeuralNetwork(7, 5, 2), 10);
        Individuo[] popIndividuos = new Individuo[6];
        popIndividuos[0] = i;
        popIndividuos[1] = i2;
        popIndividuos[2] = i3;
        popIndividuos[3] = i4;
        popIndividuos[4] = i5;
        popIndividuos[5] = i;
        for (Individuo individuo : popIndividuos) {
            System.out.println(individuo);
        }
        System.out.println("Ordenando");
        java.util.Arrays.sort(popIndividuos);
        for (Individuo individuo : popIndividuos) {
            System.out.println(individuo);
        }
        System.out.println("Embaralhando");
        Collections.shuffle(Arrays.asList(popIndividuos));  // Embaralhar a população

        for (Individuo individuo : popIndividuos) {
            System.out.println(individuo);
        }
    }
}
