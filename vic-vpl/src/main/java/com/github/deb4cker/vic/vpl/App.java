package com.github.deb4cker.vic.vpl;

import com.github.deb4cker.vic.vpl.cli.ApplicationPipeline;
import com.github.deb4cker.vic.commons.Printer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.github.deb4cker.vic.vpl.cli.commons.constants.FileExtension.JAVA;
import static com.github.deb4cker.vic.vpl.cli.commons.constants.FileExtension.UXF;

public class App
{
    private static final String LOCAL_FOLDER = "./";
    private static final String LOCAL_DEVELOPMENT_FOLDER = "local-dev/";

    public static void main(String[] args){
        boolean hasArgs = args.length > 0 && args[0] != null;

        boolean isDevelopment = hasArgs && args[0].equals("-d");
        if (isDevelopment) {
            runDevelopment(args);
            return;
        }

        evaluateImplementation(LOCAL_FOLDER, hasArgs? args[0] : null);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void evaluateImplementation(String folder, String uxfName){
        File uxfFile = checkForUxfFile(folder, uxfName);
        List<File> submittedJavaFiles = checkForJavaFiles(folder);
        String result = new ApplicationPipeline(uxfFile, submittedJavaFiles).run();
        Printer.printDirectlyToConsole(result);
    }


    private static File checkForUxfFile(String folder, String uxfName) {
        List<Predicate<File>> predicates = new ArrayList<>();
        predicates.add(file -> file.getName().endsWith(UXF));

        if (uxfName != null){
            String filename = uxfName.replace(UXF, "");
            predicates.add(file -> file.getName().replace(UXF, "").equals(filename));
        }

        List<File> submittedUxfFiles = getFilesByFolderPath(predicates, folder);
        if (submittedUxfFiles.isEmpty()) return null;
        return submittedUxfFiles.get(0);
    }

    private static List<File> checkForJavaFiles(String folder) {
        List<File> javaFiles = getFilesByFolderPath(List.of(file -> file.getName().endsWith(JAVA)), folder);
        if (javaFiles.isEmpty()) return List.of();
        return javaFiles;
    }

    private static List<File> getFilesByFolderPath(List<Predicate<File>> filters, String folder) {
        try (Stream<Path> paths = Files.list(Paths.get(folder))) {
            return paths
                    .map(Path::toFile)
                    .filter(file -> filters.stream().allMatch(filter -> filter.test(file)))
                    .toList();
        } catch (IOException e) {
            System.out.println("Nenhum arquivo encontrado em " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static void runDevelopment(String[] args) {
        Path devRoot = Paths.get(LOCAL_DEVELOPMENT_FOLDER);

        if (!Files.exists(devRoot) || !Files.isDirectory(devRoot)) {
            System.out.println("A pasta '" + LOCAL_DEVELOPMENT_FOLDER + "' não existe.");
            return;
        }

        if (args.length > 1) {
            String folderName = args[1];
            String uxfName = null;
            if (args.length > 2) {
                uxfName = args[2];
            }

            Path specificFolder = devRoot.resolve(folderName);
            if (!Files.exists(specificFolder) || !Files.isDirectory(specificFolder)) {
                System.out.println("A pasta especificada '" + specificFolder + "' não existe.");
                return;
            }

            System.out.println("Rodando localmente em: " + specificFolder);
            evaluateImplementation(specificFolder.toString(), uxfName);
            return;
        }

        try (Stream<Path> paths = Files.list(devRoot)) {
            List<Path> directories = paths
                    .filter(Files::isDirectory)
                    .toList();

            if (directories.isEmpty()) {
                System.out.println("Nenhuma subpasta encontrada em " + LOCAL_DEVELOPMENT_FOLDER);
                return;
            }

            for (Path dir : directories) {
                try (Stream<Path> files = Files.list(dir)) {
                    boolean hasRelevantFiles = files
                            .map(Path::toFile)
                            .anyMatch(file ->
                                    file.getName().endsWith(JAVA) ||
                                            file.getName().endsWith(UXF));

                    if (!hasRelevantFiles) {
                        System.out.println("Ignorando " + dir + ": nenhum arquivo .java ou .uxf encontrado.");
                        continue;
                    }
                }

                System.out.println("Rodando " + dir);
                evaluateImplementation(dir.toString(), null);
            }
        } catch (IOException e) {
            System.err.println("Erro ao listar diretórios em " + LOCAL_DEVELOPMENT_FOLDER + ": " + e.getMessage());
        }
    }
}
