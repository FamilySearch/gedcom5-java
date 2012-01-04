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

/**
 * User: Dallan
 * Date: 12/27/11
 * 
 * omit: Linked objects, Reference number, Rin
 * add: File, primary, type, scrapbook
 */
public class Media extends NoteContainer {
   private String id = null;
   private String form = null;
   private String titl = null;
   private String blob = null;
   private Change chan = null;
   private String _file = null;
   private String fileTag = null;
   private String _prim = null;
   private String _type = null;
   private String _scbk = null;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getFormat() {
      return form;
   }

   public void setFormat(String form) {
      this.form = form;
   }

   public String getTitle() {
      return titl;
   }

   public void setTitle(String titl) {
      this.titl = titl;
   }

   public String getBlob() {
      return blob;
   }

   public void setBlob(String blob) {
      this.blob = blob;
   }

   public Change getChange() {
      return chan;
   }

   public void setChange(Change chan) {
      this.chan = chan;
   }

   public String getFile() {
      return _file;
   }

   public void setFile(String _file) {
      this._file = _file;
   }

   public String getFileTag() {
      return fileTag;
   }

   public void setFileTag(String fileTag) {
      this.fileTag = fileTag;
   }

   public String getPrimary() {
      return _prim;
   }

   public void setPrimary(String prim) {
      this._prim = prim;
   }

   public String getType() {
      return _type;
   }

   public void setType(String type) {
      this._type = type;
   }

   public String getScrapbook() {
      return _scbk;
   }

   public void setScrapbook(String scbk) {
      this._scbk = scbk;
   }

   public void accept(Visitor visitor) {
      visitor.visit(this);
      if (chan != null) {
         chan.accept(visitor);
      }
      super.visitContainedObjects(visitor);
      visitor.endVisit(this);
   }
}
