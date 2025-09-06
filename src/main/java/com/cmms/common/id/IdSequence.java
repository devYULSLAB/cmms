package com.cmms.common.id;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "id_sequence")
@IdClass(IdSequenceId.class)
public class IdSequence {

    @Id
    @Column(name = "company_id", length = 5, nullable = false)
    private String companyId;

    @Id
    @Column(name = "prefix", length = 1, nullable = false)
    private String prefix;

    @Column(name = "next_val", nullable = false)
    private Long nextVal;
}
