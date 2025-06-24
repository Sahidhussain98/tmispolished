package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "m_menu")
public class M_Menu {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long menuID;

	@Column(length = 5, nullable = false)
	private String menuCode;

	@Column(length = 50)
	private String menuName;
}
