/**
 * A node class for Double linked list. 
 * 
 * @author cesar Delgado and CS221-1
 *
 * @param <T>
 */
public class Node<T> {
	private T element;
	private Node<T> next;
	private Node<T> prev;
	

	/**
	 * Constructor of a given element
	 * @param element
	 */
	public Node(T element) {
		this.element = element;
		this.next = null;
	}
	
	/**
	 * This returns reference to element stored in node
	 * @return reference to element stored in node
	 */
	public T getElement() {
		return element;
	}
	
	/**
	 * This sets reference to element stored at node
	 * @param element - the element to set
	 */
	public void setElement(T element) {
		this.element = element;
	}
	
	/**
	 * This provides reference to the next code.
	 * @return reference to the next code
	 */
	public Node<T> getNext() {
		return next;
	}
	
	/**
	 * This provides reference to previous node.
	 * @param next - reference to next node
	 */
	public void setNext(Node<T> next) {
		this.next = next;
	}
	/**
	 * This provides reference to previous code.
	 * @return reference to previous code
	 */
	public Node<T> getPrev() {
		return prev;
	}
	/**
	 * This provides reference to previous node.
	 * @param prev - reference to previous node
	 */
	public void setPrev(Node<T> prev) {
		this.prev = prev;
	}

}
