package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "M_Program")
public class M_Program {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long programId;

	private String programName;

	private String fmrcode;

}
