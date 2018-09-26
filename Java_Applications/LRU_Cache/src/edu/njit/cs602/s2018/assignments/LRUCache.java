package edu.njit.cs602.s2018.assignments;

import java.util.*;

//Program submitted by Safia Hareema//

/**
 * Created by Ravi Varadarajan on 2/20/2018.
 */
public class LRUCache<K, T extends Cacheable<K>> {

	// Size of cache that can be changed later
	private int cacheSize;

	// persister
	private Persister<K, T> persister;

	// counter of how many times cache is accessed
	private int persisterAccessed = 0;

	// counter of how many times item is found in cache
	private int cacheItemFound = 0;

	// Stores items for look up
	private final Map<K, T> itemMap;

	// list in LRU order
	private final List<K> lruList;

	/**
	 * To be implemented!! Iterator class only for only keys of cached items;
	 * order should be in LRU order, most recently used first
	 * 
	 * @param <K>
	 */
	private class CacheKeyIterator<K> implements Iterator<K> {

		private K[] cacheCopy;
		private int counter = 0;

		public CacheKeyIterator(List<K> lruList) {
			cacheCopy = (K[]) lruList.toArray();
			// this.cacheCopy = (LinkedList)lruList;
		}

		@Override
		public boolean hasNext() {
			boolean result = true;
			if (counter >= cacheCopy.length) {
				result = false;
			}
			return result;
		}

		@Override
		public K next() {
			K next = cacheCopy[counter];
			this.counter++;
			return next;
		}
	}

	/**
	 * Constructor
	 * 
	 * @param size
	 *            initial size of the cache which can change later
	 * @param persister
	 *            persister instance to use for accessing/modifying evicted
	 *            items
	 */
	public LRUCache(int size, Persister<? extends K, ? extends T> persister) {
		// set size of cache
		this.cacheSize = size;
		// instantiate persister
		this.persister = (Persister<K, T>) persister;
		// instantiate hashmap for the cache
		this.itemMap = new HashMap<K, T>();
		// instantiate lrulist as a linked list
		this.lruList = new LinkedList<K>();

	}

	/**
	 * Modify the cache size
	 * 
	 * @param newSize
	 */
	public void modifySize(int newSize) {
		this.cacheSize = newSize;
	}

	/**
	 * Get item with the key (need to get item even if evicted)
	 * 
	 * @param key
	 * @return
	 */
	public T getItem(K key) {
		T result = null;
		K evictedItem = null;
		// need to check if it's in cache map, if yes, return item and move item
		// to front of LRU cache
		// item in cache map
		if (this.itemMap.containsKey(key)) {
			result = (T) this.itemMap.get(key);
			// bring this item to the front of LRU list by removing it and
			// adding it to the front
			this.lruList.remove(key);
			this.lruList.add(0, key);
		}
		// item not in cache map, but is in persister --> return item, add to
		// cache map and LRU list, increment cacheItemFound counter
		else if (this.persister.getValue(key) != null) {
			this.persisterAccessed++;
			this.cacheItemFound++;
			result = (T) this.persister.getValue(key);
			// add item to cache map and LRU list--first check size
			if (this.lruList.size() == this.cacheSize) {
				// retrieve key of LRU item to evict- aka the end and evict
				evictedItem = this.lruList.remove(this.lruList.size() - 1);
				this.itemMap.remove(evictedItem);
				this.itemMap.put(key, (result));
				// add new MRU item to front of LRU list
				this.lruList.add(0, key);
			}
		}
		return result;
	}

	/**
	 * Add/Modify item with the key
	 * 
	 * @param item
	 *            item to be put
	 */
	public void putItem(T item) {
		K evictedItem = null;
		// Check if item exists
		T item1 = item;
		// cast key of the item as generic K
		K key = item.getKey();
		// persister has item already --> modifying item
		if (this.persister.getValue(key) != null) {
			this.persisterAccessed++;
			// cache has item
			if (this.itemMap.containsKey(key)) {
				// remove old item and add new one to persister map
				this.persister.removeValue(key);
				this.persister.persistValue(item1);
				// remove old item and add new one to cache map
				this.itemMap.remove(key);
				this.itemMap.put(key, item1);
				// bring to front of linked list as MRU item
				this.lruList.remove(key);
				this.lruList.add(0, key);
				// System.out.println("Updated cache");
			} else {
				this.cacheItemFound++;
				// cache doesn't have item --> need to add it
				if (this.lruList.size() == this.cacheSize) {
					// retrieve key of LRU item to evict- aka the end and evict
					evictedItem = this.lruList.remove(this.lruList.size() - 1);
					this.itemMap.remove(evictedItem);
					this.itemMap.put(key, (item1));
					// add new MRU item to front of LRU list
					this.lruList.add(0, key);
				}
				/// size is fine, just need to add to cache map and cache list
				else {
					this.itemMap.put(key, item1);
					this.lruList.add(0, key);
				}

			}
		}
		// persister doesn't have item, need to add to persister map
		else {
			if (this.lruList.size() == this.cacheSize) {
				// retrieve key of LRU item to evict- aka the end item
				evictedItem = this.lruList.remove(this.lruList.size() - 1);
				this.itemMap.remove(evictedItem);
				// this.itemMap.remove(evictedItem.getKey(), evictedItem);
				this.itemMap.put(key, (item1));
				// add new MRU item to front of LRU list
				this.lruList.add(0, key);
				this.persister.persistValue(item1);
			} else {
				// put in persister map
				// System.out.println("Persister empty");
				this.persister.persistValue(item1);
				// put in LRU map
				this.itemMap.put(key, item1);
				// System.out.println("Added to cache map");
				// add to linked list
				this.lruList.add(0, key);
				// System.out.println("Item key added to linked list");
			}
		}

	}

