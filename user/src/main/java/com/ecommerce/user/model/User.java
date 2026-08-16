package com.ecommerce.user.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Persistable<Long> {

	@Id
	private Long id;

	private String firstName;
	private String lastName;

	@Indexed(unique = true)
	private String email;

	private String password;
	private String phone;

	@Builder.Default
	private boolean active = true;

	@Builder.Default
	private Role role = Role.CUSTOMER;

	@CreatedDate
	private Instant createdAt;

	@LastModifiedDate
	private Instant updatedAt;

	@Builder.Default
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private List<Address> addresses = new ArrayList<>();

	@Transient
	@Builder.Default
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private boolean newEntity = true;

	@Override
	public boolean isNew() {
		return newEntity;
	}
}
