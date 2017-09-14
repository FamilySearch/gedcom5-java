/*
 * Copyright 2011 Foundation for On-Line Genealogy, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.folg.gedcom.tools;

import java.io.PrintWriter;
import java.util.*;

/**
 * General class to collect counts
 *
 * User: dallan
 * Date: 1/2/12
 */
public class CountsCollector {
   HashMap<String,Counter> counts;

   public class Counter {
      int count;
      Counter() {
         count = 0;
      }
      public String toString() {
         return String.valueOf(count);
      }
   }

   public int size() {
       return counts.size();
   }

   private class ValueComparator implements Comparator<Map.Entry<String,Counter>> {
      public int compare(Map.Entry<String,Counter> me1, Map.Entry<String,Counter> me2) {
         if (me1.getValue().count < me2.getValue().count ||
             (me1.getValue().count == me2.getValue().count &&
              me1.getKey().compareTo(me2.getKey()) < 0)) {
            return 1;
         }
         else {
            return -1;
         }
      }

      public boolean equals(Map.Entry<String,Counter> me1, Map.Entry<String,Counter> me2) {
         return (me1.getValue().count == me2.getValue().count &&
                 me1.getKey().equals(me2.getKey()));
      }
   }

   private class KeyComparator implements Comparator<Map.Entry<String,Counter>> {
      public int compare(Map.Entry<String,Counter> me1, Map.Entry<String,Counter> me2) {
         return (me1.getKey().compareTo(me2.getKey()));
      }

      public boolean equals(Map.Entry<String,Counter> me1, Map.Entry<String,Counter> me2) {
         return (me1.getKey().equals(me2.getKey()));
      }
   }

   public CountsCollector() {
      counts = new HashMap<String,Counter>();
   }

   public void add(String key) {
      add(key, 1);
   }

   public void add(String key, int count) {
      if (key != null) {
         Counter c = counts.get(key);
         if (c == null) {
            c = new Counter();
            counts.put(key, c);
         }
         c.count += count;
      }
   }

   public void remove(String key) {
      counts.remove(key);
   }

   public int getCount(String key) {
      Counter c = counts.get(key);
      if (c == null) {
         return 0;
      }
      else {
         return c.count;
      }
   }

   public Set<String> getKeys() {
      return counts.keySet();
   }

   public void addAll(Set<String> keys) {
      Iterator<String> iter = keys.iterator();
      while (iter.hasNext()) {
         add(iter.next());
      }
   }

   /**
    * Returns the collection sorted and filtered
    * @param byKey if true, sort by key; otherwise sort descending by count
    * @param minCount only include items greater than minCount
    * @return the collection sorted and filtered
    */
   public SortedSet<Map.Entry<String,Counter>> getSortedSet(boolean byKey, int minCount) {
      Comparator<Map.Entry<String,Counter>> comp;
      if (byKey) {
         comp = new KeyComparator();
      }
      else {
         comp = new ValueComparator();
      }
   	  SortedSet<Map.Entry<String,Counter>> ss = new TreeSet<Map.Entry<String,Counter>>(comp);
   	  Iterator<Map.Entry<String,Counter>> iter = counts.entrySet().iterator();
   	  while (iter.hasNext()) {
   	  	Map.Entry<String,Counter> entry = iter.next();
   	  	if (entry.getValue().count >= minCount) {
   	  		ss.add(entry);
   	  	}
   	  }
   	  return ss;
   }

   /**
    * Write the stats in sorted order
    * @param byKey if true, sort by key value; otherwise sort by count
    * @param minCount write out only those entries having count greater than minCount
    * @param writer PrintWriter to write to
    */
   public void writeSorted(boolean byKey, int minCount, PrintWriter writer) {
      // add all entries in the hash map appearing at least minCount times into the sorted set
      SortedSet<Map.Entry<String,Counter>> ss = getSortedSet(byKey, minCount);
      Iterator<Map.Entry<String,Counter>> iter = ss.iterator();
      while (iter.hasNext()) {
         Map.Entry<String,Counter> entry = iter.next();
         writer.println(entry.getKey() + "\t" + entry.getValue());
      }
      writer.flush();
   }
}
