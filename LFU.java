package org.unibl.etf.PagingAlgorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class LFU {
    private static class Tuple {
        private int page;
        private int hit;

        public Tuple(int page, int hit) {
            this.page = page;
            this.hit = hit;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getHit() {
            return hit;
        }

        public void setHit(int hit) {
            this.hit = hit;
        }

        public void incrementHit() {
            ++hit;
        }
    }

    private static ArrayList<Tuple> loadedPages = new ArrayList<>();
    private static int pfCounter;

    private static void loadPages(String[] pages) {
        ArrayList<Tuple> passedPages = new ArrayList<>();
        for (String page : pages) {
            boolean found = loadedPages.stream().anyMatch(p -> p.getPage() == Integer.valueOf(page));
            if (found)
                printPages(false);
            else if (loadedPages.size() < 4) {
                ++pfCounter;
                loadedPages.add(new Tuple(Integer.valueOf(page), 0));
                printPages(true);
            } else {
                ++pfCounter;
                for (Tuple tuple : passedPages) {
                    // passedPages je sortiran po rastucem redoslijedu u odnosu na broj pogodaka
                    if (loadedPages.stream().anyMatch(p -> p.getPage() == tuple.getPage())) {
                        // Trazi LFU stranicu u prostoru ucitanih stranica i mijenja je sa novom
                        loadedPages.forEach(c -> {
                            if (c.getPage() == tuple.getPage())
                                c.setPage(Integer.valueOf(page));
                        });
                        break;
                    }
                }
                printPages(true);
            }
            // Azuriranje frekvencije pogotka izbacenih stranica
            if (passedPages.stream().noneMatch(p -> p.getPage() == Integer.valueOf(page)))
                passedPages.add(new Tuple(Integer.valueOf(page), 0));
            else
                passedPages.forEach(c -> {
                    if (c.getPage() == Integer.valueOf(page))
                        c.incrementHit();
                });
            passedPages.sort(Comparator.comparingInt(Tuple::getHit).thenComparing(Tuple::getPage));
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
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter pages (1,2,3,4,5,...) : ");
        String input = scanner.next();
        String[] pages = input.split(",");
        loadPages(pages);
        System.out.println("Total page faults: " + pfCounter);
    }
}
