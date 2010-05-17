package jornado;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A simple iteratable that has just one element
 * @author john
 */
public class SingleIterable<T> implements Iterable<T> {
    private final T obj;

    public SingleIterable(T obj) {
        this.obj = obj;
    }

    public Iterator<T> iterator() {
        return new SingleIterator<T>(obj);
    }

    static class SingleIterator<T> implements Iterator<T> {
        private final T obj;
        private boolean done = false;

        SingleIterator(T obj) {
            this.obj = obj;
        }

        public boolean hasNext() {
            return !done;
        }

        public T next() {
            if (done) throw new NoSuchElementException();
            done = true;
            return obj;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
