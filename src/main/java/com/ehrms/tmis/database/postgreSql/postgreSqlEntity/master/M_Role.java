package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "m_role")
public class M_Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long roleId;

	@Column(length = 5)
	private String roleCode;

	@Column(length = 100)
	@NonNull
	private String roleName;

	@Column(name = "roleDescription")
	private String roleDescription;

	@Column(length = 150, nullable = false)
	private String landingPage;

	/** New: folder under /Users/<templatePackage> */
	@Column(length = 50, nullable = false)
	private String templatePackage;

	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonIgnoreProperties({ "roles" })
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Set<M_Process> processes = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		M_Role m_role = (M_Role) o;
		return Objects.equals(roleId, m_role.roleId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(roleId);
	}
}
