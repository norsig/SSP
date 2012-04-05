package edu.sinclair.ssp.web.api.reference;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.sinclair.ssp.factory.TransferObjectListFactory;
import edu.sinclair.ssp.model.ObjectStatus;
import edu.sinclair.ssp.model.reference.AbstractReference;
import edu.sinclair.ssp.service.AuditableCrudService;
import edu.sinclair.ssp.transferobject.ServiceResponse;
import edu.sinclair.ssp.transferobject.reference.AbstractReferenceTO;
import edu.sinclair.ssp.web.api.RestController;
import edu.sinclair.ssp.web.api.validation.ValidationException;

@Controller
public abstract class AbstractAuditableReferenceController<T extends AbstractReference, TO extends AbstractReferenceTO<T>>
		extends RestController<TO> {

	protected static final Logger logger = LoggerFactory
			.getLogger(AbstractAuditableReferenceController.class);

	protected AuditableCrudService<T> service;

	private TransferObjectListFactory<TO, T> listFactory;

	protected Class<T> persistentClass;

	protected Class<TO> transferObjectClass;

	protected AbstractAuditableReferenceController(
			AuditableCrudService<T> service, Class<T> persistentClass,
			Class<TO> transferObjectClass) {
		this.service = service;
		this.persistentClass = persistentClass;
		this.transferObjectClass = transferObjectClass;
		this.listFactory = new TransferObjectListFactory<TO, T>(
				transferObjectClass);
	}

	/**
	 * Retrieve every instance in the database filtered by the supplied status.
	 * 
	 * @param status
	 *            Filter by this status.
	 * @param firstResult
	 *            First result (0-based index) to return. Parameter must be a
	 *            positive, non-zero integer. Often comes from client as a
	 *            parameter labeled <code>start</code>.
	 * @param maxResults
	 *            Maximum number of results to return. Parameter must be a
	 *            positive, non-zero integer. Often comes from client as a
	 *            parameter labeled <code>limit</code>.
	 * @param sortExpression
	 *            Property name and ascending/descending keyword. If null or
	 *            empty string, the default sort order will be used. Often comes
	 *            from client as a parameter labeled <code>sort</code>. Example
	 *            sort expression: <code>propertyName ASC</code>
	 * @exception Exception
	 *                Generic exception thrown if there were any errors.
	 * @return All entities in the database filtered by the supplied status.
	 */
	@Override
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public @ResponseBody
	List<TO> getAll(@RequestParam(required = false) ObjectStatus status,
			@RequestParam(required = false) int start,
			@RequestParam(required = false) int limit,
			@RequestParam(required = false) String sort) throws Exception {
		if (status == null) {
			status = ObjectStatus.ACTIVE;
		}

		return listFactory.toTOList(service.getAll(status, start, limit, sort));
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	TO get(@PathVariable UUID id) throws Exception {
		T model = service.get(id);
		if (model != null) {
			TO out = this.transferObjectClass.newInstance();
			out.fromModel(model);
			return out;
		} else {
			return null;
		}
	}

	@Override
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public @ResponseBody
	TO create(@Valid @RequestBody TO obj) throws Exception {
		if (obj.getId() != null) {
			throw new ValidationException(
					"It is invalid to send a reference entity with an ID to the create method. Did you mean to use the save method instead?");
		}

		T model = obj.asModel();

		if (null != model) {
			T createdModel = service.create(model);
			if (null != createdModel) {
				TO out = this.transferObjectClass.newInstance();
				out.fromModel(createdModel);
				return out;
			}
		}
		return null;
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public @ResponseBody
	TO save(@PathVariable UUID id, @Valid @RequestBody TO obj) throws Exception {
		if (id == null) {
			throw new ValidationException(
					"You submitted a citizenship without an id to the save method.  Did you mean to create?");
		}

		T model = obj.asModel();
		model.setId(id);

		T savedT = service.save(model);
		if (null != savedT) {
			TO out = this.transferObjectClass.newInstance();
			out.fromModel(savedT);
			return out;
		}
		return null;
	}

	@Override
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	ServiceResponse delete(@PathVariable UUID id) throws Exception {
		service.delete(id);
		return new ServiceResponse(true);
	}

	@Override
	@ExceptionHandler(Exception.class)
	public @ResponseBody
	ServiceResponse handle(Exception e) {
		logger.error("Error: ", e);
		return new ServiceResponse(false, e.getMessage());
	}
}
