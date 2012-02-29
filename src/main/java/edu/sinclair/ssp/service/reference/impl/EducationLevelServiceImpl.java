package edu.sinclair.ssp.service.reference.impl;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import edu.sinclair.ssp.model.reference.EducationLevel;
import edu.sinclair.ssp.service.reference.EducationLevelService;
import edu.sinclair.ssp.service.reference.ReferenceService;

@Service
public class EducationLevelServiceImpl implements ReferenceService<EducationLevel>, EducationLevelService {

	private static final Logger logger = LoggerFactory.getLogger(EducationLevelServiceImpl.class);

	@Override
	public List<EducationLevel> getAll() {
		List<EducationLevel> all = Lists.newArrayList();
		
		all.add(new EducationLevel(UUID.randomUUID(), "High School Graduate"));
		all.add(new EducationLevel(UUID.randomUUID(), "College Graduate - 2 year"));
		all.add(new EducationLevel(UUID.randomUUID(), "College Graduate - 4 year"));
		
		return all;
	}

	@Override
	public EducationLevel get(UUID id) {
		return new EducationLevel(id, "High School Graduate");
	}

	@Override
	public EducationLevel save(EducationLevel obj) {
		return obj;
	}

	@Override
	public void delete(UUID id) {
		logger.debug("deleting {}", id.toString());
	}

}
