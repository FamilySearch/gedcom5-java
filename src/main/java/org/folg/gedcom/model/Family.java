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

/**
 * User: Dallan
 * Date: 12/27/11
 *
 * omit: Submitter, Reference type
 * add: Uid
 */
public class Family extends PersonFamilyCommonContainer {
   private List<SpouseRef> husbandRefs = null; // may be >1 if not sure which one (presumably one is preferred)
   private List<SpouseRef> wifeRefs = null;    // ditto
   private List<ChildRef> childRefs = null;

   private List<Person> getFamilyMembers(Gedcom gedcom, List<? extends SpouseRef> memberRefs, boolean preferredFirst) {
      List<Person> members = new ArrayList<Person>();
      for (SpouseRef memberRef : memberRefs) {
         Person member = memberRef.getPerson(gedcom);
         if (member != null) {
            if (preferredFirst && "Y".equals(memberRef.getPreferred())) {
               members.add(0, member);
            }
            else {
               members.add(member);
            }
         }
      }
      return members;
   }

   /**
    * Convenience function to dereference husband refs
    * Return preferred in first position
    * @param gedcom Gedcom
    * @return list of husbands, generally just one unless there are several alternatives with one preferred
    */
   public List<Person> getHusbands(Gedcom gedcom) {
      return getFamilyMembers(gedcom, getHusbandRefs(), true);
   }

   public List<SpouseRef> getHusbandRefs() {
      return husbandRefs != null ? husbandRefs : Collections.<SpouseRef>emptyList();
   }

   public void setHusbandRefs(List<SpouseRef> husbandRefs) {
      this.husbandRefs = husbandRefs;
   }
   
   public void addHusband(SpouseRef husband) {
      if (husbandRefs == null) {
         husbandRefs = new ArrayList<SpouseRef>();
      }
      husbandRefs.add(husband);
   }

   /**
    * Convenience function to dereference wife refs
    * Return preferred in first position
    * @param gedcom Gedcom
    * @return list of wives, generally just one unless there are several alternatives with one preferred
    */
   public List<Person> getWives(Gedcom gedcom) {
      return getFamilyMembers(gedcom, getWifeRefs(), true);
   }

   public List<SpouseRef> getWifeRefs() {
      return wifeRefs != null ? wifeRefs : Collections.<SpouseRef>emptyList();
   }

   public void setWifeRefs(List<SpouseRef> wifeRefs) {
      this.wifeRefs = wifeRefs;
   }

   public void addWife(SpouseRef wife) {
      if (wifeRefs == null) {
         wifeRefs = new ArrayList<SpouseRef>();
      }
      wifeRefs.add(wife);
   }

   /**
    * Convenience function to dereference child refs
    * @param gedcom Gedcom
    * @return list of children
    */
   public List<Person> getChildren(Gedcom gedcom) {
      return getFamilyMembers(gedcom, getChildRefs(), false);
   }

   public List<ChildRef> getChildRefs() {
      return childRefs != null ? childRefs : Collections.<ChildRef>emptyList();
   }

   public void setChildRefs(List<ChildRef> childRefs) {
      this.childRefs = childRefs;
   }

   public void addChild(ChildRef childRef) {
      if (childRefs == null) {
         childRefs = new ArrayList<ChildRef>();
      }
      childRefs.add(childRef);
   }

   public void accept(Visitor visitor) {
      visitor.visit(this);
      for (SpouseRef husband : getHusbandRefs()) {
         husband.accept(visitor, true);
      }
      for (SpouseRef wife : getWifeRefs()) {
         wife.accept(visitor, false);
      }
      for (ChildRef childRef : getChildRefs()) {
         childRef.accept(visitor);
      }
      super.visitContainedObjects(visitor);
      visitor.endVisit(this);
   }
}
