package com.github.anjeyy.api.dao.repository;

import com.github.anjeyy.api.dao.entity.Document;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findAll();
    
}
