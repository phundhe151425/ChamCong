package com.vmg.scrum.repository;

import com.vmg.scrum.model.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteLogRepository extends JpaRepository<Request, Long> {
}
