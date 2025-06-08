package com.github.deb4cker.vic.uxfreader.commons.interfaces;

import java.io.File;
import java.util.List;

public interface RuntimeCompiler {
    /**
     * Compila arquivos Java ou código-fonte em memória.
     * @param files Lista de arquivos java.
     * @param isModel Define se a compilação é do modelo ou do submetido.
     */
    void compileJavaFiles(List<File> files, boolean isModel);
}

