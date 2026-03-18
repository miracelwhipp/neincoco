package org.github.miracelwhipp.neincoco;

import org.apache.maven.project.MavenProject;
import org.jacoco.report.ISourceFileLocator;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DirectorySourceFileLocator implements ISourceFileLocator {

    private final File sourceRoot;
    private final Charset sourceEncoding;
    private final int tabWidth;

    public DirectorySourceFileLocator(
            File sourceRoot,
            Charset sourceEncoding,
            int tabWidth
    ) {
        this.sourceRoot = sourceRoot;
        this.sourceEncoding = sourceEncoding;
        this.tabWidth = tabWidth;
    }

    public static List<DirectorySourceFileLocator> of(MavenProject mavenProject) {

        String sourceEncoding = mavenProject.getProperties().getProperty("project.build.sourceEncoding");

        Charset sourceCharset = sourceEncoding == null ? StandardCharsets.UTF_8 : Charset.forName(sourceEncoding);

        return mavenProject.getCompileSourceRoots().stream()
                .map(sourceRoot ->
                        new DirectorySourceFileLocator(new File(sourceRoot), sourceCharset, 4))
                .toList();
    }

    @Override
    public Reader getSourceFile(String packageName, String fileName) throws IOException {

        File file = sourceRoot;

        if (!packageName.isEmpty()) {

            file = new File(file, packageName);
        }

        file = new File(file, fileName);

        if (!file.isFile()) {

            return null;
        }

        return new InputStreamReader(new FileInputStream(file), sourceEncoding);
    }

    @Override
    public int getTabWidth() {
        return tabWidth;
    }
}
