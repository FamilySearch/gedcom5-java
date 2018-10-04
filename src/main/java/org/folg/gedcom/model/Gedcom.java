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

import java.util.*;

/**
 * User: Dallan
 * Date: 12/24/11
 */
public class Gedcom extends ExtensionContainer {
   private Header head = null;
   private List<Submitter> subms = null;
   private Submission subn = null;
   private List<Person> people = null;
   private List<Family> families = null;
   private List<Media> media = null;
   private List<Note> notes = null;
   private List<Source> sources = null;
   private List<Repository> repositories = null;

   private transient Map<String,Person> personIndex;
   private transient Map<String,Family> familyIndex;
   private transient Map<String,Media> mediaIndex;
   private transient Map<String,Note> noteIndex;
   private transient Map<String,Source> sourceIndex;
   private transient Map<String,Repository> repositoryIndex;
   private transient Map<String,Submitter> submitterIndex;

   public Header getHeader() {
      return head;
   }

   public void setHeader(Header head) {
      this.head = head;
   }

   public List<Person> getPeople() {
      return people != null ? people : Collections.<Person>emptyList();
   }

   public Person getPerson(String id) {
      return personIndex.get(id);
   }

   public void setPeople(List<Person> people) {
      this.people = people;
   }

   public void addPerson(Person person) {
      if (people == null) {
         people = new ArrayList<Person>();
      }
      people.add(person);
      if (personIndex != null) {
         personIndex.put(person.getId(), person);
      }
   }

   public List<Family> getFamilies() {
      return families != null ? families : Collections.<Family>emptyList();
   }

   public Family getFamily(String id) {
      return familyIndex.get(id);
   }

   public void setFamilies(List<Family> families) {
      this.families = families;
   }

   public void addFamily(Family family) {
      if (families == null) {
         families = new ArrayList<Family>();
      }
      families.add(family);
      if (familyIndex != null) {
         familyIndex.put(family.getId(), family);
      }
   }

   public List<Media> getMedia() {
      return media != null ? media : Collections.<Media>emptyList();
   }

   public Media getMedia(String id) {
      return mediaIndex.get(id);
   }

   public void setMedia(List<Media> media) {
      this.media = media;
   }

   public void addMedia(Media m) {
      if (media == null) {
         media = new ArrayList<Media>();
      }
      media.add(m);
      if (mediaIndex != null) {
         mediaIndex.put(m.getId(), m);
      }
   }

   public List<Note> getNotes() {
      return notes != null ? notes : Collections.<Note>emptyList();
   }

   public Note getNote(String id) {
      return noteIndex.get(id);
   }

   public void setNotes(List<Note> notes) {
      this.notes = notes;
   }

   public void addNote(Note note) {
      if (notes == null) {
         notes = new ArrayList<Note>();
      }
      notes.add(note);
      if (noteIndex != null) {
         noteIndex.put(note.getId(), note);
      }
   }

   public List<Source> getSources() {
      return sources != null ? sources : Collections.<Source>emptyList();
   }

   public Source getSource(String id) {
      return sourceIndex.get(id);
   }

   public void setSources(List<Source> sources) {
      this.sources = sources;
   }

   public void addSource(Source source) {
      if (sources == null) {
         sources = new ArrayList<Source>();
      }
      sources.add(source);
      if (sourceIndex != null) {
         sourceIndex.put(source.getId(), source);
      }
   }

   public List<Repository> getRepositories() {
      return repositories != null ? repositories : Collections.<Repository>emptyList();
   }

   public Repository getRepository(String id) {
      return repositoryIndex.get(id);
   }

   public void setRepositories(List<Repository> repositories) {
      this.repositories = repositories;
   }

   public void addRepository(Repository repository) {
      if (repositories == null) {
         repositories = new ArrayList<Repository>();
      }
      repositories.add(repository);
      if (repositoryIndex != null) {
         repositoryIndex.put(repository.getId(), repository);
      }
   }

   /**
    * Use this function in place of Header.getSubmitter
    * @return Submitter top-level record or from header
    */
   public Submitter getSubmitter() {
      if (subms != null && !subms.isEmpty()) {
         return subms.get(0);
      }
      else if (head != null) {
         return submitterIndex.get(head.getSubmitterRef());
      }
      return null;
   }

   public Submitter getSubmitter(String id) { return submitterIndex.get(id); }

   public List<Submitter> getSubmitters() {
      return subms != null ? subms : Collections.<Submitter>emptyList();
   }

   public void setSubmitters(List<Submitter> submitters) {
      this.subms = submitters;
   }

   public void addSubmitter(Submitter submitter) {
      if (subms == null) {
         subms = new ArrayList<Submitter>();
      }
      subms.add(submitter);

      if (submitterIndex != null) {
         submitterIndex.put(submitter.getId(), submitter);
      }
   }

   /**
    * Use this function in place of Header.getSubmission
    * @return Submission top-level record or from header
    */
   public Submission getSubmission() {
      if (subn != null) {
         return subn;
      }
      else if (head != null) {
         return head.getSubmission();
      }
      return null;
   }

   public void setSubmission(Submission subn) {
      this.subn = subn;
   }

   public void createIndexes() {
      personIndex = new HashMap<String, Person>();
      for (Person person : getPeople()) {
         personIndex.put(person.getId(), person);
      }
      familyIndex = new HashMap<String, Family>();
      for (Family family : getFamilies()) {
         familyIndex.put(family.getId(), family);
      }
      mediaIndex = new HashMap<String, Media>();
      for (Media m : getMedia()) {
         mediaIndex.put(m.getId(), m);
      }
      noteIndex = new HashMap<String, Note>();
      for (Note note : getNotes()) {
         noteIndex.put(note.getId(), note);
      }
      sourceIndex = new HashMap<String, Source>();
      for (Source source : getSources()) {
         sourceIndex.put(source.getId(), source);
      }
      repositoryIndex = new HashMap<String, Repository>();
      for (Repository repository : getRepositories()) {
         repositoryIndex.put(repository.getId(), repository);
      }

      submitterIndex = new HashMap<String, Submitter>();
      for (Submitter submitter : getSubmitters()){
         submitterIndex.put(submitter.getId(), submitter);
      }
   }

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         if (head != null) {
            head.accept(visitor);
         }
         for (Submitter submitter : subms) {
            submitter.accept(visitor);
         }
         if (subn != null) {
            subn.accept(visitor);
         }
         for (Person person : getPeople()) {
            person.accept(visitor);
         }
         for (Family family : getFamilies()) {
            family.accept(visitor);
         }
         for (Media media : getMedia()) {
            media.accept(visitor);
         }
         for (Note note : getNotes()) {
            note.accept(visitor);
         }
         for (Source source : getSources()) {
            source.accept(visitor);
         }
         for (Repository repository : getRepositories()) {
            repository.accept(visitor);
         }
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}
