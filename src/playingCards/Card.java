package playingCards;

/***
 * utility class representing one card that can be 1-4 or special card
 * @author Stefan
 *
 */
public class Card {
	
	private int cardID;
	
	public Card(int cardID) {
		this.cardID = cardID;
	}
	
	public int getID() {
		return this.cardID;
	}

}
