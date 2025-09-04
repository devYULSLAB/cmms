package com.yulslab.domain.sequence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface IdSequenceRepository extends JpaRepository<IdSequence, IdSequenceId> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<IdSequence> findById(IdSequenceId id);
}