	/**
	 * Remove an item with the key
	 * 
	 * @param key
	 * @return item removed or null if it does not exist
	 */
	public T removeItem(K key) {
		T result = this.persister.removeValue(key);
		// check if K key is present in cache
		// K key exists in cache map --> delete from cache map, cache, and
		// persister
		if (this.itemMap.containsKey(key)) {
			// remove from cache
			this.lruList.remove(key);
			// remove from cache map
			result = this.itemMap.remove(key);
			// remove from persister
			// this.persister.removeValue(key);
		}
		// K key doesn't exist in cache, but it exists in the persister -->
		// delete from persister
		return result;

	}

	/**
	 * Get cache keys
	 * 
	 * @return
	 */
	public Iterator<K> getCacheKeys() {
		CacheKeyIterator<K> iterator = new CacheKeyIterator(this.lruList);
		/*
		 * K next = null; while (iterator.hasNext()){ next = iterator.next();
		 * System.out.print(next + " "); }
		 */
		return iterator;
	}

	/**
	 * Get fault rate (proportion of accesses (only for retrievals and
	 * modifications) not in cache)
	 * 
	 * @return
	 */
	public double getFaultRatePercent() {
		double result;
		if (this.persisterAccessed == 0) {
			result = 0;
		} else {
			result = ((double) this.cacheItemFound / (double) this.persisterAccessed) * 100;
		}
		return result;
	}

	/**
	 * Reset fault rate stats counters
	 */
	public void resetFaultRateStats() {
		this.persisterAccessed = 0;
		this.cacheItemFound = 0;
	}

	public static void main(String[] args) {
		LRUCache<String, SimpleCacheItem> cache = new LRUCache<>(20, new SimpleFakePersister<>());
		/*
		cache.putItem(new SimpleCacheItem("name" + 1, (int) (Math.random() * 200000)));
		cache.putItem(new SimpleCacheItem("name" + 2, (int) (Math.random() * 200000)));
		cache.putItem(new SimpleCacheItem("name" + 3, (int) (Math.random() * 200000)));
		cache.putItem(new SimpleCacheItem("name" + 4, (int) (Math.random() * 200000)));
		cache.putItem(new SimpleCacheItem("name" + 5, (int) (Math.random() * 200000)));
		cache.putItem(new SimpleCacheItem("name" + 6, (int) (Math.random() * 200000)));
		System.out.println("Done adding items");
		SimpleCacheItem cacheItem = cache.getItem("name1");
		System.out.println("Fault rate percent=" + cache.getFaultRatePercent());
		cache.putItem(new SimpleCacheItem("name" + 7, (int) (Math.random() * 200000)));
		cache.putItem(new SimpleCacheItem("name" + 1, (int) (Math.random() * 200000)));
		cache.putItem(new SimpleCacheItem("name" + 6, (int) (Math.random() * 200000)));
		cacheItem = cache.getItem("name2");
		cacheItem = cache.getItem("name7");
		cacheItem = cache.removeItem("name1");
		System.out.println("Fault rate percent=" + cache.getFaultRatePercent());
		*/

		// CacheKeyIterator.LRUCache.CacheKeyIterator iterator =
		// cache.getCacheKeys();
		Iterator test = cache.getCacheKeys();
		while (test.hasNext()) {
			Object next = test.next();
			System.out.print(next + " ");
			System.out.println("Aye");

		}
		// Iterator<K> iterator = cache.getCacheKeys();

		for (int i = 0; i < 100; i++) {
			cache.putItem(new SimpleCacheItem("name" + i, (int) (Math.random() * 200000)));
			String name = "name" + (int) (Math.random() * i);
			SimpleCacheItem cacheItem = cache.getItem(name);
			if (cacheItem != null) {
				System.out.println("Salary for " + name + "=" + cacheItem.getAnnualSalary());
			}
			cache.putItem(new SimpleCacheItem("name" + (int) (Math.random() * i), (int) (Math.random() * 200000)));
			name = "name" + (int) (Math.random() * i);
			cache.removeItem(name);
			System.out.println("Fault rate percent=" + cache.getFaultRatePercent());
		}
		for (int i = 0; i < 30; i++) {
			String name = "name" + (int) (Math.random() * i);
			cache.removeItem(name);
		}
		cache.resetFaultRateStats();
		cache.modifySize(50);
		for (int i = 0; i < 100; i++) {
			cache.putItem(new SimpleCacheItem("name" + i, (int) (Math.random() * 200000)));
			String name = "name" + (int) (Math.random() * i);
			SimpleCacheItem cacheItem = cache.getItem(name);
			if (cacheItem != null) {
				System.out.println("Salary for " + name + "=" + cacheItem.getAnnualSalary());
			}
			cache.putItem(new SimpleCacheItem("name" + (int) (Math.random() * i), (int) (Math.random() * 200000)));
			name = "name" + (int) (Math.random() * i);
			cache.removeItem(name);
			System.out.println("Fault rate percent=" + cache.getFaultRatePercent());
		}

	}

}
