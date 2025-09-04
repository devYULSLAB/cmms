package com.yulslab.domain.sequence;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "id_sequence")
@Getter
@Setter
public class IdSequence {
    @EmbeddedId
    private IdSequenceId id;
    private long nextVal;
}
