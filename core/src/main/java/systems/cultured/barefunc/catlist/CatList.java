package systems.cultured.barefunc.catlist;

import java.util.AbstractSequentialList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

public abstract class CatList<T> extends AbstractSequentialList<T> {
    public static <T> CatList<T> emptyCatList() {
        return new Atom<>(Collections.emptyList());
    }

    @SafeVarargs
    public static <T> CatList<T> of(T... elements) {
        return new Atom<>(List.of(elements));
    }

    public CatList<T> append(T t) {
        return concat(Collections.singletonList(t));
    }

    public CatList<T> concat(List<T> other) {
        var otherCatList = (other instanceof CatList) ? (CatList<T>) other : new Atom<>(other);
        return new Concat<>(this, otherCatList);
    }

    @Override
    public abstract Stream<T> stream();

    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        if (index != 0) {
            throw new UnsupportedOperationException("CatList does not support starting iteration except at the beginning");
        }
        return new CatListIterator<>(iterator());
    }

    private static class Atom<T> extends CatList<T> {
        private final List<T> list;
        
        public Atom(List<T> list) {
            this.list = list;
        }

        @Override
        public int size() {
            return list.size();
        }

        @Override
        public Stream<T> stream() {
            return list.stream();
        }
    }

    private static class Concat<T> extends CatList<T> {
        private final CatList<T> head;
        private final CatList<T> tail;

        public Concat(CatList<T> head, CatList<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override
        public Stream<T> stream() {
            return Stream.concat(head.stream(), tail.stream());
        }

        @Override
        public int size() {
            return head.size() + tail.size();
        }
    }

    private static class CatListIterator<T> implements ListIterator<T> {
        private final Iterator<T> iterator;
        private int nextIndex;

        public CatListIterator(Iterator<T> iterator) {
            this.iterator = iterator;
            nextIndex = 0;
        }

        @Override
        public void add(T e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T next() {
            nextIndex++;
            return iterator.next();
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public T previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();            
        }

        @Override
        public void set(T e) {
            throw new UnsupportedOperationException();
        }
    }
}
