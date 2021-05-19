import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Double linked list implementation of IndexedUnsortedList interfaces.
 * 
 * @author CesarDelgado and CS221-1
 *
 * @param <T>
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head;
	private Node<T> tail;
	private int size;
	private int modCount;

	/**
	 * Constructor for IUDoubleLinkedList
	 */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		Node<T> newNode = new Node<T>(element);
		newNode.setNext(head);
		if (head != null) {
			head.setPrev(newNode);
		} else {
			tail = newNode;
		}
		head = newNode;
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		Node<T> newNode = new Node<T>(element);
		newNode.setPrev(tail);
		if (tail != null) {
			tail.setNext(newNode);
		} else {
			head = newNode;
		}
		tail = newNode;
		size++;
		modCount++;
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) { // Long way
		// find target's node
		Node<T> targetNode = head;
		while (targetNode != null && !targetNode.getElement().equals(target)) {
			targetNode = targetNode.getNext();
		}
		// what if it wasn't found?
		if (targetNode == null) {
			throw new NoSuchElementException();
		}
		// create a new node for element
		Node<T> newNode = new Node<T>(element);
		// attach new node and neighbors
		newNode.setNext(targetNode.getNext());
		newNode.setPrev(targetNode);
		targetNode.setNext(newNode);
		// what if target node was tail?
		if (targetNode != tail) {
			newNode.getNext().setPrev(newNode);
		} else {
			tail = newNode;
		}
		// bookkeeping
		size++;
		modCount++;
	}

	// @Override
	// public void addAfter(T element, T target) { // ListIterator way
	// ListIterator<T> lit = listIterator();
	// boolean foundIt = false;
	// while(lit.hasNext() && !foundIt) {
	// T val = lit.next();
	// if(val.equals(target)) {
	// foundIt = true;
	// }
	// }
	// if(!foundIt) {
	// throw new NoSuchElementException();
	// }
	// lit.add(element);
	// }

	// @Override
	// public void add(int index, T element) { // Long way
	// // valid index?
	// if(index < 0 || index > size) {
	// throw new IndexOutOfBoundsException();
	// }
	// Node<T> newNode = new Node<T>(element);
	// // consider special cases before or after looking for location?
	// if(isEmpty()) {
	// head = tail = newNode;
	// }
	// if(index == 0) {
	// addToFront(element);
	//// newNode.setNext(head);
	//// head.setPrev(newNode);
	//// head = newNode;
	// }else if(index == size) {
	// tail.setNext(newNode);
	// newNode.setPrev(tail);
	// tail = newNode;
	// }else {
	// // find a node in the vicinity of index with some kind of loop
	// Node<T> current = head;
	// for (int i = 0; i < index; i++) {
	// current = current.getNext();
	// }
	// // attach new node
	// Node<T> previous = current.getPrev();
	// previous.setNext(newNode);
	// current.setPrev(newNode);
	// newNode.setNext(current);
	// newNode.setPrev(previous);
	//// newNode.setPrev(current.getPrev());
	//// newNode.setNext(current);
	//// current.setPrev(newNode);
	//// newNode.getPrev().setNext(newNode);
	// }
	// // bookkeeping
	// size++;
	// modCount++;
	// }

	@Override
	public void add(int index, T element) { // ListIterator way
		ListIterator<T> lit = listIterator(index);
		lit.add(element);
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = head.getElement();
		head = head.getNext();
		if (head != null) {
			head.setPrev(null);
		} else {
			tail = null;
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T removeLast() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = tail.getElement();
		if (tail.getPrev() != null) {
			tail.getPrev().setNext(null);
		} else {
			head = null;
		}
		tail = tail.getPrev();
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) { // ListIterator way
		ListIterator<T> lit = listIterator();
		while (lit.hasNext()) {
			T currentValue = lit.next();
			if (currentValue.equals(element)) {
				lit.remove();
				return currentValue;
			}
		}
		throw new NoSuchElementException();
	}

	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> node = head;
		for (int i = 0; i < index; i++) {
			node = node.getNext();
		}
		T retVal = node.getElement();
		if (node != head) {
			node.getPrev().setNext(node.getNext());
		} else {
			head = head.getNext();
		}
		if (node != tail) {
			node.getNext().setPrev(node.getPrev());
		} else {
			tail = tail.getPrev();
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> current = head;
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		modCount++;
		current.setElement(element);
	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		ListIterator<T> lit = listIterator(index);
		return lit.next();
	}

	@Override
	public int indexOf(T element) {
		ListIterator<T> lit = listIterator();
		int index = 0;
		boolean foundIt = false;
		while (!foundIt && lit.hasNext()) {
			if (lit.next().equals(element)) {
				foundIt = true;
			} else {
				index++;
			}
		}
		if (!foundIt) {
			index = -1;
		}
		return index;
	}

	@Override
	public T first() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return head.getElement();
	}

	@Override
	public T last() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		return (indexOf(target) >= 0);
	}

	@Override
	public boolean isEmpty() {
		return (size == 0);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[");
		Iterator<T> it = iterator();
		while (it.hasNext()) {
			str.append(it.next().toString());
			str.append(", ");
		}
		if (str.length() > 1) { // or size > 0
			str.delete(str.length() - 2, str.length());
		}
		str.append("]");
		return str.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLIterator(startingIndex);
	}

	/**
	 * ListIterator for IUDoubleLinkedList
	 */
	private class DLLIterator implements ListIterator<T> {
		private Node<T> nextNode;
		private int nextIndex;
		private int iterModCount;
		private Node<T> lastReturned;

		/**
		 * Constructor for list iterator
		 */
		public DLLIterator() {
			this(0);
		}

		/**
		 * Constructor for the list iterator's starting index
		 * 
		 * @param startingIndex
		 */
		public DLLIterator(int startingIndex) {
			if (startingIndex < 0 || startingIndex > size) {
				throw new IndexOutOfBoundsException();
			}
			nextNode = head;
			for (int i = 0; i < startingIndex; i++) {
				nextNode = nextNode.getNext();
			}
			iterModCount = modCount;
			nextIndex = startingIndex;
			lastReturned = null;
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != null);
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			lastReturned = nextNode;
			nextNode = nextNode.getNext();
			nextIndex++;
			return lastReturned.getElement();
		}

		@Override
		public boolean hasPrevious() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != head);
		}

		@Override
		public T previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			if (nextNode == null) {
				nextNode = tail;
			} else {
				nextNode = nextNode.getPrev();
			}
			lastReturned = nextNode;
			nextIndex--;
			return lastReturned.getElement();
		}

		@Override
		public int nextIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return nextIndex;
		}

		@Override
		public int previousIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return nextIndex - 1;
		}

		@Override
		public void remove() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturned == null) {
				throw new IllegalStateException();
			}
			if (lastReturned != head) {
				lastReturned.getPrev().setNext(lastReturned.getNext());
			} else {
				head = lastReturned.getNext();
				if (head != null) {
					head.setPrev(null);
				}
			}
			if (lastReturned != tail) {
				lastReturned.getNext().setPrev(lastReturned.getPrev());
			} else {
				tail = lastReturned.getPrev();
				if (tail != null) {
					tail.setNext(null);
				}
			}
			if (lastReturned == nextNode) { // last move was previous
				nextNode = nextNode.getNext();
			} else { // last move was next
				nextIndex--;
			}
			size--;
			modCount++;
			iterModCount++;
			lastReturned = null;
		}

		@Override
		public void set(T e) {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturned == null) {
				throw new IllegalStateException();
			} else {
				lastReturned.setElement(e);
			}
			modCount++;
			iterModCount++;
		}

		@Override
		public void add(T e) {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			Node<T> newNode = new Node<T>(e);
			newNode.setNext(nextNode);
			if (nextNode != null) {
				newNode.setPrev(nextNode.getPrev());
				nextNode.setPrev(newNode);
			} else {
				if (tail != null) {
					tail.setNext(newNode);
					newNode.setPrev(tail);
				}
				tail = newNode;
			}
			if (newNode.getPrev() != null) {
				newNode.getPrev().setNext(newNode);
			} else {
				head = newNode;
			}
			modCount++;
			iterModCount++;
			size++;
			nextIndex++;
			lastReturned = null;
		}
	}
}
