package dice;

public class Dice {
    public int numberOfDices;
    public int numberDicesEdges;
    public int diceBonus;
    public int result;

    public Dice(){

    };
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
}
