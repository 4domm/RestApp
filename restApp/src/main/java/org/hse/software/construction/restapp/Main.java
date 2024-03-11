package org.hse.software.construction.restapp;

import org.hse.software.construction.restapp.util.ApplicationInitializer;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Ошибка: Не указаны все файлы. Пожалуйста, укажите пути к файлам dishes.json, order.json и user.json.");
            return;
        }
        File file1 = new File(args[0]);
        File file2 = new File(args[1]);
        File file3 = new File(args[2]);
        ApplicationInitializer.startApplication(file1, file2, file3);
    }
}