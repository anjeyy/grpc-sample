package com.github.anjeyy.api.dao.entity;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "docs")
public class Document {

    @Id
    @Column(columnDefinition = "uuid", nullable = false, updatable = false)
    private UUID docid;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String person;

    @Column(nullable = false)
    private int filesize;
}
