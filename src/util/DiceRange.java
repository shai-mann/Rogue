package util;

public class DiceRange {

    private int numDice, numFaces;

    public DiceRange(String diceNotation) {
        parseDiceNotation(diceNotation); // TODO: add error throwing/logging? for invalid inputs
    }

    private DiceRange(int numDice, int numFaces) {
        this.numDice = numDice;
        this.numFaces = numFaces;
    }

    /**
     * Multiplies the range by the given value, so min -> value * min, max -> value * max
     * @param value the value to scale the range by
     * @return a new DiceRange with the scaled range
     */
    public DiceRange scale(int value) {
        return new DiceRange(numDice * value, numFaces);
    }

    public int getValue() {
        return Helper.getRandom(numDice, numFaces * numDice);
    }

    private void parseDiceNotation(String die) {
        String[] parts = die.split("d");

        numDice = Integer.parseInt(parts[0]);
        numFaces = Integer.parseInt(parts[1]);
    }

    @Override
    public String toString() {
        return numDice + "d" + numFaces;
    }

}
