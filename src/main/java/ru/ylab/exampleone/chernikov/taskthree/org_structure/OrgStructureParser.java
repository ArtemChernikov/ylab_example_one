package ru.ylab.exampleone.chernikov.taskthree.org_structure;

import java.io.File;
import java.io.IOException;

/**
 * @author Artem Chernikov
 * @version 1.0
 * @since 18.03.2023
 */
public interface OrgStructureParser {
    public Employee parseStructure(File csvFile) throws IOException;
}
