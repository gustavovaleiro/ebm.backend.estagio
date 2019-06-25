package com.ebm;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.MultiValueMap;

import com.ebm.geral.resource.exception.ValidationError;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TestUtils {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper om;
	@PersistenceContext
	private EntityManager entityManager;
	public Integer getIdRedirect(MvcResult result, String ENDPOINT_NAME) {

		String url = result.getResponse().getRedirectedUrl();
		String id = url.split(ENDPOINT_NAME+"/")[1];
		return Integer.valueOf(id);
	}
	public ResultActions testPostExpectCreated(String endpoint, Object objectForPost) throws Exception {
		return mockMvc
				.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON)
						.content(om.writeValueAsString(objectForPost)))
				.andExpect(status().isCreated()).andExpect(redirectedUrlPattern("**//**" + endpoint + "/{[0-9]*}"));
	}

	public ResultActions testPostExpectForbidden(String endpoint, Object objectForPost) throws Exception {
		return mockMvc.perform(
				post(endpoint).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(objectForPost)))
				.andExpect(status().isForbidden());

	}

	public ResultActions testPost(String endpoint, Object objectForPost, ResultMatcher statusExpect) throws Exception {
		return mockMvc.perform(
				post(endpoint).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(objectForPost)))
				.andExpect(statusExpect);
	}

	public ResultActions testPutExpectNoContent(String endpoint, Object objectForUpdate) throws Exception {
		String writeValueAsString = om.writeValueAsString(objectForUpdate);
		return mockMvc.perform(
				put(endpoint).contentType(MediaType.APPLICATION_JSON).content(writeValueAsString))
				.andExpect(status().isNoContent());

	}

	public ResultActions testPutExpectedForbidden(String endpoint, Object objectForUpdate) throws Exception {
		return mockMvc.perform(
				put(endpoint).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(objectForUpdate)))
				.andExpect(status().isForbidden());
	}
	public ResultActions testPut(String endpoint, Object objectForUpdate, ResultMatcher status) throws Exception {
		return mockMvc.perform(
				put(endpoint).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(objectForUpdate)))
				.andExpect(status);
	}

	public ResultActions testGetRequestParams(String endpoint, MultiValueMap<String, String> params,
			ResultMatcher expectedStatus) throws Exception {
		return this.mockMvc.perform(get(endpoint).accept(MediaType.APPLICATION_JSON).params(params))
				.andExpect(expectedStatus);
	}

	public ResultActions testGetPage(String endpoint, MultiValueMap<String, String> params,
			ResultMatcher expectedStatus, Integer totalElementes, Integer totalPage) throws Exception {
		return this.testGetRequestParams(endpoint, params, expectedStatus)
				.andExpect(jsonPath("$.totalElements", equalTo(totalElementes)))
				.andExpect(jsonPath("$.totalPages", equalTo(totalPage)));
	}

	public ResultActions testGetExpectedSucess(String endpoint, Integer id) throws Exception {
		return this.mockMvc.perform(get(endpoint + "/" + id).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", equalTo(id)))
				.andExpect(jsonPath("$.id", notNullValue()));
	}

	public ResultActions testGet(String endpoint, Integer id, ResultMatcher status) throws Exception {
		return this.mockMvc.perform(get(endpoint + "/" + id).accept(MediaType.APPLICATION_JSON)).andExpect(status);
	}

	public ResultActions testDelete(String endpoint, ResultMatcher status) throws Exception {
		return this.mockMvc.perform(delete(endpoint))
				.andExpect(status);
	}
	
	public ValidationError getValidationErrorOf(MvcResult result) throws JsonParseException, JsonMappingException, IOException {
		String jsonResponseBody = result.getResponse().getContentAsString();
		ValidationError err = this.om.readValue(jsonResponseBody, ValidationError.class);
		return err;
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
