import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author Cesar Delgado and CS221-1
 *
 * @param <T>
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head;
	private Node<T> tail;
	private int size;
	private int modCount;

	/**
	 * This is the default constructor, and empty list.
	 */
	public IUSingleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		Node<T> newNode = new Node<T>(element);
		newNode.setNext(head);
		head = newNode;
		size++;
		modCount++;
		if (tail == null) {
			tail = newNode;
		}
	}

	@Override
	public void addToRear(T element) {
		Node<T> newNode = new Node<T>(element);
		if (!isEmpty()) {
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
	public void addAfter(T element, T target) {
		Node<T> current = head;
		while (current != null && !current.getElement().equals(target)) {
			current = current.getNext();
		}
		if (current == null) {
			throw new NoSuchElementException();
		}
		Node<T> newNode = new Node<T>(element);
		newNode.setNext(current.getNext());
		current.setNext(newNode);
		if (current == tail) {
			tail = newNode;
		}
		size++;
		modCount++;
	}

	@Override
	public void add(int index, T element) {
		if (index < 0 || index > size()) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> newNode = new Node<T> (element);
		if(index == 0) {
			newNode.setNext(head);
			head= newNode;
		}else {
			Node<T> current = head;
			for (int i = 0;i < index-1; i++) {
				current = current.getNext();
			}
			Node<T> next = current.getNext();
			newNode.setNext(next);
			current.setNext(newNode);
		}
		if(index == size) {
			tail = newNode;
		}
		size++;
		modCount++;
	}

	@Override
	public T removeFirst() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = head.getElement();
		if (size == 1) { // or head == tail
			head = tail = null;
		} else {
			head = head.getNext();
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
		if (size == 1) { // or head == tail
			head = tail = null;
		} else {
			Node<T> previous = head;
			while (previous.getNext() != tail) {
				previous = previous.getNext();
			}
			previous.setNext(null);
			tail = previous;
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(T element) {
		Node<T> current = head;
		Node<T> previous = null;
		while (current != null && !current.getElement().equals(element)) {
			previous = current;
			current = current.getNext();
		}
		if (current == null) {
			throw new NoSuchElementException();
		}
		T retVal = current.getElement();
		if (previous == null) { // current is head
			head = head.getNext();
		} else { // current is not head
			previous.setNext(current.getNext());
		}
		if (current == tail) {
			tail = previous;
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		T retVal;
		if (index == 0) {
			retVal = head.getElement();
			head = head.getNext();
			if (head == null) {
				tail = null;
			}
		} else {
			Node<T> previous = head;
			for (int i = 0; i < index - 1; i++) {
				previous = previous.getNext();
			}
			retVal = previous.getNext().getElement();
			previous.setNext(previous.getNext().getNext());
			if (previous.getNext() == null) {
				tail = previous;
			}
		}
		size--;
		modCount++;
		return retVal;
	}

	@Override
	public void set(int index, T element) {
		// TODO Auto-generated method stub
		{
			if (index < 0 || index >= size) {
				throw new IndexOutOfBoundsException();
			}
			Node<T> current = head;

			for (int i = 0; i < index; i++) {
				current = current.getNext();
			}

			current.setElement(element);
		}
		modCount++;
	}

	@Override
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		Node<T> current = head;

		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}

		return current.getElement();
	}

	@Override
	public int indexOf(T element) {
		Node<T> current = head;
		int index = 0;
		while (current != null && !current.getElement().equals(element)) {
			index++;
			current = current.getNext();
		}
		if (current == null) {
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
		// TODO Auto-generated method stub
		return indexOf(target) == 0;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
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
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Iterator for SLL
	 */
	private class SLLIterator implements Iterator<T> {
		private Node<T> nextNode; // nodeThatWouldBeNext
		private int iterModCount;
		private boolean canRemove;

		public SLLIterator() {
			nextNode = head;
			iterModCount = modCount;
			canRemove = false;
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
			T retVal = nextNode.getElement();
			nextNode = nextNode.getNext();
			canRemove = true;
			return retVal;
		}

		@Override
		public void remove() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (!canRemove) {
				throw new IllegalStateException();
			}
			canRemove = false;
			if (head.getNext() == nextNode) { // removing head
				head = head.getNext();
				if (head == null) { // or size == 1 or size() == 1
					tail = null;
				}
			} else { // deeper in the list
				Node<T> previousPrevious = head;
				while (previousPrevious.getNext().getNext() != nextNode) {
					previousPrevious = previousPrevious.getNext();
				}
				previousPrevious.setNext(nextNode);
				if (previousPrevious.getNext() == null) { // new tail
					tail = previousPrevious;
				}
			}
			size--;
			modCount++;
			iterModCount++;
		}
	}
}
