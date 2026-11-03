package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map;
import java.util.stream.Collectors;


@SpringBootApplication
public class MedicalBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedicalBookingApplication.class, args);
    }
}
//@EnableScheduling
//
//// 1. Указываем Spring, где искать ВСЕ репозитории
//@EnableJpaRepositories(basePackages = {
//        "com.example.repository",
//        "com.example.warehouseManagementSystem.repository"
//})
//// 2. Указываем Hibernate, где искать ВСЕ сущности (@Entity)
//@EntityScan(basePackages = {
//        "com.example.domain",
//        "com.example.warehouseManagementSystem.domain"
//})
//public class MedicalBookingApplication {
//    public static Map<String, Double> getTopCategoriesUSD(List<Transaction> transactions) {
////        return transactions.stream()
////                // 1. Фильтруем: только SUCCESS и только в валюте USD
////                .filter(t -> t.status() == Status.SUCCESS && "USD".equalsIgnoreCase(t.currency()))
////                // 2. Группируем по категориям и суммируем amount (приводя int к double)
////                .collect(Collectors.groupingBy(
////                        Transaction::category,
////                        Collectors.summingDouble(t -> (double) t.amount())
////                ))
////                // 3. Переводим в поток записей Map для сортировки
////                .entrySet().stream()
////                // 4. Сортируем по значениям (суммам) в обратном порядке (по убыванию)
////                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
////                // 5. Собираем в LinkedHashMap, чтобы сохранить порядок сортировки
////                .collect(Collectors.toMap(
////                        Map.Entry::getKey,
////                        Map.Entry::getValue,
////                        (oldValue, newValue) -> oldValue, // Слияние не потребуется, ключи уникальны
////                        LinkedHashMap::new
////                ));
//        // Шаг 1: Фильтруем и группируем с подсчетом суммы
//        Map<String, Double> unsortedMap = transactions.stream()
//                .filter(t -> t.status() == Status.SUCCESS)
//                .filter(t -> "USD".equalsIgnoreCase(t.currency()))
//                .collect(Collectors.groupingBy(
//                        Transaction::category,
//                        Collectors.summingDouble(Transaction::amount)
//                ));
//
//        // Шаг 2: Сортируем полученную Map по убыванию суммы
//        return unsortedMap.entrySet().stream()      // Set<Map.Entry<String, Double>>
//                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (oldValue, newValue) -> oldValue, // Лямбда на случай конфликта ключей (здесь не случится)
//                        LinkedHashMap::new                // Важно! Сохраняет порядок сортировки
//                ));
//    }
//
//
//    public static void main(String[] args) {
//
//        List<Transaction> transactions = List.of(
//                new Transaction("1", 12, "МОЛОКО", Status.SUCCESS, "USD"),
//                new Transaction("2", 15, "ХЛЕБ", Status.PENDING, "USD"),
//                new Transaction("3", 16, "ХЛЕБ", Status.SUCCESS, "USD"),
//                new Transaction("4", 10, "ГОРЯЧЕЕ", Status.SUCCESS, "RUB"), // Другая валюта
//                new Transaction("5", 19, "ГОРЯЧЕЕ", Status.FAILED, "USD"),
//                new Transaction("6", 13, "МОЛОКО", Status.FAILED, "USD"),
//                new Transaction("7", 11, "ХЛЕБ", Status.SUCCESS, "USD"),  // ХЛЕБ суммарно: 16 + 11 = 27
//                new Transaction("8", 20, "МОЛОКО", Status.PENDING, "USD"),
//                new Transaction("9", 23, "МОЛОКО", Status.SUCCESS, "USD"), // МОЛОКО суммарно: 12 + 23 = 35
//                new Transaction("10", 15, "ГОРЯЧЕЕ", Status.SUCCESS, "USD")  // ГОРЯЧЕЕ суммарно: 15
//        );
//
//
//        System.out.println(getTopCategoriesUSD(transactions));
//
//    }
//
//
//    public record Transaction(
//            String id,
//            double amount,
//            String category,
//            Status status,
//            String currency
//    ) {
//    }
//
//    public enum Status {SUCCESS, PENDING, FAILED}
//}

//public class MedicalBookingApplication {
//    public static void main(String[] args) {
//        List<Employee> employees = List.of(
//                new Employee("Ситора", true, List.of("Летчица", "стюардесса", "бортмеханик", "авиадипетчер")),
//                new Employee("Рухшона", false, List.of("Водитель", "курьер", "автодиспетчер", "автомеханик")),
//                new Employee("Мафтуна", true, List.of("Стоматолог", "анестезиолог", "медсестра")),
//                new Employee("Содик", true, List.of("Программист", "тестировщик", "devops-инженер")),
//                new Employee("Алиджон", true, List.of("Водитель", "тракдиспетчер")),
//                new Employee("Джамал", false, List.of("техподдержка"))
//        );
//        System.out.println(getActiveEmployeesSkills(employees));
//
//    }
//    public static List<String> getActiveEmployeesSkills(List<Employee> employees) {
//        return employees.stream()
//                .filter(i -> i.isActive)
//                .flatMap(u -> u.skills.stream())
//                .distinct()
//                .sorted()
//                .toList();
//    }
//
//    public record Employee(
//            String name,
//            boolean isActive,
//            List<String> skills
//    ) {}
//}
