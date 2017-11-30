/******************************************************************************
 * Student's name:
 * Student's group:
 * Data Structures. Grado en Informática. UMA.
******************************************************************************/

package dataStructures.vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SparseVector<T> implements Iterable<T> {

	protected interface Tree<T> {

		T get(int sz, int i);

		Tree<T> set(int sz, int i, T x);
	}

	// Unif Implementation

	protected static class Unif<T> implements Tree<T> {

		private T elem;

		public Unif(T e) {
			elem = e;
		}

		@Override
		public T get(int sz, int i) {
			return elem;
		}

		@Override
		public Tree<T> set(int sz, int i, T x) {
			if (sz == 1) {
				return new Unif<T>(x);
			}

			int mitad = sz / 2;
			Node<T> nn;
			if (i < mitad) {
				nn = new Node<T>(set(mitad, i, x), this);
			} else {
				nn = new Node<T>(this, set(mitad, i - mitad, x));
			}
			return nn;
		}

		@Override
		public String toString() {
			return "Unif(" + elem + ")";
		}
	}

	// Node Implementation

	protected static class Node<T> implements Tree<T> {

		private Tree<T> left, right;

		public Node(Tree<T> l, Tree<T> r) {
			left = l;
			right = r;
		}

		@Override
		public T get(int sz, int i) {
			int hsz = sz / 2;

			if (i < hsz)
				return left.get(hsz, i);
			else
				return right.get(hsz, i - hsz);
		}

		@Override
		public Tree<T> set(int sz, int i, T x) {
			int hsz = sz / 2;

			if (i < hsz)
				left = left.set(hsz, i, x);
			else
				right = right.set(hsz, i - hsz, x);

			return this;
		}

		protected Tree<T> simplify() {
			if (left instanceof Unif<?> && right instanceof Unif<?>) {
				Unif<T> leftU = (Unif<T>) left;
				Unif<T> rightU = (Unif<T>) right;
				if (leftU.elem.equals(rightU.elem))
					return leftU;
			}
			return this;
		}
	}

	// SparseVector Implementation

	private int size;
	private Tree<T> root;

	public SparseVector(int n, T elem) {
		if (n < 0) {
			throw new VectorException("N no puede ser negativo");
		}
		size = (int) Math.pow(2, n);
		root = new Unif<T>(elem);
	}

	public int size() {
		return size;
	}

	public T get(int i) {
		if ((i < 0) || (i > size - 1)) {
			throw new VectorException("El indice no esta en el rango");
		}

		return root.get(size, i);
	}

	public void set(int i, T x) {
		if ((i < 0) || (i > size - 1)) {
			throw new VectorException("El indice no esta en el rango");
		}
		root = root.set(size, i, x);
	}

	@Override
	public Iterator<T> iterator() {
		List<T> lista = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			lista.add(get(i));
		}

		return lista.iterator();
	}

	@Override
	public String toString() {
		return "SparseVector(" + size + "," + root + ")";
	}
}
