package com.uproject.generic.service.anydetail;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.uproject.generic.domain.repository.AnyDetailRepository;
import com.uproject.generic.domain.repository.entity.AnyEntityDetail;
import com.uproject.generic.service.AnyDetailService;
import com.uproject.generic.service.AnyService;
import com.uproject.library.ums.domain.exception.UException;
import com.uproject.library.ums.domain.response.USuccessResponse;
import com.uproject.library.ums.domain.util.ApiMethod;
import com.uproject.library.ums.service.UValidator;

// TODO: Auto-generated Javadoc
/**
 * Unit test for {@link AnyService#restore(String)}.
 * 
 * @author Wilever Gomez [wilevergomez@gmail.com]
 * 
 */
public class RestoreTest {

	/** The service. */
	@InjectMocks
	private AnyDetailService service;
	
	/** The repository. */
	@Mock
	private AnyDetailRepository repository;
	
	/** The validator. */
	@Mock
	private UValidator validator;
	
	/** The entity. */
	private AnyEntityDetail entity = new AnyEntityDetail(0,0);
	
	/** The method to test: {@link ApiMethod#RESTORE}. */
	private ApiMethod method = ApiMethod.RESTORE;
	
	/**
	 * Call mock annotations.
	 */
	@Before
	public void beforeEachTest() {
		MockitoAnnotations.initMocks(this);		
	}
	
	/**
	 * Success, {@link #method} return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test
	public void Success() throws Exception {
		when(repository.findOne(entity.getId())).
			thenReturn(entity);
		doNothing().when(validator).isValid(entity, method);
		assertThat(service.restore(entity.getId())).
			isEqualToComparingFieldByFieldRecursively(
				new ResponseEntity<>(
						new USuccessResponse<>(
								method, 
								entity.getId()),
						HttpStatus.OK));
	}
	
	/**
	 * PK not available, {@link #method} return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test(expected=UException.class)
	public void PKNotAvailable() throws Exception {
		when(repository.findOne(entity.getId())).
			thenReturn(null);
		service.restore(entity.getId());
	}
	
	/**
	 * Validate fail, {@link #method} return expected response.
	 *
	 * @throws Exception The exception
	 */
	@Test(expected=Exception.class)
	public void ValidateFailAndServerError() throws Exception {
		doThrow(new Exception()).when(validator).isValid(entity, method);
		service.restore(entity.getId());
	}
}
