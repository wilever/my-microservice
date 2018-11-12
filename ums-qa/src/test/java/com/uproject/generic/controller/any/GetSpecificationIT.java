package com.uproject.generic.controller.any;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uproject.generic.AnyApplication;
import com.uproject.generic.controller.AnyController;
import com.uproject.generic.domain.repository.ViewAnyRepository;
import com.uproject.generic.domain.repository.entity.view.ViewAnyEntity;
import com.uproject.generic.domain.utils.AnyConstant;
import com.uproject.library.ums.domain.repository.specification.USpecificationOperator;
import com.uproject.library.ums.domain.util.UTestAssistant;

/**
 * Integration test for {@link AnyController#get(String, String, String, org.springframework.data.domain.Pageable, org.springframework.data.web.PagedResourcesAssembler)}.
 * 
 * @author Wilever Gomez [wilevergomez@gmail.com]
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		classes = AnyApplication.class)
@ActiveProfiles("test")
public class GetSpecificationIT {
	
	/** The rest template. */
	@Autowired
	private TestRestTemplate restTemplate;

	/** The assistant. */
	@Autowired
	private UTestAssistant<ViewAnyEntity> assistant;

	/** The any repository. */
	@Autowired
	private ViewAnyRepository repository;
	
	/** The path to controller. */
	private String pathToController = AnyConstant.PATH_TO_CONTROLLER;
	
	/** The entity 0. */
	private ViewAnyEntity entity0 = new ViewAnyEntity(0);
	
	/** The entity 1. */
	private ViewAnyEntity entity1 = new ViewAnyEntity(1);
	
	/** The entity 2. */
	private ViewAnyEntity entity2 = new ViewAnyEntity(2);
	
	/** The entity 3. */
	private ViewAnyEntity entity3 = new ViewAnyEntity(3);
	
	/** The entity 4. */
	private ViewAnyEntity entity4 = new ViewAnyEntity(4);
	
	/** The entity 5. */
	private ViewAnyEntity entity5 = new ViewAnyEntity(5);
	
	/** The entities. */
	private List<ViewAnyEntity> entities = new ArrayList<>();
	
	/** The response. */
	private ResponseEntity<String> response;
	
	/** The expected. */
	private ResponseEntity<String> expected;
	
	/** Parameter to search. */
	private String parameter0 = "anyId";
	
	/** Parameter to search. */
	private String parameter1 = "activeInd";
	
	/** Parameter to search. */
	private String parameter2 = "field";
	
	/**
	 * Save entity test.
	 *
	 * @param entity The entity
	 * @param id The id
	 * @param activeInd The active ind
	 * @param field The field
	 */
	public void saveEntityTest(
			ViewAnyEntity entity,
			String parameter1,
			String parameter2) {
		entity.setActiveInd(parameter1);
		entity.setField(parameter2);
		repository.save(entity);
	}
	
	/**
	 * Initialize the data.
	 */
	@Before
	public void initData() {
		assistant.setRestTemplate(restTemplate.getRestTemplate());
		assistant.setPathToController(pathToController);
		entities.clear();
		saveEntityTest(
				entity0, "Y", "field");
		saveEntityTest(
				entity1, "N", "field");
		saveEntityTest(
				entity2, "Y", "field");
		saveEntityTest(
				entity3, "N", "anotherField");
		saveEntityTest(
				entity4, "Y", "anotherField");
		saveEntityTest(
				entity5, null, "anotherField");
	}
	
	/**
	 * One entity by ID, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void OneEntityByID() throws Exception {
		response = assistant.get("?search="+parameter0+":"+entity0.getAnyId());
		entities.add(entity0);
		expected = assistant.getResponse(entities,new PageMetadata(20,0,1),null,null);
		compareResponse();
	}
	
	/**
	 * Search by one field, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void SearchByOneField() throws Exception {
		String search = parameter1+USpecificationOperator.EQUALITY.operator+"Y";
		response = assistant.get("?search="+search);
		entities.add(entity0);
		entities.add(entity2);
		entities.add(entity4);
		PageMetadata metadata = new PageMetadata(20, 0, entities.size());
		expected = assistant.getResponse(entities, metadata, null, null);
		compareResponse();
	}
	
	/**
	 * Filter by two fields, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void FilterByTwoFields() throws Exception {
		String filter = parameter1+USpecificationOperator.EQUALITY.operator+"Y"+USpecificationOperator.SEPARATOR+
						parameter2+USpecificationOperator.EQUALITY.operator+"field";
		response = assistant.get("?filter="+filter);
		entities.add(entity0);
		entities.add(entity2);
		PageMetadata metadata = new PageMetadata(20, 0, entities.size());
		expected = assistant.getResponse(entities, metadata, null, null);
		compareResponse();
	}
	
	/**
	 * Filter by three fields, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void FilterByThreeFields() throws Exception {
		String filter = parameter1+USpecificationOperator.EQUALITY.operator+"Y"+USpecificationOperator.SEPARATOR+
						parameter2+USpecificationOperator.EQUALITY.operator+"field"+USpecificationOperator.SEPARATOR+
						parameter0+USpecificationOperator.GREATER_THAN.operator+entity1.getAnyId();
		response = assistant.get("?filter="+filter);
		entities.add(entity2);
		PageMetadata metadata = new PageMetadata(20, 0, entities.size());
		expected = assistant.getResponse(entities, metadata, null, null);
		compareResponse();
	}
	
	/**
	 * Search by two fields, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void searchByTwoFields() throws Exception {
		String search = parameter1+USpecificationOperator.NEGATION.operator+"Y"+USpecificationOperator.SEPARATOR+
						parameter0+USpecificationOperator.GREATER_THAN.operator+entity4.getAnyId();
		response = assistant.get("?search="+search);
		entities.add(entity1);
		entities.add(entity3);
		entities.add(entity5);
		PageMetadata metadata = new PageMetadata(20, 0, entities.size());
		expected = assistant.getResponse(entities, metadata, null, null);
		compareResponse();
	}
	
	/**
	 * Search and filter, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void SearchAndFilter() throws Exception {
		String filter = parameter2+USpecificationOperator.NEGATION.operator+"field"+USpecificationOperator.SEPARATOR+
						parameter0+USpecificationOperator.GREATER_THAN.operator+entity4.getAnyId();
		String search = parameter1+USpecificationOperator.NEGATION.operator+"Y"+USpecificationOperator.SEPARATOR+
						parameter0+USpecificationOperator.GREATER_THAN.operator+entity3.getAnyId();
		response = assistant.get("?search="+search+"&filter="+filter);
		entities.add(entity5);
		PageMetadata metadata = new PageMetadata(20, 0, entities.size());
		expected = assistant.getResponse(entities, metadata, null, null);
		compareResponse();
	}
	
	/**
	 * Search filter by any data, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void SearchFilterByAnyData() throws Exception {
		response = assistant.get("?search=sds:dsd&filter=sdas!dasd");
		expected = assistant.getResponse(new Exception());
		compareResponse();
	}
	
	/**
	 * Search by not compatible data, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void SearchByNotCompatibleData() throws Exception {
		response = assistant.get("?search="+parameter0+":asd&filter="+parameter0+":asd");
		expected = assistant.getResponse(new Exception());
		compareResponse();
	}
	
	/**
	 * Filter by not compatible data, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void FilterByNotCompatibleData() throws Exception {
		response = assistant.get("?filter="+parameter0+":*3*");
		expected = assistant.getResponse(new Exception());
		compareResponse();
	}
	
	/**
	 * Filter by unknown field, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void FilterByUnknownField() throws Exception {
		response = assistant.get("?filter=fiesdsdld:*3*");
		expected = assistant.getResponse(new Exception());
		compareResponse();
	}
	
	/**
	 * Search by unknown field, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void SearchByUnknownField() throws Exception {
		response = assistant.get("?search=fiesdsdld:*3*");
		expected = assistant.getResponse(new Exception());
		compareResponse();
	}
	
	/**
	 * Filter or search by unknown field, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void FilterOrSearchByUnknownField() throws Exception {
		response = assistant.get("?filter="+parameter2+":field,"+parameter1+"!Y&search="+parameter0+"<2,activessssInd:N");
		expected = assistant.getResponse(new Exception());
		compareResponse();
	}
	
	/**
	 * Compare responses.
	 */
	private void compareResponse() {
		assertThat(response.
						getBody()).
		isEqualTo(expected.
						getBody());
		assertThat(response.
						getStatusCode()).
		isEqualTo(expected.
						getStatusCode());
	}
}
