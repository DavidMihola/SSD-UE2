package api;


import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import api.Card.CardState;

/**
 * Represents a memory game. A memory game consist of a current game state,
 * a current player, two current cards, a set of cards and of a set of uncovered
 * card pairs.
 * 
 * @author Alexander Tomsu
 */
public class Game {
    /**
     * Defines the game states as they are defined in memory.xsd.
     */
    public static enum GameState {
        NONE, CARD1, CARD2_MATCH, CARD2_UNMATCH, GAME_OVER;
    }

    /**
     * The current state of the game.
     */
    private GameState currentState;

    /**
     * Name of the player which is at turn currently or null.
     */
    private String currentPlayer;

    /**
     * Card number one which is currently uncovered or null.
     */
    private Card currentCard1;

    /**
     * Card number two which is currently uncovered or null.
     */
    private Card currentCard2;

    /**
     * Set of all cards in the memory table.
     */
    private Set<Card> cards;

    /**
     * Set of all pairs which have got uncovered from the memory players.
     */
    private Set<Pair> uncoveredPairs;

    /**
     * Initializes a new instance of game with the given document.
     * This constructor is not null safe.
     *
     * Document will be read in and the fields currentState, currentPlayer, 
     * currentCard1, currentCard2, cards and uncoveredPairs will be set.
     */
    public Game (Document document) {
        String docUri = document.getDocumentElement().getNamespaceURI();
		
        // Einlesen der card Elemente
        NodeList cardElems = document.getElementsByTagNameNS(docUri, "card");
        Set<Card> cards = new HashSet<Card>();
        for(int i = 0; i < cardElems.getLength(); i++) {
            String currentStateS = cardElems.item(i).getAttributes().
			getNamedItem("current-state").getTextContent();
            CardState currentState = CardState.UNCOVERED;
            if(currentStateS.equals("COVERED") || currentStateS.equals("0")) {
                currentState = CardState.COVERED;
            }
			
            String key = cardElems.item(i).getAttributes().getNamedItem("key").
			getTextContent();
            String id = cardElems.item(i).getAttributes().getNamedItem("id").
			getTextContent();
            int column = Integer.parseInt(cardElems.item(i).getAttributes().
										  getNamedItem("column").getTextContent());
            int row = Integer.parseInt(cardElems.item(i).getAttributes().
									   getNamedItem("row").getTextContent());
            Node uncoverCountNode = cardElems.item(i).
			getAttributes().getNamedItem("uncover-count");
            int uncoverCount = 0;
            if(uncoverCountNode != null) {
                uncoverCount = Integer.parseInt(uncoverCountNode.getTextContent());
            }
            
			
            cards.add(new Card(key, currentState, id, column, row,
							   uncoverCount));
        }
		
        // Einlesen der pair Elemente
        NodeList pairElems = document.getElementsByTagNameNS(docUri, "pair");
        Set<Pair> uncoveredPairs = new HashSet<Pair>();
        for(int i = 0; i < pairElems.getLength(); i++) {
            Card card1 = Card.findCard(cards, pairElems.item(i).getAttributes().
									   getNamedItem("card1").getTextContent());
            Card card2 = Card.findCard(cards, pairElems.item(i).getAttributes().
									   getNamedItem("card2").getTextContent());
            Node isMatchNode = pairElems.item(i).getAttributes().
			getNamedItem("is-match");
            boolean isMatch = false;
            if(isMatchNode != null) {
                isMatch = Boolean.parseBoolean(isMatchNode.getTextContent());
            }
			
            uncoveredPairs.add(new Pair(card1, card2, isMatch));
        }
		
        // Einlesen der Attribute des game Elements
        Element gameElem = document.getDocumentElement();
		
        String currentStateS = gameElem.getAttribute("current-state");
        GameState currentState = GameState.GAME_OVER;
        if(currentStateS.equals("NONE") || currentStateS.equals("0")) {
            currentState = GameState.NONE;
        } else if(currentStateS.equals("CARD1") || currentStateS.equals("1")) {
            currentState = GameState.CARD1;
        } else if(currentStateS.equals("CARD2_MATCH") ||
				  currentStateS.equals("2")) {
            currentState = GameState.CARD2_MATCH;
        } else if(currentStateS.equals("CARD2_UNMATCH") ||
				  currentStateS.equals("3")) {
            currentState = GameState.CARD2_UNMATCH;
        }
		
        String currentPlayer = gameElem.getAttribute("current-player");
        Card currentCard1 = null;
        Card currentCard2 = null;
        if(!gameElem.getAttribute("current-card1").equals("")) {
            currentCard1 = Card.findCard(cards,
										 gameElem.getAttribute("current-card1"));
        }
        if(!gameElem.getAttribute("current-card2").equals("")) {
            currentCard2 = Card.findCard(cards,
										 gameElem.getAttribute("current-card2"));
        }
	
        this.currentState = currentState;
        this.currentPlayer = currentPlayer;
        this.currentCard1 = currentCard1;
        this.currentCard2 = currentCard2;
        this.cards = cards;
        this.uncoveredPairs = uncoveredPairs;
    }

