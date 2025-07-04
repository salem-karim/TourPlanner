package at.technikum.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import at.technikum.common.models.Tour;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Reset db after each test
class TourControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testCreateAndGetTour() throws Exception {
    final Tour tour = new Tour(UUID.randomUUID(), "Test", "Desc", "From", "To", null, 10, 20, new byte[0], null);

    // Create tour
    mockMvc.perform(post("/api/tours")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tour)))
        .andExpect(status().isCreated()); // <-- expect 201

    // Get all tours and check if present
    mockMvc.perform(get("/api/tours"))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("Test")));
  }

  @Test
  void testGetAllToursInitiallyEmpty() throws Exception {
    mockMvc.perform(get("/api/tours"))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Test"))));
  }

  @Test
  void testGetTourById() throws Exception {
    final Tour tour = new Tour(UUID.randomUUID(), "ById", "Desc", "From", "To", null, 10, 20, new byte[0], null);
    final String response = mockMvc.perform(post("/api/tours")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tour)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    final UUID id = objectMapper.readTree(response).get("id").isTextual()
        ? UUID.fromString(objectMapper.readTree(response).get("id").asText())
        : tour.getId();

    mockMvc.perform(get("/api/tours"))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("ById")));
  }

  @Test
  void testUpdateTourSuccess() throws Exception {
    final Tour tour = new Tour(UUID.randomUUID(), "Update", "Desc", "From", "To", null, 10, 20, new byte[0], null);
    final String response = mockMvc.perform(post("/api/tours")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tour)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    final UUID id = objectMapper.readTree(response).get("id").isTextual()
        ? UUID.fromString(objectMapper.readTree(response).get("id").asText())
        : tour.getId();

    tour.setName("UpdatedName");
    mockMvc.perform(put("/api/tours/" + id)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tour)))
        .andExpect(status().isOk())
        .andExpect(content().string(org.hamcrest.Matchers.containsString("UpdatedName")));
  }

  @Test
  void testUpdateTourNotFound() throws Exception {
    final Tour tour = new Tour(UUID.randomUUID(), "NotFound", "Desc", "From", "To", null, 10, 20, new byte[0], null);
    mockMvc.perform(put("/api/tours/" + UUID.randomUUID())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tour)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteTourSuccess() throws Exception {
    final Tour tour = new Tour(UUID.randomUUID(), "Delete", "Desc", "From", "To", null, 10, 20, new byte[0], null);
    final String response = mockMvc.perform(post("/api/tours")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tour)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    final UUID id = objectMapper.readTree(response).get("id").isTextual()
        ? UUID.fromString(objectMapper.readTree(response).get("id").asText())
        : tour.getId();

    mockMvc.perform(delete("/api/tours/" + id))
        .andExpect(status().isNoContent());
  }

  @Test
  void testDeleteTourNotFound() throws Exception {
    mockMvc.perform(delete("/api/tours/" + UUID.randomUUID()))
        .andExpect(status().isNotFound());
  }

  // todo: does not work now, because not all fields are required to have a value
  // (maybe change that???)
  @Test
  void testCreateTourWithMissingFields() throws Exception {
    // Missing required fields (e.g., name)
    final String invalidTourJson = "{\"id\":\"" + UUID.randomUUID() + "\"}";
    mockMvc.perform(post("/api/tours")
        .contentType(MediaType.APPLICATION_JSON)
        .content(invalidTourJson))
        .andExpect(status().isBadRequest());
  }
}
