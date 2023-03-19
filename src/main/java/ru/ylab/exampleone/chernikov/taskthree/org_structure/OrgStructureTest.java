package ru.ylab.exampleone.chernikov.taskthree.org_structure;

import java.io.File;
import java.io.IOException;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 19.03.2023
 */
public class OrgStructureTest {
    public static void main(String[] args) {
        File file = new File("src/main/resources/org_structure/list.csv");
        OrgStructureParser orgStructureParser = new OrgStructureParserImpl();
        try {
            System.out.println(orgStructureParser.parseStructure(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
