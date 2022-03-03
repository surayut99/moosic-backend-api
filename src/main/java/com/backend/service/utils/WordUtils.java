package com.backend.service.utils;

import java.util.ArrayList;
import java.util.List;

public class WordUtils {
  public static List<String> createCombinationWord(List<String> mainWords, String... headWords) {
    List<String> combination = new ArrayList<>();

    for (String m : mainWords) {
      for (String h : headWords) {
        if (h.equals("")) continue;

        combination.add(String.format("%s %s", h, m));
      }
    }

    return combination;
  }
}
