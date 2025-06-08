package com.github.deb4cker.vic.uxfreader.compilation;

import com.github.deb4cker.vic.uxfreader.commons.interfaces.IdentifiedClassLoader;
import com.github.deb4cker.vic.uxfreader.commons.interfaces.RuntimeCompiler;
import com.github.deb4cker.vic.uxfreader.enums.ClassLoaderContext;
import com.github.deb4cker.vic.uxfreader.exception.SubmittedFileWithCompilationErrorsException;
import com.github.deb4cker.vic.commons.interfaces.Loggable;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.util.*;

import static com.github.deb4cker.vic.uxfreader.commons.constants.CompilationErrorMessages.MODELED_CLASSES_COMPILATION_ERROR;
import static com.github.deb4cker.vic.uxfreader.commons.constants.CompilationErrorMessages.SUBMITTED_CLASSES_COMPILATION_ERROR;

public class InMemoryRuntimeCompiler implements RuntimeCompiler, Loggable {
    private final Map<String, InMemoryByteCode> modeledClasses = new HashMap<>();
    private final Map<String, InMemoryByteCode> submittedClasses = new HashMap<>();
    private final IdentifiedClassLoader modeledClassLoader;
    private final IdentifiedClassLoader submittedClassLoader;
    private final JavaCompiler compiler;

    public InMemoryRuntimeCompiler() {
        modeledClassLoader   = new GeneratedContextClassLoader(ClassLoaderContext.MODELED);
        submittedClassLoader = new GeneratedContextClassLoader(ClassLoaderContext.SUBMITTED);
        compiler = ToolProvider.getSystemJavaCompiler();
    }

    @Override
    public void compileJavaFiles(List<File> files, boolean isModel) {
        if (compiler == null) {
            throw new IllegalStateException("Não foi encontrado um compilador Java no sistema.");
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnostics, null, null);

        JavaFileManager fileManager = new ForwardingJavaFileManager<>(standardFileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
                InMemoryByteCode byteCode = new InMemoryByteCode(className);
                if (isModel) {
                    modeledClasses.put(className, byteCode);
                } else {
                    submittedClasses.put(className, byteCode);
                }
                return byteCode;
            }
        };

        List<JavaFileObject> fileObjects = new ArrayList<>();
        for (File file : files) {
            fileObjects.add(new SimpleJavaFileObject(file.toURI(), JavaFileObject.Kind.SOURCE) {
                @Override
                public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
                    return new String(Files.readAllBytes(file.toPath()));
                }
            });
        }

        List<String> options = new ArrayList<>();
        options.add("-parameters");

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, fileObjects );

        boolean success = task.call();
        if (!success) {
            String errorMessageTemplate = isModel? MODELED_CLASSES_COMPILATION_ERROR : SUBMITTED_CLASSES_COMPILATION_ERROR;
            String formattedMessage = "";
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                String errorMessage = diagnostic.getMessage(null);
                formattedMessage = String.format(errorMessageTemplate, errorMessage);
            }

            throw new SubmittedFileWithCompilationErrorsException(formattedMessage);
        }
    }

    public static class InMemoryByteCode extends SimpleJavaFileObject {
        private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        protected InMemoryByteCode(String name) {
            super(URI.create("byte:///" + name.replace('.', '/') + Kind.CLASS.extension), Kind.CLASS);
        }

        @Override
        public OutputStream openOutputStream() {
            return outputStream;
        }

        public byte[] getByteCode() {
            return outputStream.toByteArray();
        }
    }

    public Map<String, Class<?>> getModeledClasses() {
        return loadClasses(modeledClasses, modeledClassLoader);
    }

    public Map<String, Class<?>> getSubmittedClasses() {
        return loadClasses(submittedClasses, submittedClassLoader);
    }

    private Map<String, Class<?>> loadClasses(Map<String, InMemoryByteCode> compiledClasses, IdentifiedClassLoader classLoader) {
        Map<String, Class<?>> result = new HashMap<>();

        if (classLoader instanceof GeneratedContextClassLoader gccLoader) {
            for (var entry : compiledClasses.entrySet()) {
                byte[] byteCode = entry.getValue().getByteCode();
                if (byteCode.length == 0) {
                    System.err.println("Erro: bytecode vazio para a classe " + entry.getKey());
                    continue;
                }
                gccLoader.addClassBytes(entry.getKey(), byteCode);
            }
        }

        for (var entry : compiledClasses.entrySet()) {
            String className = entry.getKey();
            byte[] byteCode = entry.getValue().getByteCode();
            if (byteCode.length == 0) continue;

            try {
                assert classLoader instanceof GeneratedContextClassLoader;
                Class<?> loadedClass = Class.forName(className, true, (GeneratedContextClassLoader) classLoader);
                result.put(className, loadedClass);
            } catch (ClassNotFoundException e) {
                Class<?> loadedClass = classLoader.loadClassFromBytes(byteCode, className);
                result.put(className, loadedClass);
            }
        }

        return result;
    }
}