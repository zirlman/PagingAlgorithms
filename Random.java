package org.unibl.etf.PagingAlgorithms;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Predicate;

public class Random {
    private static ArrayList<Integer> loadedPages = new ArrayList<>();
    private static int pfCounter;

    private static void loadPages(String[] pages) {
        java.util.Random random = new java.util.Random();
        for (String page : pages) {
            if (loadedPages.stream().anyMatch(Predicate.isEqual(Integer.valueOf(page))))
                printPages(false);
            else if (loadedPages.size() < 4) {
                ++pfCounter;
                loadedPages.add(Integer.valueOf(page));
                printPages(true);
            } else {
                ++pfCounter;
                loadedPages.set(random.nextInt(4), Integer.valueOf(page));
                printPages(true);
            }
        }
    }

    private static void printPages(boolean pgOccur) {
        for (Integer page : loadedPages) System.out.format("|| %-3d", page);
        if (pgOccur)
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
