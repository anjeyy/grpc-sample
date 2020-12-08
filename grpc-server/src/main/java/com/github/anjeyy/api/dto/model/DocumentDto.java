package com.github.anjeyy.api.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentDto {

    UUID docid;
    String title;
    String person;
    int filesize;

}
