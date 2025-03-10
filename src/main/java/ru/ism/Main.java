package ru.ism;

import ru.ism.module.Operation;
import ru.ism.module.Role;
import ru.ism.module.Transaction;
import ru.ism.module.User;
import ru.ism.repositiry.TransactionalRepoImpl;
import ru.ism.repositiry.TransactionalRepository;
import ru.ism.repositiry.UserRepository;
import ru.ism.repositiry.UserRepositoryImpl;
import ru.ism.service.TransactionService;
import ru.ism.service.TransactionalServiceImpl;
import ru.ism.service.UserService;
import ru.ism.service.UserServiceImpl;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        UserRepository userRepository = new UserRepositoryImpl();
        TransactionalRepository transactionalRepository = new TransactionalRepoImpl();
        UserService userService = new UserServiceImpl(userRepository);
        TransactionService transactionService = new TransactionalServiceImpl(transactionalRepository, userService);
        boolean exit = false;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Добрый день");
        while (!exit) {
            Optional<User> user;
            String email;
            String name;
            String password;
            do {
                System.out.println("Вы зарегистрированы? нет-1 / да-иной символ");
                if (scanner.next().equals("1")) {
                    System.out.println("Введите данные для регистрации");
                    System.out.println("введите email");
                    email = scanner.next();
                    System.out.println("введите имя");
                    name = scanner.next();
                    System.out.println("введите пароль");
                    password = scanner.next();
                    user = userService.addUser(email, name, password);
                } else {
                    System.out.println("Введите данные для входа в систему");
                    System.out.println("введите email");
                    email = scanner.next();
                    System.out.println("введите пароль");
                    password = scanner.next();
                    user = userService.registration(email, password);
                }
            } while (user.isEmpty());
            label:
            while (true) {
                System.out.println("""
                        -----\s
                        редактировать данные пользователя - 1\s
                        покинуть профиль - 2
                        меню администратора - 3\s
                        работать с транзакциями - иное""");
                String menu1 = scanner.next();
                switch (menu1) {
                    case "2":
                        break label;
                    case "3":
                        menuAdmin(scanner, user.get().getId(), transactionService, userService);
                        break;
                    case "1":
                        System.out.println("Удалить профиль 1, редактировать профиль - иное");
                        String menu2 = scanner.next();
                        if (menu2.equals("1")) {
                            userService.deleteUser(user.get().getId());
                            break label;
                        } else {
                            Optional<User> updateUser;
                            do {
                                System.out.println("введите email");
                                email = scanner.next();
                                System.out.println("введите имя");
                                name = scanner.next();
                                System.out.println("введите пароль");
                                password = scanner.next();
                                updateUser = userService.updateUser(user.get().getId(), email, name, password);
                            } while (updateUser.isEmpty());
                            user = updateUser;
                        }
                        break;
                    default:
                        boolean menu3 = true;
                        do {
                            printTransactionMenu();
                            menu2 = scanner.next();
                            switch (menu2) {
                                case "1":
                                    transactionService.addTransaction(user.get().getId(), menuCreateTransaction(scanner));
                                    break;
                                case "2":
                                    transactionService.updateTransaction(user.get().getId(), inputInt(scanner, "Id транзакции"),
                                            menuCreateTransaction(scanner));
                                    break;
                                case "3":
                                    transactionService.removeTransaction(user.get().getId(), inputInt(scanner, "id транзакции"));
                                    break;
                                case "4":
                                    getTransactionWithFiltration(scanner, user.get().getId(), transactionService);
                                    break;
                                case "5":
                                    userService.setMonthLimit(user.get().getId(),
                                            inputInt(scanner, "Введите лимит расходов за месяц"));
                                    break;
                                case "6":
                                    System.out.println(userService.getBalance(user.get().getId()));
                                    break;
                                case "7":
                                    System.out.println("Текущий баланс - " + userService.getBalance(user.get().getId()));
                                    System.out.println("Доходы за текущий месяц - "
                                            + transactionService.getCurrentMonthDebutCount(user.get().getId()));
                                    System.out.println("Расходы за текущий месяц - "
                                            + transactionService.getCurrentMonthCreditCount(user.get().getId()));
                                    break;
                                default:
                                    menu3 = false;
                            }
                        } while (menu3);
                        break;
                }
            }
        }
    }

    static void printTransactionMenu() {
        System.out.print("""
                ---------
                Создать транзакцию - 1\s
                редактировать транзакцию - 2\s
                удалить транзакцию - 3\s
                список транзакций с фильтрацией - 4\s
                установить лимит - 5\s
                вывести текущий баланс - 6\s
                получение статистики за месяц - 7\s
                выход в предыдущие меню - иное\s
                """);
    }

    static Transaction menuCreateTransaction(Scanner scanner) {
        System.out.println("Тип транзакции дебит 1 / кредит - иное");
        Operation operation = scanner.next().equals("1") ? Operation.DEBIT : Operation.CREDIT;
        String sum;
        do {
            System.out.println("Сумма транзакции");
            sum = scanner.next();
        } while (!isInt(sum));
        int amount = Integer.parseInt(sum);
        System.out.println("Категория:");
        String category = scanner.next();
        System.out.println("Описание:");
        String description = scanner.next();
        String date;
        do {
            System.out.println("Дата транзакции (пример: 2025-04-22):");
            date = scanner.next();
        } while (!isDate(date));
        LocalDate localDate = LocalDate.parse(date);
        return new Transaction(operation, amount, category, description, localDate);
    }

    static void menuAdmin(Scanner scanner, int userId, TransactionService transactionService, UserService userService) {
        if (userService.getUser(userId).get().getRole() != Role.ADMIN) {
            System.out.println("Это меню для администратора");
            return;
        }
        boolean menu3 = true;
        String string;
        do {
            System.out.println("---------\nСписок всех пользователей - 1\n" +
                    "Список транзакций пользователя - 2\nУдалить пользователя - 3\n" +
                    "Покинуть меню - иной символ");
            string = scanner.next();
            switch (string) {
                case "1":
                    userService.getUsers().forEach(System.out::println);
                    break;
                case "2":
                    int id = inputInt(scanner, "id пользователя");
                    transactionService.getTransactions(id).forEach(System.out::println);
                    break;
                case "3":
                    int deleteId = inputInt(scanner, "id пользователя для удаления");
                    if (deleteId == userId) {
                        System.out.println("Себя в этом меню удалить нельзя");
                        break;
                    }
                    userService.deleteUser(deleteId);
                    break;
                    default:
                        menu3 = false;
            }
        } while (menu3);
    }

    static void getTransactionWithFiltration(Scanner scanner, int userId, TransactionService service) {
        System.out.println("Ведите данные для фильтрации: \n доход - 1, расход 2, любая операция - иное");
        String str = scanner.next();
        Operation operation;
        if (str.equals("1")) {
            operation = Operation.DEBIT;
        } else if (str.equals("2")) {
            operation = Operation.CREDIT;
        } else {
            operation = null;
        }
        System.out.println("Введите название категории или 'all' для вывода всех категорий");
        String category;
        str = scanner.next();
        if (str.equals("all")) {
            category = null;
        } else {
            category = str;
        }
        LocalDate starDate;
        do {
            System.out.println("Введите доту начала периода (например 2025-10-10) или 'any'");
            str = scanner.next();
        } while (!isDate(str) && !"any".equals(str));
        if (str.equals("any")) {
            starDate = null;
        } else {
            starDate = LocalDate.parse(str);
        }
        LocalDate finishDate;
        do {
            System.out.println("Введите доту конца периода (например 2025-10-10) или 'any'");
            str = scanner.next();
        } while (!isDate(str) && !"any".equals(str));
        if (str.equals("any")) {
            finishDate = null;
        } else {
            finishDate = LocalDate.parse(str);
        }
        service.getTransactionsByUser(userId, operation, category, starDate, finishDate).forEach(System.out::println);
    }

    static int inputInt(Scanner scanner, String string) {
        String id;
        do {
            System.out.println("Введите " + string);
            id = scanner.next();
        } while (!isInt(id));
        return Integer.parseInt(id);
    }

    static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    static boolean isDate(String str) {
        try {
            LocalDate.parse(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}