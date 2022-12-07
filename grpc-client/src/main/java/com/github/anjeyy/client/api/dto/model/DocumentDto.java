package com.github.anjeyy.client.api.dto.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DocumentDto {

    String docid;
    String title;
    String person;
    int filesize;
}
