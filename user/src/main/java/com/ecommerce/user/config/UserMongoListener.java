package com.ecommerce.user.config;

import com.ecommerce.user.model.User;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class UserMongoListener extends AbstractMongoEventListener<User> {

	@Override
	public void onAfterConvert(AfterConvertEvent<User> event) {
		event.getSource().setNewEntity(false);
	}
}
