package com.ebm;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TestUtils {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private  ObjectMapper om;
	@PersistenceContext
	private EntityManager entityManager;
	
	public ResultActions testPostExpectCreated( String endpoint, Object objectForPost) throws Exception {
		return mockMvc.perform( post("/"+endpoint) 
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(objectForPost))
				).
		         andExpect(status().isCreated())
				.andExpect(redirectedUrlPattern("**//**/"+endpoint+"/{[0-9]*}"));
	}
	public ResultActions testPostExpectForbidden( String endpoint, Object objectForPost) throws Exception {
		return mockMvc.perform( post("/"+endpoint) 
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(objectForPost))
				).
		         andExpect(status().isForbidden());
				
	}
	public ResultActions testPost( String endpoint, Object objectForPost, ResultMatcher statusExpect) throws Exception {
		return mockMvc.perform( post("/"+endpoint) 
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(objectForPost))
				).
		         andExpect(statusExpect);
	}
	public ResultActions testUpdateExpectSucess( String endpoint, Object objectForUpdate) throws Exception {
		return mockMvc.perform( put("/"+endpoint) 
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(objectForUpdate))
				).andExpect(status().isOk());
		         
	}
	
	public ResultActions testUpdateExpectedForbidden( String endpoint, Object objectForUpdate) throws Exception {
		return mockMvc.perform( put("/"+endpoint) 
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(objectForUpdate))
				).andExpect(status().isForbidden());
	}
	
	
	
	public ObjectMapper objectMapper() {
		return this.om;
	}
	public MockMvc mockMvc() {
		return this.mockMvc;
	}
	public EntityManager em() {
		return this.entityManager;
	}
}
