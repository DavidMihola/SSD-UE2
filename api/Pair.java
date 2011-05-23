package api;

/**
 * Represents a pair of memory cards. A pair consists of 2 cards and a boolean
 * which is true, if these two cards match each other.
 *
 * @author Alexander Tomsu
 */
public class Pair {
    private Card card1;
    private Card card2;
    private Boolean isMatch;

    /**
     * Initializes a new instance of Pair with the given parameters.
     */
    public Pair(Card card1, Card card2, boolean isMatch) {
        this.card1 = card1;
        this.card2 = card2;
        this.isMatch = isMatch;
    }

    /**
     * @return True, if other is instance of Pair and all attributes equal the 
     * attributes of this object, otherwise: false
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof Pair) {
            Pair otherPair = (Pair) other;
            if(isMatch == otherPair.isMatch) {
                if(card1.equals(otherPair.card1)) {
                    return card2.equals(otherPair.card2);
                } else if(card1.equals(otherPair.card2)) {
                    return card2.equals(otherPair.card1);
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (this.card1 != null ? this.card1.hashCode() : 0);
        hash = hash + (this.card2 != null ? this.card2.hashCode() : 0);
        hash = 67 * hash + (this.isMatch != null ? this.isMatch.hashCode() : 0);
        return hash;
    }

    public Card getCard1() {
        return card1;
    }

    public void setCard1(Card card1) {
        this.card1 = card1;
    }

    public Card getCard2() {
        return card2;
    }

    public void setCard2(Card card2) {
        this.card2 = card2;
    }

    public Boolean isMatch() {
        return isMatch;
    }

    public void setIsMatch(Boolean isMatch) {
        this.isMatch = isMatch;
    }
}
