package guru.springframework.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

//@SpringBootTest
@WebMvcTest(BeerController.class) // This annotation is used to test the BeerController, which is a WebMvc controller
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc; // This is the MockMvc instance that will be used to call the controller methods and verify the results
    @MockitoBean
    BeerService beerService; // Declare a mock bean for the BeerService, which will be used in tests
    BeerServiceImpl beerServiceImpl;
    @Autowired
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<UUID> beerIdArgumentCaptor; // Create an ArgumentCaptor for the UUID class
    @Captor
    private ArgumentCaptor<Beer> beerArgumentCaptor;  // Create an ArgumentCaptor for the Beer class

    @BeforeEach
        //this will run before each test case
    void setUp() {
        beerServiceImpl = new BeerServiceImpl(); // Instantiate the BeerServiceImpl to use its methods directly
    }

    @Test
    void testPatchBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Beer Name");
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/beer/" + beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(beerService).patchBeerById(beerIdArgumentCaptor.capture(),
                beerArgumentCaptor.capture()); // Verify that the patchBeerById method was called with the correct arguments (UUID and Beer object)
        assertThat(beer.getId()).isEqualTo(beerIdArgumentCaptor.getValue());

        
    }

    @Test
    void testDeleteBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);
        mockMvc.
                perform(MockMvcRequestBuilders.delete("/api/v1/beer/" + beer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(beerService).deleteById(beerIdArgumentCaptor.capture()); // Verify that the deleteById method was called with the correct argument (UUID

        System.out.println("beerIdArgumentCaptor.getValue() = " + beerIdArgumentCaptor.getValue());

        assertThat(beer.getId()).isEqualTo(beerIdArgumentCaptor.getValue());
    }

    @Test
    void testUpdateBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/beer/" + beer.getId()).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(beer)));

        verify(beerService).updateBeerById(any(UUID.class), any(Beer.class));//verify that the updateBeerById method was called with the correct arguments (beer id and beer object) when the PUT request is made
    }

    @Test
    void testCreateNewBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().get(0);
        beer.setVersion(null);
        beer.setId(null);
        System.out.println("beer json = " + objectMapper.writeValueAsString(beer));

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));//returning the second element of the list so the id is not null
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/beer").accept(MediaType.APPLICATION_JSON)// Set the Accept header to JSON for the request, indicating that the client expects JSON response
                        .contentType(MediaType.APPLICATION_JSON)// Set the Content-Type header to JSON for the request, indicating that the client is sending JSON data
                        .content(objectMapper.writeValueAsString(beer))) // Convert the Beer object to JSON and set it as the request body
                .andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.header().exists("Location"));

    }

    @Test
    void getBeerById() throws Exception { // Define a test case for the getBeerById method

        Beer testBeer = beerServiceImpl.listBeers().get(0); // Retrieve the first beer from the list for testing

        given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer); // Mock the BeerService to return the testBeer when getBeerById is called

        // Act & Assert: Call the controller and verify the result
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/" + UUID.randomUUID()).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        // Act & Assert: Call the controller and verify the result
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/" + testBeer.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.jsonPath("$.id", is(testBeer.getId().toString()))).andExpect(MockMvcResultMatchers.jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void testListBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer").accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.jsonPath("$.length()", is(3)));
    }

}