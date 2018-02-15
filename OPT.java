package org.unibl.etf.PagingAlgorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeSet;

public class OPT {
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
    private static int pfCounter;

    private static void loadPages(String[] pages) {
        int currPos = 0;
        for (String page : pages) {
            boolean found = loadedPages.stream().anyMatch(p -> p.getPage() == Integer.valueOf(page));
            if (found)
                printPages(false);
            else if (loadedPages.size() < 3) {
                ++pfCounter;
                loadedPages.add(new Tuple(Integer.valueOf(page), System.nanoTime()));
                printPages(true);
            } else {
                ++pfCounter;
                // tmp sadrzi ucitane stranice i pomjeraj za koliiko ce one doci na red
                // tmp je sortiran po vremenu dolaska u opadajucem redoslijedu
                // Ona stranica koja najkasnije dolazi ce biti na pocetku
                TreeSet<Tuple> tmp = new TreeSet<>(Comparator.comparingLong(Tuple::getTime).reversed());
                long howFarAway;
                for (Tuple tuple : loadedPages) {
                    howFarAway = 0;
                    for (int i = currPos; i < pages.length; ++i) {
                        int currPage = Integer.valueOf(pages[i]);
                        boolean inSet = tmp.stream().anyMatch(p -> p.getPage() == currPage);
                        if (!inSet && tuple.getPage() == currPage) {
                            tmp.add(new Tuple(tuple.getPage(), howFarAway));
                            break;
                        } else
                            ++howFarAway;
                    }
                    // Ukoliko se ucitana stranica vise ne pojavljuje, dodijeli joj najduze vrijeme
                    if (tmp.stream().noneMatch(p -> p.getPage() == tuple.getPage()))
                        tmp.add(new Tuple(tuple.getPage(), Integer.MAX_VALUE));
                }
                // Novu stranica se mijenja sa onom stranicom koja ce najkasnije doci na red
                loadedPages.forEach(c -> {
                    if (c.getPage() == tmp.first().getPage()) {
                        c.setPage(Integer.valueOf(page));
                        c.setTime(System.nanoTime());
                    }
                });
                printPages(true);
            }
            ++currPos;
        }
    }

    private static void printPages(boolean pfOccur) {
        for (Tuple tuple : loadedPages) System.out.format("|| %-3d", tuple.getPage());
        if (pfOccur)
            System.out.println("||  *");
        else
            System.out.println("||  /");
    }

    // 7,0,1,2,0,3,0,4,2,3,0,3,2,1,2,0,1,7,0,1 ( ulazna sekvenca za 3 okvira - primjer sa prezentacije )
    // 1,4,6,3,4,6,4,2,1,4,5,6,4,5,6
    // 1,2,3,4,3,1,5,7,2,1,5,3,7,4,6,2,7
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
