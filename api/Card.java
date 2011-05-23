package api;

import java.util.Set;

/**
 * Represents a memory card. A card consists of a card key, a current state,
 * an id, a column and a row and an uncover count.
 * 
 * @author Alexander Tomsu
 */
public class Card {
    /**
     * Searches for the card with the given cardKey in a set of card objects.
     *
     * @return Card object if found, otherwise: null.
     */
    public static Card findCard(Set<Card> cards, String cardKey) {
        Card cardHelp = new Card(cardKey);
        for(Card card: cards) {
            if(card.equals(cardHelp)) {
                return card;
            }
        }

        return null;
    }

    /**
     * Defines the card states as they are defined in memory.xsd.
     */
    public static enum CardState {
        COVERED, UNCOVERED;
    }

    private String key;
    private CardState currentState;
    private String id;
    private int column;
    private int row;
    private int uncoverCount;

    /**
     * Initializes a new instance of Pair with the given parameters.
     */
    public Card(String key, CardState currentState, String id, int column,
            int row, int uncoverCount) {
        this.key = key;
        this.currentState = currentState;
        this.id = id;
        this.column = column;
        this.row = row;
        this.uncoverCount = uncoverCount;
    }

    /**
     * Helper for finding equal cards in a collection.
     */
    private Card(String key) {
        this.key = key;
    }

    /**
     * @return True, if the id of this Card is the same as of the other card and
     * row and/or column differ.
     */
    public boolean matches(Card other) {
        if(id.equals(other.id)) {
            if(row == other.row) {
                return column != other.column;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * @return True, if other is instance of Card and attribute key equals.
     */
    @Override
    public boolean equals(Object other) {
        if(other instanceof Card) {
            Card otherCard = (Card) other;
            return key.equals(otherCard.key);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (id != null ? this.key.hashCode() : 0);
        return hash;
    }

    /**
     * @return id_column_row
     */
    @Override
    public String toString() {
        return id + "_" + column + "_" + row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public CardState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(CardState currentState) {
        this.currentState = currentState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getUncoverCount() {
        return uncoverCount;
    }

    public void setUncoverCount(int uncoverCount) {
        this.uncoverCount = uncoverCount;
    }
}
