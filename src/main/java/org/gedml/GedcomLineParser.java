package org.gedml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: npowell
 * Date: May 2, 2007
 */
public class GedcomLineParser {
   private static final Pattern pGedcomLine = Pattern.compile(
           "^\\s*(\\d)\\s+(@([^@ ]+)@\\s+)?([a-zA-Z_0-9.]+)(\\s+@([^@ ]+)@)?(\\s(.*))?$", Pattern.DOTALL); // DOTALL for unicode line separator

   private static final int LEVEL_GROUP = 1;
   private static final int ID_GROUP = 3;
   private static final int TAG_GROUP = 4;
   private static final int XREF_GROUP = 6;
   private static final int VALUE_GROUP = 8;

   private Matcher m = null;

   public boolean parse(String line) {
      m = pGedcomLine.matcher(line);
      return m.find();
   }

   public String getLevel() {
      return m.group(LEVEL_GROUP);
   }

   public String getXRef() {
      return m.group(XREF_GROUP);
   }

   public String getID() {
      return m.group(ID_GROUP);
      // TODO - necessary?
//      String returnValue = m.group(AFTER_ID_GROUP);
//      if (returnValue == null || returnValue.length() == 0) {
//         return returnValue;
//      } else {
//         return returnValue.replaceAll("[|#/]+", "");
//      }
   }

   public String getTag() {
      return m.group(TAG_GROUP);
   }

   public String getValue() {
      return m.group(VALUE_GROUP);
   }
}
