package com.needs.api.needsapi.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.NeedType;
import com.needs.api.needsapi.model.UrgencyTag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Need Comparator class
 * 
 * @author Team Swiss Pandas
 * 
 */
@Tag("Persistence-tier")
public class NeedComparatorTest {
    NeedFileDAO needFileDAO;
    Need[] testNeeds;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupNeedFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testNeeds = new Need[3];
        testNeeds[0] = new Need(97,"syringes",NeedType.EQUIPMENT,20.0, 50,0, UrgencyTag.LOW, "some description", "image");
        testNeeds[1] = new Need(98,"incubator",NeedType.EQUIPMENT,100.0,1,0, UrgencyTag.LOW, "some description", "image");
        testNeeds[2] = new Need(99,"milk formula",NeedType.EQUIPMENT,25.0,1,0, UrgencyTag.LOW, "some description", "image");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the need array above
        when(mockObjectMapper
            .readValue(new File("data/needs.json"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("data/needs.json",mockObjectMapper);
    }

    @Test
    public void testBothUrgent(){
        // Setup
        Need need1 = new Need(100,"vaccines",NeedType.EQUIPMENT,30.0,50,1, UrgencyTag.HIGH, "some description", "image");
        Need need2 = new Need(100,"vaccines",NeedType.EQUIPMENT,30.0,50,1, UrgencyTag.HIGH, "some description", "image");
        ArrayList<Need> needs = new ArrayList<>();
        needs.add(need1);
        needs.add(need2);
        NeedComparator comparator = new NeedComparator();

        // Invoke
        Collections.sort(needs, comparator);

        // Analyze
        assertNotNull(needs);
        assertEquals(needs, needs);
    }

    @Test
    public void testFirstUrgent(){
        // Setup
        Need need1 = new Need(100,"vaccines",NeedType.EQUIPMENT,30.0,50,0, UrgencyTag.HIGH, "some description", "image");
        Need need2 = new Need(100,"vaccines",NeedType.EQUIPMENT,30.0,50,0, UrgencyTag.LOW, "some description", "image");
        ArrayList<Need> needs = new ArrayList<>();
        needs.add(need1);
        needs.add(need2);
        NeedComparator comparator = new NeedComparator();

        // Invoke
        Collections.sort(needs, comparator);

        // Analyze
        assertNotNull(needs);
        assertEquals(needs.get(0), need1);
        assertEquals(needs.get(1), need2);
    }

    @Test
    public void testLastUrgent(){
        // Setup
        Need need1 = new Need(100,"vaccines",NeedType.EQUIPMENT,30.0,50,0, UrgencyTag.LOW, "some description", "image");
        Need need2 = new Need(100,"vaccines",NeedType.EQUIPMENT,30.0,50,0, UrgencyTag.HIGH, "some description", "image");
        ArrayList<Need> needs = new ArrayList<>();
        needs.add(need1);
        needs.add(need2);
        NeedComparator comparator = new NeedComparator();

        // Invoke
        Collections.sort(needs, comparator);

        // Analyze
        assertNotNull(needs);
        assertEquals(needs.get(0), need2);
        assertEquals(needs.get(1), need1);
    }

}
