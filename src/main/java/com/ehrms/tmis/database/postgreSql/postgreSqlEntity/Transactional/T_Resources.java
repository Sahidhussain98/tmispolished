package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class T_Resources {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long resourcePersonId;

  @Column(name = "EmpCd", nullable = false)
  private String empCd;

  
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "resource_file", nullable = false)
  private byte[] resource;

  @Column(name = "file_name", nullable = false)
  private String fileName;

  @Column(name = "file_type", nullable = false)
  private String fileType;
}
