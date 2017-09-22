package com.agbreder.compiler.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * ArrayList leve
 * 
 * @author bernardobreder
 * @param <E>
 */
public class LightArrayList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

	private List<E> delagate;

	/**
	 * Construtor
	 */
	public LightArrayList() {
	}

	/**
	 * Construtor
	 * 
	 * @param es
	 */
	public LightArrayList(E... es) {
		this.delagate = new ArrayList<E>();
		for (int n = 0; n < es.length; n++) {
			this.delagate.add(es[n]);
		}
	}

	@Override
	public int size() {
		if (this.delagate == null) {
			return 0;
		} else {
			return this.delagate.size();
		}
	}

	@Override
	public boolean isEmpty() {
		if (this.delagate == null) {
			return true;
		} else {
			return this.delagate.isEmpty();
		}
	}

	@Override
	public boolean contains(Object o) {
		if (this.delagate == null) {
			return false;
		} else {
			return this.delagate.contains(o);
		}
	}

	@Override
	public Iterator<E> iterator() {
		if (this.delagate == null) {
			return new EmptyIterator<E>();
		} else {
			return this.delagate.iterator();
		}
	}

	@Override
	public Object[] toArray() {
		if (this.delagate == null) {
			return new Object[0];
		} else {
			return this.delagate.toArray();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) {
		if (this.delagate == null) {
			return (T[]) Arrays.copyOf(new Object[0], 0, a.getClass());
		} else {
			return this.delagate.toArray(a);
		}
	}

	@Override
	public boolean add(E e) {
		if (this.delagate == null) {
			this.delagate = new ArrayList<E>();
		}
		return this.delagate.add(e);
	}

	@Override
	public boolean remove(Object o) {
		if (this.delagate == null) {
			return false;
		} else {
			return this.delagate.remove(o);
		}
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if (this.delagate == null) {
			return false;
		} else {
			return this.delagate.containsAll(c);
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c.size() > 0) {
			if (this.delagate == null) {
				this.delagate = new ArrayList<E>();
			}
			return this.delagate.addAll(c);
		} else {
			return false;
		}
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (c.size() > 0) {
			if (this.delagate == null) {
				this.delagate = new ArrayList<E>();
			}
			return this.delagate.addAll(index, c);
		} else {
			return false;
		}
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if (this.delagate == null) {
			return false;
		} else {
			return this.delagate.removeAll(c);
		}
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		if (this.delagate == null) {
			return false;
		} else {
			return this.delagate.retainAll(c);
		}
	}

	@Override
	public void clear() {
		if (this.delagate != null) {
			this.delagate.clear();
		}
	}

	@Override
	public E get(int index) {
		if (this.delagate == null) {
			throw new IndexOutOfBoundsException(((Integer) index).toString());
		} else {
			return this.delagate.get(index);
		}
	}

	@Override
	public E set(int index, E element) {
		if (this.delagate == null) {
			throw new IndexOutOfBoundsException(((Integer) index).toString());
		} else {
			return this.delagate.set(index, element);
		}
	}

	@Override
	public void add(int index, E element) {
		if (this.delagate == null) {
			this.delagate = new ArrayList<E>();
		}
		this.delagate.add(index, element);
	}

	@Override
	public E remove(int index) {
		if (this.delagate == null) {
			return null;
		} else {
			return this.delagate.remove(index);
		}
	}

	@Override
	public int indexOf(Object o) {
		if (this.delagate == null) {
			return -1;
		} else {
			return this.delagate.indexOf(o);
		}
	}

	@Override
	public int lastIndexOf(Object o) {
		if (this.delagate == null) {
			return -1;
		} else {
			return this.delagate.lastIndexOf(o);
		}
	}

	@Override
	public ListIterator<E> listIterator() {
		if (this.delagate == null) {
			return new EmptyIterator<E>();
		} else {
			return this.delagate.listIterator();
		}
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		if (this.delagate == null) {
			return new EmptyIterator<E>();
		} else {
			return this.delagate.listIterator(index);
		}
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		if (this.delagate == null) {
			return new LightArrayList<E>();
		} else {
			return this.delagate.subList(fromIndex, toIndex);
		}
	}

	public String toString() {
		if (this.delagate == null) {
			return "[]";
		} else {
			return this.delagate.toString();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		if (this.delagate == null) {
			return 0;
		} else {
			return this.delagate.hashCode();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this.delagate == null) {
			if (obj instanceof List) {
				return ((Collection<?>) obj).size() == 0;
			} else {
				return false;
			}
		} else {
			return this.delagate.equals(obj);
		}
	}

	private static class EmptyIterator<E> implements ListIterator<E> {

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public E next() {
			return null;
		}

		@Override
		public void remove() {
		}

		@Override
		public boolean hasPrevious() {
			return false;
		}

		@Override
		public E previous() {
			return null;
		}

		@Override
		public int nextIndex() {
			return 0;
		}

		@Override
		public int previousIndex() {
			return -1;
		}

		@Override
		public void set(E e) {
		}

		@Override
		public void add(E e) {
		}

	}

}
