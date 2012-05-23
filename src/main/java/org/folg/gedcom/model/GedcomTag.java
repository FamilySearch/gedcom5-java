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

package org.folg.gedcom.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple data structure for a gedcom node - json-friendly
 * User: Dallan
 * Date: 12/23/11
 */
public class GedcomTag implements Comparable<GedcomTag> {
   private static final Logger logger = LoggerFactory.getLogger("org.folg.gedcom.model");

   private String id;
   private String tag;
   private String ref;
   private String value;
   private String parentTagName; // used by ModelParser to store tags under string fields
   private List<GedcomTag> children;

   public GedcomTag(String id, String tag, String ref) {
      this.id = id;
      this.tag = tag;
      this.ref = ref;
      this.value = null;
      this.parentTagName = null;
      this.children = null;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getTag() {
      return tag;
   }

   public void setTag(String tag) {
      this.tag = tag;
   }

   public String getRef() {
      return ref;
   }

   public void setRef(String ref) {
      this.ref = ref;
   }

   public String getParentTagName() {
      return parentTagName;
   }

   public void setParentTagName(String parentTagName) {
      this.parentTagName = parentTagName;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public void appendValue(String value) {
      if (this.value == null) {
         this.value = value;
      } else {
         this.value += value;
      }
   }

   public List<GedcomTag> getChildren() {
      return children != null ? children : Collections.<GedcomTag>emptyList();
   }

   public void setChildren(List<GedcomTag> children) {
      this.children = children;
   }

   public void addChild(GedcomTag child) {
      if (children == null) {
         children = new ArrayList<GedcomTag>();
      }
      children.add(child);
   }

   private ArrayList<GedcomTag> getSortedChildren(List<GedcomTag> children) {
      ArrayList<GedcomTag> sortedChildren = new ArrayList<GedcomTag>(children.size());
      sortedChildren.addAll(children);
      Collections.sort(sortedChildren);
      return sortedChildren;
   }

   public boolean isEmpty() {
      return (id == null || id.length() == 0) &&
             (ref == null || ref.length() == 0) &&
             (parentTagName == null || parentTagName.length() == 0) &&
             (value == null || value.length() == 0) &&
             (children == null || children.size() == 0);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      GedcomTag gt = (GedcomTag) o;

      if (!(tag == null ? "" : tag).equals(gt.tag == null ? "" : gt.tag)) {
         return false;
      }
      if (!(id == null ? "" : id).equals(gt.id == null ? "" : gt.id)) {
         return false;
      }
      if (!(ref == null ? "" : ref).equals(gt.ref == null ? "" : gt.ref)) {
         return false;
      }
      if (!(parentTagName == null ? "" : parentTagName).equals(gt.parentTagName == null ? "" : gt.parentTagName)) {
         return false;
      }
      if (!(value == null ? "" : value).equals(gt.value == null ? "" : gt.value)) {
         return false;
      }
      if (getChildren().size() != gt.getChildren().size()) {
         return false;
      }

      // this is horribly inefficient - if we're going to do this a lot during production we need to rewrite it
      // but as-is, it's convenient for debugging
      ArrayList<GedcomTag> sortedChildren = getSortedChildren(getChildren());
      ArrayList<GedcomTag> compareChildren = getSortedChildren(gt.getChildren());

      for (int indx = 0; indx < sortedChildren.size(); indx++) {
         if (!sortedChildren.get(indx).equals(compareChildren.get(indx))) {
            logger.info("!tag="+sortedChildren.get(indx).toString()+"<=>"+compareChildren.get(indx).toString());
            return false;
         }
      }
      return true;
   }

   @Override
   public int hashCode() {
      int result = id != null && id.length() > 0 ? id.hashCode() : 0;
      result = 31 * result + (tag != null && tag.length() > 0 ? tag.hashCode() : 0);
      result = 31 * result + (ref != null && ref.length() > 0 ? ref.hashCode() : 0);
      result = 31 * result + (parentTagName != null && parentTagName.length() > 0 ? parentTagName.hashCode() : 0);
      result = 31 * result + (value != null && value.length() > 0 ? value.hashCode() : 0);

      // Calculate the children hash code based on the actual value of each child hashcode
      // result = 31 * result + (children != null ? children.hashCode() : 0);
      if (children != null) {
         for (GedcomTag child : getSortedChildren(children)) {
            result = 31 * result + child.hashCode();
         }
      }

      return result;
   }

   @Override
   public String toString() {
      StringBuilder buf = new StringBuilder();
      if (tag != null) {
         buf.append(" tag:"+tag);
      }
      if (id != null) {
         buf.append(" id:"+id);
      }
      if (ref != null) {
         buf.append(" ref:"+ref);
      }
      if (parentTagName != null) {
         buf.append(" parentTag:"+parentTagName);
      }
      if (value != null) {
         buf.append(" value:"+value);
      }
      if (children != null) {
         buf.append(" [");
         for (GedcomTag child : getSortedChildren(children)) {
            buf.append(child.toString());
         }
         buf.append(" ]");
      }
      return buf.toString();
   }

   public int compareTo(GedcomTag tag) {
      int c = (getTag() == null ? "" : getTag()).compareTo(tag.getTag() == null ? "" : tag.getTag());
      if (c != 0) return c;
      c = (getId() == null ? "" : getId()).compareTo(tag.getId() == null ? "" : tag.getId());
      if (c != 0) return c;
      c = (getRef() == null ? "" : getRef()).compareTo(tag.getRef() == null ? "" : tag.getRef());
      if (c != 0) return c;
      c = (getParentTagName() == null ? "" : getParentTagName()).compareTo(tag.getParentTagName() == null ? "" : tag.getParentTagName());
      if (c != 0) return c;
      c = (getValue() == null ? "" : getValue()).compareTo(tag.getValue() == null ? "" : tag.getValue());
      if (c != 0) return c;
      // compare hashcode because that compares children as well
      return new Integer(this.hashCode()).compareTo(new Integer(tag.hashCode()));
   }
}