    /**
     * Checks if the game is valid. This method is not null safe.
     *
     * This method check: If the game state is a valid state, if every card has
     * a partner card, if the uncover count of every card is set to the right
     * value and if the current state must be game over.
     *
     * This method doesn't check: If all uncoveredPairs (from the player) are valid
     *
     * @return True if this game is a valid game, otherwise: false.
     */
    public boolean isValid() {
        // check if the current game state is a valid state
        switch(currentState) {
            case NONE:
                if(currentPlayer == null || currentCard1 != null ||
                        currentCard2 != null) {// game attributes invalid
                    return false;
                }
                break;
            case CARD1:
                if(currentPlayer != null && currentCard1 != null &&
                        currentCard2 == null) {// game attributes valid
                    if(currentCard1.getCurrentState() != CardState.UNCOVERED) {
                        return false;
                    }
                } else {//game attributes invalid
                    return false;
                }
                break;
            case CARD2_MATCH:
                if(currentPlayer != null && currentCard1 != null &&
                        currentCard2 != null) {// game attributes valid
                    if(currentCard1.getCurrentState() != CardState.UNCOVERED) {
                        return false;
                    }
                    if(currentCard2.getCurrentState() != CardState.UNCOVERED) {
                        return false;
                    }
                    if(!currentCard1.matches(currentCard2)) {
                        return false;
                    }
                } else {// game attributes invalid
                    return false;
                }
                break;
            case CARD2_UNMATCH:
                if(currentPlayer != null && currentCard1 != null &&
                        currentCard2 != null) {// game attributes valid
                    if(currentCard1.getCurrentState() != CardState.UNCOVERED) {
                        return false;
                    }
                    if(currentCard2.getCurrentState() != CardState.UNCOVERED) {
                        return false;
                    }
                    if(currentCard1.matches(currentCard2)) {
                        return false;
                    }
                } else {// game attributes invalid
                    return false;
                }
                break;
            case GAME_OVER:
                if(currentPlayer != null || currentCard1 != null ||
                        currentCard2 != null) {// game attributes valid
                    return false;
                }
                break;
            default: // should never come here
                return false;
        }

        // check if every card has one partner
        for(Card card1: cards) {
            int counter = 0;
            for(Card card2: cards) {
                if(card1.matches(card2)) {
                    counter ++;
                }
            }
            if(counter != 1) {
                return false;
            }
        }

        // check if the uncoverCount of every game card is set to the right
        // value
        for(Card card: cards) {
            int counter = 0;
            if(card.equals(currentCard1) || card.equals(currentCard2)) {
                counter++;
            }

            for(Pair pair: uncoveredPairs) {
                if(card.equals(pair.getCard1()) ||
                        card.equals(pair.getCard2())) {
                    counter++;
                }
            }

            if(counter != card.getUncoverCount()) {
                return false;
            }
        }

        // check if the current state must be GAME_OVER
        if(!currentState.equals(GameState.GAME_OVER)) {
            int counter = 0;
            for(Pair pair: uncoveredPairs) {
                if(pair.isMatch()) {
                    counter++;
                }
            }
            if(counter * 2 == cards.size()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Goes through all cards and generates a set of all covered pairs in this
     * memory game. This method is not null safe.
     *
     * @return Set of all currently covered pairs in the game.
     */
    public Set<Pair> generateGameCoveredPairs() {
        Set<Pair> coveredGamePairs = new HashSet<Pair>();

        for(Card card1: cards) {
            if(card1.getCurrentState() == CardState.COVERED) {
                for(Card card2: cards) {
                    if(card2.matches(card1)) {
                        coveredGamePairs.add(new Pair(card1, card2, true));
                    }
                }
            }
        }

        return coveredGamePairs;
    }

    /**
     * Goes through all cards and generates a set of all uncovered pairs in this
     * memory game. This method is not null safe.
     *
     * @return Set of all currently uncovered pairs in the game.
     */
    public Set<Pair> generateGameUncoveredPairs() {
        Set<Pair> uncoveredGamePairs = new HashSet<Pair>();

        for(Card card1: cards) {
            if(!card1.equals(currentCard1) && !card1.equals(currentCard2)) {
                if(card1.getCurrentState() == CardState.UNCOVERED) {
                    for(Card card2: cards) {
                        if(card2.matches(card1)) {
                            uncoveredGamePairs.add(
                                    new Pair(card1, card2, true));
                        }
                    }
                }
            }
        }

        return uncoveredGamePairs;
    }

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public Card getCurrentCard1() {
        return currentCard1;
    }

    public void setCurrentCard1(Card currentCard1) {
        this.currentCard1 = currentCard1;
    }

    public Card getCurrentCard2() {
        return currentCard2;
    }

    public void setCurrentCard2(Card currentCard2) {
        this.currentCard2 = currentCard2;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public Set<Pair> getUncoveredPairs() {
        return uncoveredPairs;
    }

    public void setUncoveredPairs(Set<Pair> uncoveredPairs) {
        this.uncoveredPairs = uncoveredPairs;
    }
}
