package dice;

public class Dice {
    private int numberOfDices;
    private int numberDicesEdges;
    private int diceBonus;
    private int result;

    public Dice(int numberOfDices, int numberDicesEdges, int diceBonus){
        this.diceBonus = diceBonus;
        this.numberDicesEdges = numberDicesEdges;
        this.numberOfDices = numberOfDices;
    }

    /**
     *  Roll the dice and return random number between 1 and 20
     */
    public int roll() {
        result = 0;
        for (int i=0; i< numberOfDices; i++)
        {
            result = result + ((int)(Math.random() * numberDicesEdges + 1 )) ;
        }
        return result+ diceBonus;
    }

    public int getDiceBonus() {
        return diceBonus;
    }

    public int getNumberDicesEdges() {
        return numberDicesEdges;
    }

    public int getNumberOfDices() {
        return numberOfDices;
    }

    public int getResult() {
        return result;
    }
}
