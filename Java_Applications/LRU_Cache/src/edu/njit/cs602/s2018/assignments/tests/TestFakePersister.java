package edu.njit.cs602.s2018.assignments.tests;

import edu.njit.cs602.s2018.assignments.Cacheable;
import edu.njit.cs602.s2018.assignments.SimpleCacheItem;
import edu.njit.cs602.s2018.assignments.SimpleFakePersister;

/**
 * Created by Ravi Varadarajan on 4/15/2018.
 */
public class TestFakePersister<K, T extends Cacheable<K>> extends SimpleFakePersister<K,T> {

    private int persistCnt = 0;
    private int retrieveCnt = 0;
    private int removeCnt = 0;

    @Override
    public void persistValue(T value) {
        super.persistValue(value);
        persistCnt += 1;
    }

    @Override
    public T getValue(K key) {
        retrieveCnt += 1;
        return super.getValue(key);
    }

    @Override
    public T removeValue(K key) {
        removeCnt += 1;
        return super.removeValue(key);
    }

    public int getPersistCnt() {
        return persistCnt;
    }

    public int getRetrieveCnt() {
        return retrieveCnt;
    }

    public int getRemoveCnt() {
        return removeCnt;
    }
}