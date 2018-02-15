package org.unibl.etf.PagingAlgorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeSet;

public class FIFO {
    private static class Tuple {
        private int page;
        private long time;

        public Tuple(int page, long time) {
            this.page = page;
            this.time = time;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    private static ArrayList<Tuple> loadedPages = new ArrayList<>();
    private static int pfCounter;

    private static void loadPages(String[] pages) {
        for (String page : pages)
            if (loadedPages.stream().anyMatch(p -> p.getPage() == Integer.valueOf(page)))
                printPages(false);
            else if (loadedPages.size() < 4) {
                ++pfCounter;
                loadedPages.add(new Tuple(Integer.valueOf(page), System.nanoTime()));
                printPages(true);
            } else {
                ++pfCounter;
                TreeSet<Tuple> tmp = new TreeSet<>(Comparator.comparingLong(Tuple::getTime));
                tmp.addAll(loadedPages);
                loadedPages.forEach(c -> {
                    if (c.getPage() == tmp.first().getPage()) {
                        c.setPage(Integer.valueOf(page));
                        c.setTime(System.nanoTime());
                    }
                });
                printPages(true);
            }
    }

    private static void printPages(boolean pfOccur) {
        for (Tuple tuple : loadedPages) System.out.format("|| %-3d", tuple.getPage());
        if (pfOccur)
            System.out.println("||  *");
        else
            System.out.println("||  /");
    }

    // 1,2,3,4,1,2,5,6,7,8,7,8,1,2,3,4
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter pages (1,2,3,4,5,...) : ");
        String input = scanner.next();
        String[] pages = input.split(",");
        loadPages(pages);
        System.out.println("Total page faults: " + pfCounter);
    }
}
