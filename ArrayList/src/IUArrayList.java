
import java.util.Arrays;

import java.util.ConcurrentModificationException;

import java.util.Iterator;

import java.util.ListIterator;

import java.util.NoSuchElementException;

/**
 * 
 * @author Cesardelgado and CS221-1
 *
 * 
 * 
 * @param <T>
 * 
 */

public class IUArrayList<T> implements IndexedUnsortedList<T> {

	private T[] array;

	private int rear;

	private int modCount;

	private static final int DEFAULT_CAPACITY = 10;

	public IUArrayList() {

		this(DEFAULT_CAPACITY);

	}

	@SuppressWarnings("unchecked")

	public IUArrayList(int initialCapacity) {

		array = (T[]) (new Object[initialCapacity]);

		rear = 0;

		modCount = 0;

	}

	@Override

	public void addToFront(T element) {

		expandIfNecessary();

		for (int i = rear; i > 0; i--) {

			array[i] = array[i - 1];

		}

		rear++;

		array[0] = element;

		modCount++;

	}

	@Override

	public void addToRear(T element) {

		expandIfNecessary();

		array[rear] = element;

		rear++;

		modCount++;

	}

	@Override

	public void add(T element) {

		addToRear(element);

	}

	@Override

	public void addAfter(T element, T target) {

		int targetIndex = indexOf(target);

		if (targetIndex < 0) {

			throw new NoSuchElementException();

		}

		add(targetIndex + 1, element);

	}

	@Override

	public void add(int index, T element) {

		if (index < 0 || index > size()) {

			throw new IndexOutOfBoundsException();

		}

		expandIfNecessary();

		for (int i = rear; i > index; i--) {

			array[i] = array[i - 1];

		}

		array[index] = element;

		rear++;

		modCount++;

	}

	/**
	 * 
	 * Double the array capacity if we've run out of room
	 * 
	 */

	private void expandIfNecessary() {

		if (array.length == rear) {

			array = Arrays.copyOf(array, array.length * 2);

		}

	}

	@Override

	public T removeFirst() {

		if (isEmpty()) {

			throw new NoSuchElementException();

		}

		T retVal = array[0];

		for (int i = 0; i < rear - 1; i++) {

			array[i] = array[i + 1];

		}

		rear--;

		modCount++;

		array[rear] = null;

		return retVal;

	}

	@Override

	public T removeLast() {

		if (isEmpty()) {

			throw new NoSuchElementException();

		}

		T retVal = array[rear - 1];

		array[rear - 1] = null;

		rear--;

		modCount++;

		return retVal;

	}

	@Override

	public T remove(T element) {

		int targetIndex = indexOf(element);

		if (targetIndex < 0) {

			throw new NoSuchElementException();

		}

		return remove(targetIndex);

	}

	@Override

	public T remove(int index) {

		if (index < 0 || index >= rear) {

			throw new IndexOutOfBoundsException();

		}

		T retVal = array[index];

		for (int i = index; i < rear - 1; i++) {

			array[i] = array[i + 1];

		}

		array[rear - 1] = null;

		rear--;

		modCount++;

		return retVal;

	}

	@Override

	public void set(int index, T element) {

		if (index < 0 || index >= rear) {

			throw new IndexOutOfBoundsException();

		}

		array[index] = element;

		modCount++;

	}

	@Override

	public T get(int index) {

		if (index < 0 || index >= rear) {

			throw new IndexOutOfBoundsException();

		}

		return array[index];

	}

	@Override

	public int indexOf(T element) {

		int index = -1;

		int curIndex = 0;

		while (curIndex < rear && index < 0) {

			if (array[curIndex].equals(element)) {

				index = curIndex;

			}

			curIndex++;

		}

		return index;

	}

	@Override

	public T first() {

		if (isEmpty()) {

			throw new NoSuchElementException();

		}

		return array[0];

	}

	@Override

	public T last() {

		if (isEmpty()) {

			throw new NoSuchElementException();

		}

		return array[rear - 1];

	}

	@Override

	public boolean contains(T target) {

		return (indexOf(target) >= 0);

	}

	@Override

	public boolean isEmpty() {

		return (rear == 0);

	}

	@Override

	public int size() {

		return rear;

	}

	@Override

	public String toString() {

		StringBuilder str = new StringBuilder();

		str.append("[");

		for (T element : this) {

			str.append(element.toString());

			str.append(", ");

		}

		if (!isEmpty()) {

			str.delete(str.length() - 2, str.length());

		}

		str.append("]");

		return str.toString();

	}

	@Override

	public Iterator<T> iterator() {

		return new ALIterator();

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
	 * 
	 * Iterator for IUArrayList
	 * 
	 */

	private class ALIterator implements Iterator<T> {

		private int next;

		private int iterModCount;

		private boolean canRemove;

		public ALIterator() {

			next = 0;

			iterModCount = modCount;

			canRemove = false;

		}

		@Override

		public boolean hasNext() {

			if (iterModCount != modCount) {

				throw new ConcurrentModificationException();

			}

			return next < rear;

		}

		@Override

		public T next() {

			if (!hasNext()) {

				throw new NoSuchElementException();

			}

			next++;

			canRemove = true;

			return array[next - 1];

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

			for (int i = next - 1; i < rear - 1; i++) {

				array[i] = array[i + 1];
			}
			next--;
			rear--;
			array[rear] = null;
			iterModCount++;
			modCount++;
		}
	}
}