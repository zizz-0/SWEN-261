package com.needs.api.needsapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.NeedType;
import com.needs.api.needsapi.model.UrgencyTag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Need File DAO class
 * 
 * @author Team Swiss Pandas
 * @author SWEN Faculty
 */
@Tag("Persistence-tier")
public class NeedFileDAOTest {
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
        testNeeds[1] = new Need(98,"incubator",NeedType.EQUIPMENT,100.0,1,1, UrgencyTag.LOW, "some description", "image");
        testNeeds[2] = new Need(99,"milk formula",NeedType.EQUIPMENT,25.0,1,3, UrgencyTag.LOW, "some description", "image");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the need array above
        when(mockObjectMapper
            .readValue(new File("data/needs.json"),Need[].class))
                .thenReturn(testNeeds);
        needFileDAO = new NeedFileDAO("data/needs.json",mockObjectMapper);
    }

    @Test
    public void testGetNeeds() {
        // Invoke
        Need[] needs = needFileDAO.getNeeds();

        // Analyze
        assertEquals(needs.length,testNeeds.length);
        for (int i = 0; i < testNeeds.length;++i)
            assertEquals(needs[i],testNeeds[i]);
    }

    @Test
    public void testFindNeeds() {
        // Invoke
        Need[] needs = needFileDAO.findNeeds("in");

        // Analyze
        assertEquals(needs.length,2);
        assertEquals(needs[0],testNeeds[0]);
        assertEquals(needs[1],testNeeds[1]);
    }

    @Test
    public void testGetNeed() {
        // Invoke
        Need need = needFileDAO.getNeed(99);

        // Analzye
        assertEquals(need,testNeeds[2]);
    }

    @Test
    public void testDeleteNeed() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needFileDAO.deleteNeed(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test needs array - 1 (because of the delete)
        // Because needs attribute of NeedFileDAO is package private
        // we can access it directly
        assertEquals(needFileDAO.needs.size(),testNeeds.length-1);
    }

    @Test
    public void testCreateNeed() {
        // Setup
        Need need = new Need(100,"vaccines",NeedType.EQUIPMENT,30.0,50,0, UrgencyTag.LOW, "some description", "image");

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.createNeed(need),
                                "Unexpected exception thrown");
        // Analyze
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getId());
        assertEquals(actual.getId(),need.getId());
        assertEquals(actual.getName(),need.getName());
        assertEquals(actual.getPrice(),need.getPrice());
        assertEquals(actual.getQuantityFulfilled(),need.getQuantityFulfilled());
        assertEquals(actual.getType(),need.getType());
        assertEquals(actual.getUrgency(), need.getUrgency());
        assertEquals(actual.getDescription(), need.getDescription());
    }

    @Test
    public void testCreateNeedConflict() {
        // Setup
        Need need1 = new Need(101,null,NeedType.EQUIPMENT,30.0,50,0, UrgencyTag.LOW, "some description", "image");
        Need need2 = new Need(101,"test1",null,30.0,50,0, UrgencyTag.LOW, "some description", "image");
        Need need3 = new Need(102,"test2",NeedType.EQUIPMENT,0,50,0, UrgencyTag.LOW, "some description", "image");
        Need need4 = new Need(103,"test3",NeedType.EQUIPMENT,30.0,0,0, UrgencyTag.LOW, "some description", "image");
        Need need5 = new Need(104,"syringes",null,0,0,0, UrgencyTag.LOW, "some description", "image");

        // tests that when any fields are left empty or if need already exists, new need will not be created 

        // Invoke
        Need result1 = assertDoesNotThrow(() -> needFileDAO.createNeed(need1),
                                "Unexpected exception thrown");
        Need result2 = assertDoesNotThrow(() -> needFileDAO.createNeed(need2),
                                "Unexpected exception thrown");
        Need result3 = assertDoesNotThrow(() -> needFileDAO.createNeed(need3),
                                "Unexpected exception thrown");
        Need result4 = assertDoesNotThrow(() -> needFileDAO.createNeed(need4),
                                "Unexpected exception thrown");
        Need result5 = assertDoesNotThrow(() -> needFileDAO.createNeed(need5),
                                "Unexpected exception thrown");

        // Analyze
        assertEquals(null, result1);
        assertEquals(null, result2);
        assertEquals(null, result3);
        assertEquals(null, result4);
        assertEquals(null, result5);
    }

    @Test
    public void testUpdateNeed() {
        // Setup
        Need need = new Need(99,"milk formula",NeedType.EQUIPMENT,25.0,1,0, UrgencyTag.LOW, "some description", "image");

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Need actual = needFileDAO.getNeed(need.getId());
        assertEquals(actual,need);
    }

    @Test
    public void testOnePartiallyUpdateNeed() {
        // Setup
        Need need1 = new Need(99,null,null,0,3,0, UrgencyTag.LOW, "some description", "image");
        Need need2 = new Need(98,null,null,0,0,0, UrgencyTag.LOW, "some description", "image");

        // tests that any fields that are null stay the same

        // Invoke
        Need result1 = assertDoesNotThrow(() -> needFileDAO.updateNeed(need1),
                                "Unexpected exception thrown");
        Need result2 = assertDoesNotThrow(() -> needFileDAO.updateNeed(need2),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result1);
        assertNotNull(result2);
        Need actual1 = needFileDAO.getNeed(need1.getId());
        Need actual2 = needFileDAO.getNeed(need2.getId());
        assertEquals(actual1,result1);
        assertEquals(actual2,result2);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Need[].class));

        Need need = new Need(101,"blood oxygen monitor",NeedType.EQUIPMENT,15.0,1,0, UrgencyTag.LOW, "some description", "image");

        assertThrows(IOException.class,
                        () -> needFileDAO.createNeed(need),
                        "IOException not thrown");
    }

    @Test
    public void testGetNeedNotFound() {
        // Invoke
        Need need = needFileDAO.getNeed(90);

        // Analyze
        assertEquals(need,null);
    }

    @Test
    public void testDeleteNeedNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> needFileDAO.deleteNeed(96),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(needFileDAO.needs.size(),testNeeds.length);
    }

    @Test
    public void testUpdateNeedNotFound() {
        // Setup
        Need need = new Need(96,"worming paste",NeedType.EQUIPMENT,10.0,1,0, UrgencyTag.LOW, "some description", "image");

        // Invoke
        Need result = assertDoesNotThrow(() -> needFileDAO.updateNeed(need),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testTotalNeeded(){
        // Invoke
        int result = needFileDAO.getTotalNeeded();

        // Analyze
        assertEquals(52, result);
    }

    @Test
    public void testTotalFulfilled(){
        // Invoke
        int result = needFileDAO.getTotalFulfilled();

        // Analyze
        assertEquals(2, result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the NeedFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("data/needs.json"),Need[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new NeedFileDAO("data/needs.json",mockObjectMapper),
                        "IOException not thrown");
    }
}
