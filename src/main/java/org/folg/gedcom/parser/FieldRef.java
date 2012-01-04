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

package org.folg.gedcom.parser;

import org.folg.gedcom.model.ExtensionContainer;

import javax.management.RuntimeErrorException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * User: Dallan
 * Date: 12/26/11
 */
public class FieldRef {
   private Object target;
   private String name;

   public FieldRef(Object target, String name) {
      this.target = target;
      this.name = name;
   }

   public String getClassFieldName() {
      return target.getClass().getName()+"."+name;
   }

   public Object getTarget() {
      return target;
   }

   public String getFieldName() {
      return name;
   }

   public void setValue(String value) throws NoSuchMethodException {
      try {
         Method method = target.getClass().getMethod("set"+name, String.class);
         method.invoke(target, value);
      } catch (InvocationTargetException e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   public String getValue() throws NoSuchMethodException {
      try {
         Method method = target.getClass().getMethod("get"+name);
         return (String)method.invoke(target);
      } catch (InvocationTargetException e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      } catch (IllegalAccessException e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      }
   }

   public void appendValue(String value) throws NoSuchMethodException {
      try {
         String currentValue = getValue();
         setValue((currentValue == null ? "" : currentValue) + value);
      } catch (NoSuchMethodException e) {
         // try "add"
         try {
            Method method = target.getClass().getMethod("add"+name, String.class);
            method.invoke(target, value);
         } catch (InvocationTargetException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e);
         } catch (IllegalAccessException e1) {
            e1.printStackTrace();
            throw new RuntimeException(e);
         }
      }
   }
}
