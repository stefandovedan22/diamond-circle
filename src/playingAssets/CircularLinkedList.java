package playingAssets;

/***
 * utility class used to set the way at which players play
 * @author Stefan
 *
 */
public class CircularLinkedList {

	private CircularNode head = null;
	private CircularNode tail = null;
	private CircularNode tempHead = null;
	
	/***
	 * adding player to the list
	 * @param value player ID
	 */
	public void addNode(int value) {
		CircularNode node = new CircularNode(value);
		if(head == null) {
			head = node;
			tempHead = node;
		} else {
			tail.nextNode = node;
		}
		tail = node;
		tail.nextNode = head;
	}
	
	public void deleteNode(int value) {
		if(head == null) {
			return;
		}
		CircularNode currentNode = head;
		CircularNode nextNode;
		
		do {
			nextNode = currentNode.nextNode;
			if(nextNode.value == value) {
				if(tail == head) {
					head = null;
					tail = null;
					tempHead = null;
				} else {
					currentNode.nextNode = nextNode.nextNode;
					tempHead = tempHead.nextNode;
					if(head == nextNode) {
						head = head.nextNode;
					}
					if(tail == nextNode) {
						tail = currentNode;
					}
				}
				break;
			}
			currentNode = nextNode;
		} while(currentNode != head);
	}
	
	public synchronized boolean getCurrentPlayer(int value) {
		if(head != null && tempHead.value == value) {
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized void moveTempHead() {
		tempHead = tempHead.nextNode;
	}
}
