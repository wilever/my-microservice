package com.uproject.generic.controller.anydetail;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.uproject.generic.AnyApplication;
import com.uproject.generic.controller.AnyController;
import com.uproject.generic.domain.repository.AnyRepository;
import com.uproject.generic.domain.repository.ViewAnyDetailRepository;
import com.uproject.generic.domain.repository.entity.AnyEntity;
import com.uproject.generic.domain.repository.entity.view.ViewAnyDetail;
import com.uproject.generic.domain.utils.AnyDetailConstant;
import com.uproject.library.ums.domain.util.UTestAssistant;
import com.uproject.library.ums.domain.util.PagePosition;

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
public class GetIT {
	
	/** The rest template. */
	@Autowired
	private TestRestTemplate restTemplate;

	/** The assistant. */
	@Autowired
	private UTestAssistant<ViewAnyDetail> assistant;

	/** The any base repository. */
	@Autowired
	private AnyRepository baseRepository;
	
	/** The any repository. */
	@Autowired
	private ViewAnyDetailRepository repository;
	
	/** The path to controller. */
	private String pathToController = AnyDetailConstant.PATH_TO_CONTROLLER;
	
	/** The base entity 0. */
	private AnyEntity baseEntity0 = new AnyEntity(0);
	
	/** The base entity 1. */
	private AnyEntity baseEntity1 = new AnyEntity(1);
	
	/** The base entity 2. */
	private AnyEntity baseEntity2 = new AnyEntity(2);
	
	/** The base entity 3. */
	private AnyEntity baseEntity3 = new AnyEntity(3);
	
	/** The base entity 4. */
	private AnyEntity baseEntity4 = new AnyEntity(4);
	
	/** The base entity 5. */
	private AnyEntity baseEntity5 = new AnyEntity(5);
	
	/** The entity 0. */
	private ViewAnyDetail entity0 = new ViewAnyDetail(0,0);
	
	/** The entity 1. */
	private ViewAnyDetail entity1 = new ViewAnyDetail(1,0);
	
	/** The entity 2. */
	private ViewAnyDetail entity2 = new ViewAnyDetail(2,0);
	
	/** The entity 3. */
	private ViewAnyDetail entity3 = new ViewAnyDetail(3,0);
	
	/** The entity 4. */
	private ViewAnyDetail entity4 = new ViewAnyDetail(4,0);
	
	/** The entity 5. */
	private ViewAnyDetail entity5 = new ViewAnyDetail(5,0);
	
	/** The default metadata. */
	private PageMetadata defaultMetadata = new PageMetadata(20,0,6);
	
	/** The entities. */
	private List<ViewAnyDetail> entities = new ArrayList<>();
	
	/** The response. */
	private ResponseEntity<String> response;
	
	/** The expected. */
	private ResponseEntity<String> expected;
	
	/**
	 * Save entity test.
	 *
	 * @param entity The entity
	 * @param id The id
	 * @param activeInd The active ind
	 * @param field The field
	 */
	public void saveEntityTest(
			ViewAnyDetail entity,
			Integer id,
			String activeInd,
			String detailField) {
		entity.setActiveInd(activeInd);
		entity.setDetailField(detailField);
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
		repository.deleteAll();
		baseRepository.save(baseEntity0);
		baseRepository.save(baseEntity1);
		baseRepository.save(baseEntity2);
		baseRepository.save(baseEntity3);
		baseRepository.save(baseEntity4);
		baseRepository.save(baseEntity5);
		saveEntityTest(
				entity0, 0, "Y", "field");
		saveEntityTest(
				entity1, 1, "N", "field");
		saveEntityTest(
				entity2, 2, "Y", "field");
		saveEntityTest(
				entity3, 3, "N", "anotherField");
		saveEntityTest(
				entity4, 4, "Y", "anotherField");
		saveEntityTest(
				entity5, 5, null, "anotherField");
	}
	
	/**
	 * No content, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void NoContent() throws Exception {
		repository.deleteAllInBatch();
		response = assistant.get("");
		String data = null;
		expected = assistant.getResponse(data, HttpStatus.NO_CONTENT);
		compareResponse();
	}
	
	/**
	 * Pageable by default, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void PageableByDefault() throws Exception {
		response = assistant.get("");
		entities.add(entity0);
		entities.add(entity1);
		entities.add(entity2);
		entities.add(entity3);
		entities.add(entity4);
		entities.add(entity5);
		expected = assistant.getResponse(entities, defaultMetadata, null, null);
		compareResponse();
	}
	
	/**
	 * Pageable by page size and sort asc, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void PageableByPageSizeAndSortAsc() throws Exception {
		String sort = "anyId,asc";
		PageMetadata metadata = new PageMetadata(3,1,6);
		response = assistant.get(assistant.getPagination(metadata.getNumber(), metadata.getSize(), sort));
		entities.add(entity3);
		entities.add(entity4);
		entities.add(entity5);
		expected = assistant.getResponse(entities, metadata, PagePosition.PREV, sort);
		compareResponse();
	}
	
	/**
	 * Pageable by page size and sort asc 2, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void PageableByPageSizeAndSortAsc2() throws Exception {
		String sort = "anyId,asc";
		PageMetadata metadata = new PageMetadata(1,1,6);
		response = assistant.get(assistant.getPagination(metadata.getNumber(), metadata.getSize(), sort));
		entities.add(entity1);
		expected = assistant.getResponse(entities, metadata, PagePosition.BOTH, sort);
		compareResponse();
	}
	
	/**
	 * Pageable by page size and sort desc, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void PageableByPageSizeAndSortDesc() throws Exception {
		String sort = "anyId,desc";
		PageMetadata metadata = new PageMetadata(3,0,6);
		response = assistant.get(assistant.getPagination(metadata.getNumber(), metadata.getSize(), sort));
		entities.add(entity5);
		entities.add(entity4);
		entities.add(entity3);
		expected = assistant.getResponse(entities, metadata, PagePosition.NEXT, sort);
		compareResponse();
	}
	
	/**
	 * Page size negatives, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void PageSizeNegatives() throws Exception {
		response = assistant.get("?page=-1&size=-10");
		entities.add(entity0);
		entities.add(entity1);
		entities.add(entity2);
		entities.add(entity3);
		entities.add(entity4);
		entities.add(entity5);
		expected = assistant.getResponse(entities, defaultMetadata, null, null);
		compareResponse();
	}
	
	/**
	 * Page size string, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void PageSizeString() throws Exception {
		response = assistant.get("?page=aaa&size=bbb");
		entities.add(entity0);
		entities.add(entity1);
		entities.add(entity2);
		entities.add(entity3);
		entities.add(entity4);
		entities.add(entity5);
		expected = assistant.getResponse(entities, defaultMetadata, null, null);
		compareResponse();
	}
	
	/**
	 * Sort by string, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void SortByString() throws Exception {
		response = assistant.get("?sort=aaa");
		expected = assistant.getResponse(new Exception());
		compareResponse();
	}
	
	/**
	 * Sort by unkown field, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void SortByUnkownField() throws Exception {
		response = assistant.get("?sort=fiel,asc");
		expected = assistant.getResponse(new Exception());
		compareResponse();
	}
	
	/**
	 * Sort by unkown property, return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void SortByUnkownProperty() throws Exception {
		response = assistant.get("?sort=field,descddd");
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
