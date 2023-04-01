package ru.ylab.exampleone.chernikov.lesson05.eventsourcing.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApiApp {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        PersonApi personApi = applicationContext.getBean(PersonApiImpl.class);
        personApi.savePerson(1L, "Artem", "Chernikov", "Vladimirovich");
        personApi.savePerson(2L, "Andrey", "Ivanov", "Grigorievich");
        personApi.savePerson(3L, "Ivan", "Sarkov", "Vladislavovich");
        System.out.println(personApi.findPerson(1L));
        System.out.println(personApi.findAll());
        personApi.savePerson(2L, "Maxim", "Fedorov", "Andreevich");
        personApi.deletePerson(3L);
        System.out.println(personApi.findPerson(3L));
        System.out.println(personApi.findAll());
        applicationContext.close();
    }
}
