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
@Getter
@Setter
@Entity
public class M_Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;

    @Column(nullable = false)
    private String topicName;

    @Column(nullable = false)
    private String topicDescription;

    @Column(nullable = false)
    private String fmrCode;

    @ManyToOne
    @JoinColumn(name = "program_id", referencedColumnName = "programId", nullable = false)
    private M_Program program;

    @Transient // This will be used in the request but not stored in the DB
    private Long programId;

}
