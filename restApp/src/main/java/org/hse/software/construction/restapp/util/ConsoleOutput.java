package org.hse.software.construction.restapp.util;

public class ConsoleOutput {
    static public void showAdminMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1. Добавить блюдо в меню");
        System.out.println("2. Удалить блюдо из меню");
        System.out.println("3. Изменить количество порций блюда");
        System.out.println("4. Изменить время приготовления блюда");
        System.out.println("5. Изменить цену блюда");
        System.out.println("6. Посмотреть выручку");
        System.out.println("7. Выйти из приложения");
        System.out.println("8. Выйти из аккаунта");
        System.out.println("9. Посмотреть отзывы");
    }

    static public void showLoginMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1. Войти");
        System.out.println("2. Зарегистрироваться");
        System.out.println("3. Завершить работу");
    }

    static public void showVisitorMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1. Посмотреть меню");
        System.out.println("2. Добавить блюдо в заказ");
        System.out.println("3. Отменить заказ");
        System.out.println("4. Проверить статус заказа");
        System.out.println("5. Выйти из из аккаунта");
        System.out.println("6. Выйти из приложения");
        System.out.println("7. Создать заказ");
        System.out.println("8. Оплатить заказ");
        System.out.println("9. Оставить отзыв");
    }
}
