package com.github.anjeyy.api.dto.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DocumentDto {

    UUID docid;
    String title;
    String person;
    int filesize;
}
