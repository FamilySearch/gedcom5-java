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
 * Date: 12/30/11
 */
public abstract class MediaContainer extends NoteContainer {
   private List<String> mediaRefs = null;
   private List<Media> media = null;

   /**
    * Use this function in place of getMedia and getMediaRefs
    * @param gedcom Gedcom
    * @return inline media as well as referenced media
    */
   public List<Media> getAllMedia(Gedcom gedcom) {
      List<Media> media = new ArrayList<Media>();
      for (String mediaRef : getMediaRefs()) {
         Media m = gedcom.getMedia(mediaRef);
         if (m != null) {
            media.add(m);
         }
      }
      media.addAll(getMedia());
      return media;
   }

   public List<String> getMediaRefs() {
      return mediaRefs != null ? mediaRefs : Collections.<String>emptyList();
   }

   public void setMediaRefs(List<String> mediaRefs) {
      this.mediaRefs = mediaRefs;
   }

   public void addMediaRef(String mediaRef) {
      if (mediaRefs == null) {
         mediaRefs = new ArrayList<String>();
      }
      mediaRefs.add(mediaRef);
   }

   public List<Media> getMedia() {
      return media != null ? media : Collections.<Media>emptyList();
   }

   public void setMedia(List<Media> media) {
      this.media = media;
   }

   public void addMedia(Media mediaObject) {
      if (media == null) {
         media = new ArrayList<Media>();
      }
      media.add(mediaObject);
   }

   public void visitContainedObjects(Visitor visitor) {
      for (Media m : getMedia()) {
         m.accept(visitor);
      }
      super.visitContainedObjects(visitor);
   }
}
