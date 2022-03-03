package com.backend.service.models.responses.searchs;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResponse {
  private int page;
  private int totalPage;
  private int count;
  private List<?> contents;
}
