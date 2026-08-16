package com.ecommerce.user.service;

import com.ecommerce.user.model.DatabaseSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SequenceGenerator {

	private final MongoOperations mongoOperations;

	public long next(String sequenceName) {
		DatabaseSequence counter = mongoOperations.findAndModify(
				Query.query(Criteria.where("_id").is(sequenceName)),
				new Update().inc("seq", 1),
				FindAndModifyOptions.options().returnNew(true).upsert(true),
				DatabaseSequence.class);

		return counter != null ? counter.getSeq() : 1L;
	}
}
