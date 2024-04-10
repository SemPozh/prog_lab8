package laba6.server.modules;

import laba6.common.data.Organization;
import laba6.common.exeptions.InvalidObjectFieldException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class CollectionFileManager {
    private String envVariable;

    public CollectionFileManager(String envVariable) {
        this.envVariable = envVariable;
    }

    /**
     * Writes collection to a file.
     *
     * @param collection Collection to write.
     */
    public void writeCollection(Stack<Organization> collection) {
        if (System.getenv().get(envVariable) != null) {
            try (FileWriter collectionFileWriter = new FileWriter(new File(System.getenv().get(envVariable)))) {
                collectionFileWriter.write(CSVParser.toCSV(collection));
                ResponseOutputer.appendln("The collection was successfully saved to a file.");
            } catch (IOException exception) {
                ResponseOutputer.appenderror("The download file is a directory/cannot be opened!");
            }
        } else ResponseOutputer.appenderror("System variable with boot file not found!");
    }

    /**
     * Reads collection from a file.
     *
     * @return Readed collection.
     */
    public Stack<Organization> readCollection() {
        if (System.getenv().get(envVariable) != null) {
            try (Scanner collectionFileScanner = new Scanner(new File(System.getenv().get(envVariable)))) {
                Stack<Organization> collection = new Stack<>();
                while (collectionFileScanner.hasNextLine()){
                    collection.add(CSVParser.parseCSVString(collectionFileScanner.nextLine().trim()));
                }
                System.out.println("The collection has been successfully loaded.");
//                App.logger.info("Коллекция успешно загружена.");
                return collection;
            } catch (FileNotFoundException exception) {
                System.out.println("Boot file not found!");
//                App.logger.warn("Загрузочный файл не найден!");
            } catch (NoSuchElementException exception) {
                System.out.println("The download file is empty!");
//                App.logger.error("Загрузочный файл пуст!");
            } catch (InvalidObjectFieldException | NullPointerException exception) {
                System.out.println("An incorrect collection was detected in the loading file!");
//                App.logger.error("В загрузочном файле обнаружена некорректная коллекция!");
            } catch (IllegalStateException exception) {
                System.out.println("Unexpected error!");
//                App.logger.fatal("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        else System.out.println("System variable with boot file not found!");
        return new Stack<>();
    }
}
