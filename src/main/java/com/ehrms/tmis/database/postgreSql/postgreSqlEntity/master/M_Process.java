package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "m_process")
public class M_Process {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long processId;

	@Column(length = 3)
	private String processCode;

	@Column(length = 50)
	@NonNull
	private String processName;

	@Column(length = 100)
	// @NonNull
	private String pageURL;

	@ManyToOne
	@JoinColumn(name = "menuID", nullable = true)
	private M_Menu menu;

	@Transient
	private Long menuID; // Temporary field to capture `menuID` from the request

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JsonIgnoreProperties({ "processes" })
	@JoinTable(name = "m_process_role", // Join table name
			joinColumns = @JoinColumn(name = "process_id"), // Foreign key column for M_Process
			inverseJoinColumns = @JoinColumn(name = "role_id") // Foreign key column for M_Role
	)
	// @JsonIgnore
	private Set<M_Role> roles = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		M_Process m_process = (M_Process) o;
		return Objects.equals(processId, m_process.processId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(processId);
	}
}