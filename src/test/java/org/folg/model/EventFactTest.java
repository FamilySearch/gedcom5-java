package org.folg.model;

import org.folg.gedcom.model.EventFact;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class EventFactTest {
    @Test
    public void testPropertyResourceBundle() throws Exception {
        Map<String, String> displayTypes = EventFact.DISPLAY_TYPE;
        assertNotNull(displayTypes);
        assertFalse(displayTypes.isEmpty());
        ResourceBundle resourceBundle = ResourceBundle.getBundle("EventFact", Locale.getDefault());
        assertNotNull(resourceBundle);
        for (String tag : displayTypes.keySet()) {
            String key = displayTypes.get(tag);
            assertNotNull(key);
            String displayType = resourceBundle.getString(key);
            assertNotNull(displayType);
        }
    }
}
