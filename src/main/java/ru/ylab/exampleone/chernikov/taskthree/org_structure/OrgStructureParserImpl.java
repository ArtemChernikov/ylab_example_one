package ru.ylab.exampleone.chernikov.taskthree.org_structure;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 18.03.2023
 */
public class OrgStructureParserImpl implements OrgStructureParser {
    /**
     * Поле первый столбец csv файла (id)
     */
    private static final int ID = 0;
    /**
     * Поле второй столбец csv файла (bossId)
     */
    private static final int BOSS_ID = 1;
    /**
     * Поле третий столбец csv файла (name)
     */
    private static final int NAME = 2;
    /**
     * Поле четвертый столбец csv файла (position)
     */
    private static final int POSITION = 3;
    /**
     * Поле разделитель данных в csv файле
     */
    private static final String DELIMITER = ";";

    @Override
    public Employee parseStructure(File csvFile) throws IOException {
        List<Employee> employees = readCsvFile(csvFile);
        Employee boss = null;
        for (Employee employee : employees) {
            if (employee.getBossId() == null) {
                boss = employee;
            }
            setBoss(employee, employees);
            setSubordinates(employee, employees);
        }
        return boss;
    }

    /**
     * Метод используется для поиска и присвоения подчиненных сотруднику
     *
     * @param employee  - сотрудник для установки значений
     * @param employees - список всех сотрудников
     */
    private void setSubordinates(Employee employee, List<Employee> employees) {
        Long employeeId = employee.getId();
        if (employee.getSubordinate().isEmpty()) {
            for (Employee emp : employees) {
                if (employeeId.equals(emp.getBossId())) {
                    employee.getSubordinate().add(emp);
                }
            }
        }
    }

    /**
     * Метод используется для поиска и присвоения босса сотруднику
     *
     * @param employee  - сотрудник для установки значения
     * @param employees - список всех сотрудников
     */
    private void setBoss(Employee employee, List<Employee> employees) {
        Long bossId = employee.getBossId();
        if (bossId != null && employee.getBoss() == null) {
            employee.setBoss(employees.stream().filter(emp -> emp.getId().equals(bossId)).findAny().orElse(null));
        }
    }

    /**
     * Метод используется для чтения данных из csv файла
     *
     * @param csvFile - файл
     * @return - возвращает список сотрудников
     * @throws IOException - может выбросить исключение {@link IOException}
     */
    private List<Employee> readCsvFile(File csvFile) throws IOException {
        List<Employee> employees = new ArrayList<>();
        String dataOfEmployee;
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            reader.readLine();
            while ((dataOfEmployee = reader.readLine()) != null) {
                employees.add(createBaseEmployee(dataOfEmployee));
            }
        }
        return employees;
    }

    /**
     * Метод используется для создания базовой модели сотрудника
     * (без полей с подчиненными и боссом)
     *
     * @param dataOfEmployee - строка с данными сотрудника
     * @return - возвращает базовую модель сотрудника
     */
    private Employee createBaseEmployee(String dataOfEmployee) {
        Employee employee = new Employee();
        try (Scanner scanner = new Scanner(dataOfEmployee).useDelimiter(DELIMITER)) {
            int index = 0;
            while (scanner.hasNext()) {
                String data = scanner.next();
                switch (index) {
                    case ID:
                        employee.setId(Long.parseLong(data));
                        break;
                    case BOSS_ID:
                        if (!data.isEmpty()) {
                            employee.setBossId(Long.parseLong(data));
                        }
                        break;
                    case NAME:
                        employee.setName(data);
                        break;
                    case POSITION:
                        employee.setPosition(data);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + index);
                }
                index++;
            }
        }
        return employee;
    }
}
