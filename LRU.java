package org.unibl.etf.PagingAlgorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

public class LRU {
    private static class Tuple {
        private int page;
        private long time;

        Tuple(int page, long time) {
            this.page = page;
            this.time = time;
        }

        int getPage() {
            return page;
        }

        void setPage(int page) {
            this.page = page;
        }

        long getTime() {
            return time;
        }

        void setTime(long time) {
            this.time = time;
        }
    }

    private static ArrayList<Tuple> loadedPages = new ArrayList<>();
    private static int pfCounter = 0;

    private static void loadPages(String[] pages) {
        for (String page : pages) {
            boolean found = loadedPages.stream().anyMatch(p -> p.getPage() == Integer.valueOf(page));
            if (found) {
                loadedPages.forEach(c -> {
                    if (c.getPage() == Integer.valueOf(page))
                        c.setTime(System.nanoTime());
                });
                printPages(false);
            } else if (loadedPages.size() < 4) {
                ++pfCounter;
                loadedPages.add(new Tuple(Integer.valueOf(page), System.nanoTime()));
                printPages(true);
            } else {
                ++pfCounter;
                long lruPage = loadedPages
                        .stream()
                        .sorted(Comparator.comparingLong(Tuple::getTime))
                        .collect(Collectors.toList())
                        .get(0)
                        .getTime();
                loadedPages.forEach(c -> {
                    if (c.getTime() == lruPage) {
                        c.setPage(Integer.valueOf(page));
                        c.setTime(System.nanoTime());
                    }
                });
                printPages(true);
            }
        }
    }

    private static void printPages(boolean pfOccur) {
        for (Tuple tuple : loadedPages) System.out.format("|| %-3d", tuple.getPage());
        if (pfOccur)
            System.out.println("||  *");
        else
            System.out.println("||  /");
    }

    // 1,2,3,4,3,1,5,7,2,1,5,3,7,4,6,2,7,2,4,6
    // 1,4,6,3,4,6,4,2,1,4,5,6,4,5,6
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter pages (1,2,3,4,5,...) : ");
        String input = scanner.next();
        String[] pages = input.split(",");
        loadPages(pages);
        System.out.println("Total page faults: " + pfCounter);
    }
}
