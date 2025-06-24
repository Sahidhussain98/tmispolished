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
public class M_Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sessionId;

    @Column(nullable = false)
    private String sessionName;

    @ManyToOne
    @JoinColumn(name = "topic_id", referencedColumnName = "topicId", nullable = false)
    private M_Topic topic;

    @Transient // This will receive the topicId from the request
    private Long topicId;
}
