package com.github.deb4cker.vic.vpl.cli.pipeline;

import com.github.deb4cker.vic.uxfreader.compilation.InMemoryRuntimeCompiler;
import com.github.deb4cker.vic.evaluator.models.ClassData;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JavaCompilationService {

    private final InMemoryRuntimeCompiler compiler;

    public JavaCompilationService() {
        this.compiler = new InMemoryRuntimeCompiler();
    }

    public void compileModeled(List<File> files) {
        compiler.compileJavaFiles(files, true);
    }

    public void compileSubmitted(List<File> files) {
        compiler.compileJavaFiles(files, false);
    }

    public List<ClassData> loadModeledClasses() {
        return toClassDataList(compiler.getModeledClasses());
    }

    public List<ClassData> loadSubmittedClasses() {
        return toClassDataList(compiler.getSubmittedClasses());
    }

    private List<ClassData> toClassDataList(Map<String, Class<?>> compiled) {
        return compiled.values().stream().map(ClassData::new).toList();
    }
}
