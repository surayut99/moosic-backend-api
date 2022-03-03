package com.backend.service.utils;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class FutureUtils {
  private static FutureUtils futureUtils;

  private FutureUtils() {
  }

  public static FutureUtils getInstance() {
    if (futureUtils == null) {
      futureUtils = new FutureUtils();
    }

    return futureUtils;
  }

  public <T> List<T> solveAllFutures(List<CompletableFuture<T>> futures) {
    try {
      return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
          .thenApply(v -> futures
              .stream()
              .map(CompletableFuture::join)
              .collect(Collectors.toList()))
          .get();
    } catch (InterruptedException | ExecutionException e) {
      throw new MoosicException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
