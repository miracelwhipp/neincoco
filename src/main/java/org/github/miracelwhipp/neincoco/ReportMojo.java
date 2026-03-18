package org.github.miracelwhipp.neincoco;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.MultiSourceFileLocator;
import org.jacoco.report.xml.XMLFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Mojo(name = "report", defaultPhase = LifecyclePhase.VERIFY, threadSafe = true)
public class ReportMojo extends NeinCocoMojo {

    @Parameter(defaultValue = "Coverage ${project.groupId}:${project.artifactId}:${project.version}")
    private String reportName;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (skip) {

            getLog().info("skipping execution");
            return;
        }

        if (skipDueToNonLastBehaviour()) {

            return;
        }

        if (!mergedExecFile.exists()) {

            throw new MojoExecutionException("cannot find merged exec file: " + mergedExecFile);
        }

        ExecFileLoader loader = new ExecFileLoader();

        try {

            loader.load(mergedExecFile);

        } catch (IOException e) {

            throw new MojoExecutionException("cannot load " + mergedExecFile.getAbsolutePath(), e);
        }

        generateJacocoXml(loader, rawXmlReportFile);
    }

    private void generateJacocoXml(ExecFileLoader loader, File outputFile) throws MojoExecutionException {

        CoverageBuilder coverageBuilder = new CoverageBuilder();

        Analyzer analyzer = new Analyzer(loader.getExecutionDataStore(), coverageBuilder);

        for (MavenProject project : session.getProjects()) {

            File classesDir = new File(project.getBuild().getOutputDirectory());

            if (!classesDir.exists()) {

                continue;
            }

            try {

                analyzer.analyzeAll(classesDir);

            } catch (IOException e) {

                throw new MojoExecutionException(e);
            }
        }

        IBundleCoverage bundleCoverage = coverageBuilder.getBundle(reportName);

        try (FileOutputStream out = new FileOutputStream(outputFile)) {

            IReportVisitor visitor = new XMLFormatter().createVisitor(out);

            visitor.visitInfo(loader.getSessionInfoStore().getInfos(), loader.getExecutionDataStore().getContents());

            MultiSourceFileLocator sourceLocator = new MultiSourceFileLocator(4);

            for (MavenProject module : session.getProjects()) {

                DirectorySourceFileLocator.of(module).forEach(sourceLocator::add);
            }

            visitor.visitBundle(bundleCoverage, sourceLocator);

            visitor.visitEnd();

        } catch (IOException e) {

            throw new MojoExecutionException(e);
        }
    }
}
