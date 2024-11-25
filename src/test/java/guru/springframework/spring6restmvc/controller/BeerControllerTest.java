package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

//@SpringBootTest
@WebMvcTest(BeerController.class) // This annotation is used to test the BeerController, which is a WebMvc controller
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc; // This is the MockMvc instance that will be used to call the controller methods and verify the results
    @MockitoBean
    BeerService beerService; // Declare a mock bean for the BeerService, which will be used in tests

    BeerServiceImpl beerServiceImpl = new BeerServiceImpl(); // Instantiate the BeerServiceImpl to use its methods directly

    @Test
    void getBeerById() throws Exception { // Define a test case for the getBeerById method

        Beer testBeer = beerServiceImpl.listBeers().get(0); // Retrieve the first beer from the list for testing

        given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer); // Mock the BeerService to return the testBeer when getBeerById is called

        // Act & Assert: Call the controller and verify the result
        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/beer/" + UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // Act & Assert: Call the controller and verify the result
        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/v1/beer/" + testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.beerName", is(testBeer.getBeerName())));
    }
    
}