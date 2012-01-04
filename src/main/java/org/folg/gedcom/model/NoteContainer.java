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
 * Date: 12/29/11
 */
public abstract class NoteContainer extends ExtensionContainer {
   private List<NoteRef> noteRefs = null;
   private List<Note> notes = null;

   /**
    * Use this function in place of getNotes and getNoteRefs
    * @param gedcom Gedcom
    * @return inline notes as well as referenced notes
    */
   public List<Note> getAllNotes(Gedcom gedcom) {
      List<Note> notes = new ArrayList<Note>();
      for (NoteRef noteRef : getNoteRefs()) {
         Note note = noteRef.getNote(gedcom);
         if (note != null) {
            notes.add(note);
         }
      }
      notes.addAll(getNotes());
      return notes;
   }

   public List<NoteRef> getNoteRefs() {
      return noteRefs != null ? noteRefs : Collections.<NoteRef>emptyList();
   }

   public void setNoteRefs(List<NoteRef> noteRefs) {
      this.noteRefs = noteRefs;
   }

   public void addNoteRef(NoteRef noteRef) {
      if (noteRefs == null) {
         noteRefs = new ArrayList<NoteRef>();
      }
      noteRefs.add(noteRef);
   }

   public List<Note> getNotes() {
      return notes != null ? notes : Collections.<Note>emptyList();
   }

   public void setNotes(List<Note> notes) {
      this.notes = notes;
   }

   public void addNote(Note note) {
      if (notes == null) {
         notes = new ArrayList<Note>();
      }
      notes.add(note);
   }

   public void visitContainedObjects(Visitor visitor) {
      for (NoteRef noteRef : getNoteRefs()) {
         noteRef.accept(visitor);
      }
      for (Note note : getNotes()) {
         note.accept(visitor);
      }
      super.visitContainedObjects(visitor);
   }
}
