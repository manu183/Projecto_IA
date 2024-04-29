package utils;

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
        return "Individuo [fitness=" + fitness + "]";
    }

    @Override
    public int compareTo(Individuo o) {
        return (int) (this.getFitness() - o.getFitness());
    }

}
