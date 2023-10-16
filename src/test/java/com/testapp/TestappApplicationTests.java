package com.testapp;

import com.testapp.controller.RouteController;
import com.testapp.entity.Route;
import com.testapp.repository.RouteRepository;
import com.testapp.util.ValueMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Objects;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class TestappApplicationTests {

	private static final String ENDPOINT_URL = "/routes";

	@InjectMocks
	private RouteController routeController;

	@MockBean
	private RouteRepository routeRepository;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setup(){
		this.mockMvc = MockMvcBuilders.standaloneSetup(this.routeController).build();
	}

	@Test
	public void createNewRouteTest() throws Exception {
		Route demoRoute = new Route(1L, "demoRouteName", "demoOrigin", "demoDestination", 300.46, true );
		when(routeRepository.save(any())).thenReturn(demoRoute);
		mockMvc.perform(MockMvcRequestBuilders
						.post(ENDPOINT_URL)
						.content(Objects.requireNonNull(ValueMapper.jsonAsString(demoRoute)))
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.results.id").exists());
	}

}
