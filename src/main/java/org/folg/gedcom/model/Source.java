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
 * 
 * omit: data
 * add: media type, call number, type, uid, paren, italic, date
 */
public class Source extends MediaContainer {
   private String id = null;
   private String auth = null;
   private String titl = null;
   private String abbr = null;
   private String publ = null;
   private String text = null;
   private RepositoryRef repo = null;
   private List<RepositoryRef> repos = null;
   private String refn = null;
   private String rin = null;
   private Change chan = null;
   private String medi = null;
   private String caln = null;
   private String _type = null;
   private String typeTag = null;
   private String _uid = null;
   private String uidTag = null;
   private String _paren = null;
   private String _italic = null;
   private String date = null;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getAuthor() {
      return auth;
   }

   public void setAuthor(String auth) {
      this.auth = auth;
   }

   public String getTitle() {
      return titl;
   }

   public void setTitle(String titl) {
      this.titl = titl;
   }

   public String getAbbreviation() {
      return abbr;
   }

   public void setAbbreviation(String abbr) {
      this.abbr = abbr;
   }

   public String getPublicationFacts() {
      return publ;
   }

   public void setPublicationFacts(String publ) {
      this.publ = publ;
   }

   public String getText() {
      return text;
   }

   public void setText(String text) {
      this.text = text;
   }

   /**
    * @deprecated Use {@link #getRepositoryRefs()} instead.
    */
   @Deprecated
   public RepositoryRef getRepositoryRef() {
      List<RepositoryRef> list = getRepositoryRefs();
      return !list.isEmpty() ? list.get(0) : null;
   }

   /**
    * @deprecated Use {@link #addRepositoryRef(RepositoryRef)} or {@link #setRepositoryRefs(List)} instead.
    */
   @Deprecated
   public void setRepositoryRef(RepositoryRef repo) {
      this.repo = null;
      if (repo != null) {
         this.repos = new ArrayList<>(Collections.singletonList(repo));
      }
      else {
         this.repos = null;
      }
   }

   /**
    * @deprecated Use {@link #getRepositories(Gedcom)} instead.
    */
   @Deprecated
   public Repository getRepository(Gedcom gedcom) {
      RepositoryRef ref = getRepositoryRef();
      return ref != null ? ref.getRepository(gedcom) : null;
   }

   /**
    * If an old JSON populated 'repo', it migrates it to the 'repos' list
    * and clears 'repo' to avoid duplication.
    */
   private void migrateRepo() {
      if (repo != null) {
         if (repos == null) {
            repos = new ArrayList<>();
         }
         if (!repos.contains(repo)) {
            repos.add(repo);
         }
         repo = null;
      }
   }

   public List<RepositoryRef> getRepositoryRefs() {
      migrateRepo();
      return repos != null ? repos : Collections.emptyList();
   }

   public void setRepositoryRefs(List<RepositoryRef> repos) {
      this.repo = null;
      this.repos = repos;
   }

   public void addRepositoryRef(RepositoryRef repo) {
      migrateRepo();
      if (repos == null) {
         repos = new ArrayList<>();
      }
      repos.add(repo);
   }

   public List<Repository> getRepositories(Gedcom gedcom) {
      List<Repository> repositories = new ArrayList<>();
      for (RepositoryRef repositoryRef : getRepositoryRefs()) {
         Repository repository = repositoryRef.getRepository(gedcom);
         if (repository != null) {
            repositories.add(repository);
         }
      }
      return repositories;
   }

   public String getReferenceNumber() {
      return refn;
   }

   public void setReferenceNumber(String refn) {
      this.refn = refn;
   }

   public String getRin() {
      return rin;
   }

   public void setRin(String rin) {
      this.rin = rin;
   }

   public Change getChange() {
      return chan;
   }

   public void setChange(Change chan) {
      this.chan = chan;
   }

   public String getCallNumber() {
      return caln;
   }

   public void setCallNumber(String caln) {
      this.caln = caln;
   }

   public String getMediaType() {
      return medi;
   }

   public void setMediaType(String medi) {
      this.medi = medi;
   }

   public String getType() {
      return _type;
   }

   public void setType(String _type) {
      this._type = _type;
   }

   public String getTypeTag() {
      return typeTag;
   }

   public void setTypeTag(String typeTag) {
      this.typeTag = typeTag;
   }

   public String getUid() {
      return _uid;
   }

   public void setUid(String _uid) {
      this._uid = _uid;
   }

   public String getUidTag() {
      return uidTag;
   }

   public void setUidTag(String uidTag) {
      this.uidTag = uidTag;
   }

   public String getParen() {
      return _paren;
   }

   public void setParen(String paren) {
      this._paren = paren;
   }

   public String getItalic() {
      return _italic;
   }

   public void setItalic(String italic) {
      this._italic = italic;
   }

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public void accept(Visitor visitor) {
      if (visitor.visit(this)) {
         for (RepositoryRef repositoryRef : getRepositoryRefs()) {
            repositoryRef.accept(visitor);
         }
         if (chan != null) {
            chan.accept(visitor);
         }
         super.visitContainedObjects(visitor);
         visitor.endVisit(this);
      }
   }
}
