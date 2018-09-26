package edu.njit.cs602.s2018.assignments.tests;

import edu.njit.cs602.s2018.assignments.*;

import java.util.Iterator;

/**
 * Created by Ravi Varadarajan on 2/22/2018.
 */
public class LRUCacheTests {

    private static int passCnt = 0;
    private static int points = 0;

    private static class CacheItem implements Cacheable<String> {

        private final String key, value;
        public CacheItem(String key, String value) {
            this.key = key;
            this.value = value;
        }
        @Override
        public String getKey() {
            return key;
        }
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof CacheItem)) {
                return false;
            }
            CacheItem other = (CacheItem) obj;
            return this.key.equals(other.key) && this.value.equals(other.value);
        }
        @Override
        public String toString() {
            return "key: " + key + ",value:" + value;
        }
    }

    /**
     *  Check if adding an item works when cache is not full
     */
    private static void test1() {
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(1, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item);
            assert fakePersister.getPersistCnt() == 1;
            assert fakePersister.getRetrieveCnt() == 1;
            assert lruCache.getItem("John Doe").equals(item);
            assert lruCache.getFaultRatePercent() == 0.0;
            assert fakePersister.getRetrieveCnt() == 1;
            assert fakePersister.getRemoveCnt() == 0;
            passCnt += 1;
            points += 6;
            System.out.println("\nTest1 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest1 failed\n");
        }
    }

    /**
     *  Check if adding an item works when cache is full
     */
    private static void test2() {
        boolean passed = true;
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(1, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item1 = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item1);
            CacheItem item2 = new CacheItem("Jane Doe", "employee2");
            lruCache.putItem(item2);
            assert lruCache.getFaultRatePercent() == 0.0;
            assert fakePersister.getPersistCnt() == 2;
            assert lruCache.getItem("Jane Doe").equals(item2);
            assert lruCache.getFaultRatePercent() == 0.0;
            assert lruCache.getItem("John Doe").equals(item1);
            try {
                assert fakePersister.getRetrieveCnt() == 3;
                points += 3;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            assert fakePersister.getRemoveCnt() == 0;
            try {
                assert lruCache.getFaultRatePercent() > 0;
                points += 3;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            assert lruCache.getFaultRatePercent() == 50.0;
            points += 2;
            passCnt += passed ? 1 : 0;
            System.out.println("\nTest2 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest2 failed\n");
        }
    }

    /**
     *  Check if updating an item works when cache is not full
     */
    private static void test3() {
        boolean passed = true;
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(2, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item1 = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item1);
            CacheItem item2 = new CacheItem("Jane Doe", "employee2");
            lruCache.putItem(item2);
            assert lruCache.getItem("Jane Doe").equals(item2);
            assert lruCache.getItem("John Doe").equals(item1);
            assert lruCache.getFaultRatePercent() == 0.0;
            assert fakePersister.getPersistCnt() == 2;
            assert fakePersister.getRetrieveCnt() == 2;
            CacheItem item3 = new CacheItem("Jane Doe", "employee3");
            lruCache.putItem(item3);
            try {
                assert fakePersister.getPersistCnt() == 3;
                assert fakePersister.getRetrieveCnt() == 2;
                points += 2;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            assert lruCache.getItem("Jane Doe").equals(item3);
            try {
                assert fakePersister.getRetrieveCnt() == 2;
                points += 2;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            assert lruCache.getFaultRatePercent() == 0.0;
            passCnt += passed ? 1 : 0;
            points += 2;
            System.out.println("\nTest3 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest3 failed\n");
        }
    }

    /**
     *  Check if updating an item works when cache is full
     */
    private static void test4() {
        boolean passed = true;
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(3, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item1 = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item1);
            CacheItem item2 = new CacheItem("Jane Doe", "employee2");
            lruCache.putItem(item2);
            CacheItem item3 = new CacheItem("Chandra Sekhar", "employee3");
            lruCache.putItem(item3);
            CacheItem item4 = new CacheItem("Jane Liu", "employee4");
            lruCache.putItem(item4);
            assert lruCache.getFaultRatePercent() == 0.0;
            assert fakePersister.getPersistCnt() == 4;
            assert fakePersister.getRetrieveCnt() == 4;
            assert lruCache.getItem("Jane Liu").equals(item4);
            assert lruCache.getItem("Jane Doe").equals(item2);
            assert lruCache.getItem("Chandra Sekhar").equals(item3);
            assert lruCache.getItem("John Doe").equals(item1);
            try {
                assert fakePersister.getPersistCnt() == 4;
                assert fakePersister.getRetrieveCnt() == 5;
                assert lruCache.getFaultRatePercent() == 25.0;
                points += 2;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            CacheItem item5 = new CacheItem("Jane Liu", "employee5");
            lruCache.putItem(item5);
            try {
                assert fakePersister.getPersistCnt() == 5;
                assert fakePersister.getRetrieveCnt() == 6;
                points += 3;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            try {
                assert lruCache.getFaultRatePercent() > 0;
                points += 3;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            try {
                assert lruCache.getFaultRatePercent() == 40.0;
                points += 2;
            } catch (Throwable t) {
                t.printStackTrace();
            }
            passCnt += passed ? 1 : 0;
            System.out.println("\nTest4 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest4 failed\n");
        }
    }

    /**
     *  Check if removing an item works when item is in cache
     */
    private static void test5() {
        boolean passed = true;
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(3, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item1 = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item1);
            CacheItem item2 = new CacheItem("Jane Doe", "employee2");
            lruCache.putItem(item2);
            CacheItem item3 = new CacheItem("Chandra Sekhar", "employee3");
            lruCache.putItem(item3);
            assert lruCache.getItem("Jane Doe").equals(item2);
            assert lruCache.getItem("Chandra Sekhar").equals(item3);
            assert lruCache.getItem("John Doe").equals(item1);
            lruCache.removeItem("John Doe");
            try {
                assert fakePersister.getRemoveCnt() == 1;
                points += 4;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            assert lruCache.getItem("John Doe") == null;
            passCnt += passed ? 1 : 0;
            points += 2;
            System.out.println("\nTest5 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest5 failed\n");
        }
    }

    /**
     *  Check if removing an item works when item is not in cache
     */
    private static void test6() {
        boolean passed = true;
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(3, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item1 = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item1);
            CacheItem item2 = new CacheItem("Jane Doe", "employee2");
            lruCache.putItem(item2);
            CacheItem item3 = new CacheItem("Chandra Sekhar", "employee3");
            lruCache.putItem(item3);
            CacheItem item4 = new CacheItem("Jane Liu", "employee4");
            lruCache.putItem(item4);
            assert lruCache.getItem("Jane Doe").equals(item2);
            assert lruCache.getItem("Chandra Sekhar").equals(item3);
            assert lruCache.getItem("John Doe").equals(item1);
            assert lruCache.getItem("Jane Liu").equals(item4);
            lruCache.removeItem("Jane Doe");
            try {
                assert fakePersister.getRemoveCnt() == 1;
                points += 4;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            assert lruCache.getItem("Jane Doe") == null;
            passCnt += passed ? 1 : 0;;
            points += 2;
            System.out.println("\nTest6 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest6 failed\n");
        }
    }

    private static boolean sameAsArray(Iterator<String> iter, String [] arr) {
        int idx = 0;
        while (iter.hasNext()) {
            if (!iter.next().equals(arr[idx++])) {
                return false;
            }
        }
        return true;
    }

    // check if cache key iterator works correctly when items are just added
    private static void test7() {
        boolean passed = true;
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(3, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item1 = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item1);
            CacheItem item2 = new CacheItem("Jane Doe", "employee2");
            lruCache.putItem(item2);
            CacheItem item3 = new CacheItem("Chandra Sekhar", "employee3");
            lruCache.putItem(item3);
            int retrievalCnt = fakePersister.getRetrieveCnt();
            try {
                assert sameAsArray(lruCache.getCacheKeys(), new String[]{"Chandra Sekhar", "Jane Doe", "John Doe"});
                assert fakePersister.getRetrieveCnt() == retrievalCnt;
                points += 3;
            }  catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            CacheItem item4 = new CacheItem("Jane Liu", "employee4");
            lruCache.putItem(item4);
            retrievalCnt = fakePersister.getRetrieveCnt();
            assert sameAsArray(lruCache.getCacheKeys(), new String [] {"Jane Liu", "Chandra Sekhar", "Jane Doe"});
            assert fakePersister.getRetrieveCnt() == retrievalCnt;
            passCnt += passed ? 1 : 0;
            points += 4;
            System.out.println("\nTest7 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest7 failed\n");
        }
    }

    // check if cache key iterator works correctly when items are modified
    private static void test8() {
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(3, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item1 = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item1);
            CacheItem item2 = new CacheItem("Jane Doe", "employee2");
            lruCache.putItem(item2);
            CacheItem item3 = new CacheItem("Chandra Sekhar", "employee3");
            lruCache.putItem(item3);
            CacheItem item4 = new CacheItem("Jane Liu", "employee4");
            lruCache.putItem(item4);
            assert sameAsArray(lruCache.getCacheKeys(), new String [] {"Jane Liu", "Chandra Sekhar", "Jane Doe"});
            CacheItem item5 = new CacheItem("John Doe", "employee5");
            lruCache.putItem(item5);
            int retrievalCnt = fakePersister.getRetrieveCnt();
            assert sameAsArray(lruCache.getCacheKeys(), new String [] {"John Doe", "Jane Liu", "Chandra Sekhar"});
            assert fakePersister.getRetrieveCnt() == retrievalCnt;
            passCnt += 1;
            points += 7;
            System.out.println("\nTest8 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest8 failed\n");
        }
    }

    // check if cache key iterator works correctly when items are retrieved
    private static void test9() {
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(3, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item1 = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item1);
            CacheItem item2 = new CacheItem("Jane Doe", "employee2");
            lruCache.putItem(item2);
            CacheItem item3 = new CacheItem("Chandra Sekhar", "employee3");
            lruCache.putItem(item3);
            lruCache.getItem("Jane Doe");
            assert sameAsArray(lruCache.getCacheKeys(), new String [] {"Jane Doe", "Chandra Sekhar", "John Doe"});
            lruCache.getItem("Chandra Sekhar");
            assert sameAsArray(lruCache.getCacheKeys(), new String [] {"Chandra Sekhar", "Jane Doe", "John Doe"});
            passCnt += 1;
            points += 8;
            System.out.println("\nTest9 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest9 failed\n");
        }
    }

    // check if cache key iterator works correctly when items are removed
    private static void test10() {
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(3, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item1 = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item1);
            CacheItem item2 = new CacheItem("Jane Doe", "employee2");
            lruCache.putItem(item2);
            CacheItem item3 = new CacheItem("Chandra Sekhar", "employee3");
            lruCache.putItem(item3);
            assert sameAsArray(lruCache.getCacheKeys(), new String [] {"Chandra Sekhar", "Jane Doe", "John Doe"});
            lruCache.removeItem("Jane Doe");
            assert sameAsArray(lruCache.getCacheKeys(), new String [] {"Chandra Sekhar", "John Doe"});
            passCnt += 1;
            points += 8;
            System.out.println("\nTest10 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest10 failed\n");
        }
    }

    // check if reset stats works correctly
    private static void test11() {
        boolean passed = true;
        try {
            TestFakePersister<String, CacheItem> fakePersister = new TestFakePersister<>();
            LRUCache<String, CacheItem> lruCache = new LRUCache<>(2, fakePersister);
            assert lruCache.getFaultRatePercent() == 0.0;
            CacheItem item1 = new CacheItem("John Doe", "employee1");
            lruCache.putItem(item1);
            CacheItem item2 = new CacheItem("Jane Doe", "employee2");
            lruCache.putItem(item2);
            CacheItem item3 = new CacheItem("Chandra Sekhar", "employee3");
            lruCache.putItem(item3);
            lruCache.getItem("Jane Doe");
            lruCache.getItem("John Doe");
            try {
                assert lruCache.getFaultRatePercent() > 0;
                points += 3;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            try {
                assert lruCache.getFaultRatePercent() == 50.0;
                points += 1;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            lruCache.resetFaultRateStats();
            assert lruCache.getFaultRatePercent() == 0.0;
            lruCache.getItem("Chandra Sekhar");
            lruCache.getItem("Jane Doe");
            try {
                assert lruCache.getFaultRatePercent() > 0;
                points += 3;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            try {
                assert lruCache.getFaultRatePercent() == 100.0;
                points += 1;
            } catch (Throwable t) {
                t.printStackTrace();
                passed = false;
            }
            passCnt += passed ? 1 : 0;
            System.out.println("\nTest11 passed\n");
        } catch(Throwable t) {
            t.printStackTrace();
            System.out.println("\nTest11 failed\n");
        }
    }



    public static void main(String [] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
        test8();
        test9();
        test10();
        test11();
        System.out.println(String.format("%d tests passed",passCnt));
        System.out.println(String.format("total test points=%d",points));
    }
}