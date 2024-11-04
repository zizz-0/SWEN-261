package com.needs.api.needsapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.needs.api.needsapi.persistence.NeedDAO;
import com.needs.api.needsapi.model.Need;
import com.needs.api.needsapi.model.NeedType;
import com.needs.api.needsapi.model.UrgencyTag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Test the Need Controller class
 * 
 * @author Team Swiss Pandas
 */
@Tag("Controller-tier")
public class NeedControllerTest {
    private NeedController needController;
    private NeedDAO mockNeedDAO;

    /**
     * Before each test, create a new NeedController object and inject
     * a mock Need DAO
     */
    @BeforeEach
    public void setupNeedController() {
        mockNeedDAO = mock(NeedDAO.class);
        needController = new NeedController(mockNeedDAO);
    }

    @Test
    public void testGetNeed() throws IOException {  // getNeed may throw IOException
        // Setup
        Need need = new Need(98,"syringes",NeedType.EQUIPMENT,50.0,1,0, UrgencyTag.LOW, "some description", "image");
      
        // When the same id is passed in, our mock Need DAO will return the Need object
        when(mockNeedDAO.getNeed(need.getId())).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(need.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testGetNeedNotFound() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 99;
        // When the same id is passed in, our mock Need DAO will return null, simulating
        // no need found
        when(mockNeedDAO.getNeed(needId)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetNeedHandleException() throws Exception { // createNeed may throw IOException
        // Setup
        int needId = 99;
        // When getNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needController.getNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    /*****************************************************************
     * The following tests will fail until all NeedController methods
     * are implemented.
     ****************************************************************/

    @Test
    public void testCreateNeed() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(98,"syringes",NeedType.EQUIPMENT,50.0,1,0, UrgencyTag.LOW, "some description", "image");

        // when createNeed is called, return true simulating successful
        // creation and save
        when(mockNeedDAO.createNeed(need)).thenReturn(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testCreateNeedConflict() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(98,"syringes",NeedType.EQUIPMENT,50.0,1,0, UrgencyTag.LOW, "some description", "image");
      
        // when createNeed is called, return false simulating failed
        // creation and save
        when(mockNeedDAO.createNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateNeedHandleException() throws IOException {  // createNeed may throw IOException
        // Setup
        Need need = new Need(98,"syringes",NeedType.EQUIPMENT,50.0,1,0, UrgencyTag.LOW, "some description", "image");

        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).createNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.createNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateNeed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(98,"syringes",NeedType.EQUIPMENT,50.0,1,0, UrgencyTag.LOW, "some description", "image");
      
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockNeedDAO.updateNeed(need)).thenReturn(need);
        ResponseEntity<Need> response = needController.updateNeed(need);
        need.setName("Milk Formula");

        // Invoke
        response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(need,response.getBody());
    }

    @Test
    public void testUpdateNeedFailed() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(98,"syringes",NeedType.EQUIPMENT,50.0,1,0, UrgencyTag.LOW, "some description", "image");
      
        // when updateNeed is called, return true simulating successful
        // update and save
        when(mockNeedDAO.updateNeed(need)).thenReturn(null);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateNeedHandleException() throws IOException { // updateNeed may throw IOException
        // Setup
        Need need = new Need(98,"syringes",NeedType.EQUIPMENT,50.0,1,0, UrgencyTag.LOW, "some description", "image");
      
        // When updateNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).updateNeed(need);

        // Invoke
        ResponseEntity<Need> response = needController.updateNeed(need);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetNeeds() throws IOException { // getNeeds may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(98,"syringes",NeedType.EQUIPMENT,50.0,1,0, UrgencyTag.LOW, "some description", "image");
        needs[1] = new Need(99,"milk formula",NeedType.EQUIPMENT,25.0,1,0, UrgencyTag.LOW, "some description", "image");

        // When getNeeds is called return the needs created above
        when(mockNeedDAO.getNeeds()).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testGetNeedsHandleException() throws IOException { // getNeeds may throw IOException
        // Setup
        // When getNeeds is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeeds();

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeeds();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetNeedsArray() throws IOException { // getNeeds may throw IOException
        // Setup
        Need[] needs = new Need[2];
        needs[0] = new Need(98,"syringes",NeedType.EQUIPMENT,50.0,1,0, UrgencyTag.LOW, "some description", "image");
        needs[1] = new Need(99,"milk formula",NeedType.EQUIPMENT,25.0,1,0, UrgencyTag.LOW, "some description", "image");

        int[] needsIds = {needs[0].getId(), needs[1].getId()};

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeedsArray(needsIds);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testGetNeedsArrayHandleException() throws IOException { // getNeeds may throw IOException
        // Setup
        int[] needsIds = {1};
        // When getNeeds is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).getNeed(1);

        // Invoke
        ResponseEntity<Need[]> response = needController.getNeedsArray(needsIds);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchNeeds() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "in";
        Need[] needs = new Need[2];
        needs[0] = new Need(98,"vaccines",NeedType.EQUIPMENT,25.0,1,0, UrgencyTag.LOW, "some description", "image");
        needs[1] = new Need(99,"incubator",NeedType.EQUIPMENT,100.0,1,0, UrgencyTag.LOW, "some description", "image");

        // When findNeeds is called with the search string, return the two
        /// needs above
        when(mockNeedDAO.findNeeds(searchString)).thenReturn(needs);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(needs,response.getBody());
    }

    @Test
    public void testSearchNeedsNotFound() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "z";
        // When findNeeds is called with the search string, return the two
        /// needs above
        when(mockNeedDAO.findNeeds(searchString)).thenReturn(null);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertEquals(null,response.getBody());
    }

    @Test
    public void testSearchNeedsHandleException() throws IOException { // findNeeds may throw IOException
        // Setup
        String searchString = "an";
        // When createNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).findNeeds(searchString);

        // Invoke
        ResponseEntity<Need[]> response = needController.searchNeeds(searchString);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteNeed() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // when deleteNeed is called return true, simulating successful deletion
        when(mockNeedDAO.deleteNeed(needId)).thenReturn(true);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedNotFound() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // when deleteNeed is called return false, simulating failed deletion
        when(mockNeedDAO.deleteNeed(needId)).thenReturn(false);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteNeedHandleException() throws IOException { // deleteNeed may throw IOException
        // Setup
        int needId = 99;
        // When deleteNeed is called on the Mock Need DAO, throw an IOException
        doThrow(new IOException()).when(mockNeedDAO).deleteNeed(needId);

        // Invoke
        ResponseEntity<Need> response = needController.deleteNeed(needId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testFulfillQuantity() throws IOException{
        //Setup
        int quantity = 2;
        int needId = 98;
        Need need = new Need(needId,"Syringes",NeedType.EQUIPMENT,50.0,2,0, UrgencyTag.LOW, "some description", "image");
        Need need2 = new Need(needId,"Syringes",NeedType.EQUIPMENT,50.0,2,quantity, UrgencyTag.LOW, "some description", "image");
        when(mockNeedDAO.updateNeed(need2)).thenReturn(need2);
        when(mockNeedDAO.getNeed(needId)).thenReturn(need);
        //Invoke
        ResponseEntity<Need> response = needController.fulfillQuanity(98, quantity);
        //Analyze
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(quantity, response.getBody().getQuantityFulfilled());
    }

    @Test
    public void testFulfillQuantityNoNeed() throws IOException{
        //Setup
        int quantity = 2;
        int needId = 98;
        when(mockNeedDAO.getNeed(needId)).thenReturn(null);
        //Invoke
        ResponseEntity<Need> response = needController.fulfillQuanity(98, quantity);
        //Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    public void testFulfillQuantityServerError() throws IOException{
        //Setup
        int quantity = 2;
        int needId = 98;
        doThrow(new IOException()).when(mockNeedDAO).getNeed(needId);
        //Invoke
        ResponseEntity<Need> response = needController.fulfillQuanity(98, quantity);
        //Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    @Test
    public void testGetProgress(){
        int needed = 1;
        int fulfilled = 2;
        when(mockNeedDAO.getTotalFulfilled()).thenReturn(needed);
        when(mockNeedDAO.getTotalNeeded()).thenReturn(fulfilled);
        Integer[] expected = new Integer[2];
        expected[0] = fulfilled;
        expected[1] = needed;

        ResponseEntity<Integer[]> response = needController.getProgress();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected[0], response.getBody()[0]);
        assertEquals(expected[1], response.getBody()[1]);
    }
}
