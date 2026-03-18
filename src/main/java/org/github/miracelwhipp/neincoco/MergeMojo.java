package org.github.miracelwhipp.neincoco;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.DirectoryScanner;
import org.jacoco.core.tools.ExecFileLoader;

import java.io.File;
import java.util.List;

@Mojo(name = "merge", aggregator = true, defaultPhase = LifecyclePhase.VERIFY)
public class MergeMojo extends NeinCocoMojo {

    @Parameter
    private List<String> execIncludes = List.of("**/jacoco.exec");

    @Parameter
    private List<String> execExcludes = List.of();

    public void execute() throws MojoExecutionException {

        if (skip) {

            getLog().info("skipping execution");
            return;
        }

        if (skipDueToNonLastBehaviour()) {

            return;
        }

        try {

            ExecFileLoader loader = new ExecFileLoader();

            for (MavenProject project : session.getProjects()) {

                File baseDir = new File(project.getBuild().getDirectory());

                if (!baseDir.isDirectory()) {

                    continue;
                }

                DirectoryScanner scanner = new DirectoryScanner();
                scanner.setBasedir(baseDir);
                scanner.setIncludes(execIncludes.toArray(new String[0]));
                scanner.setExcludes(execExcludes.toArray(new String[0]));
                scanner.scan();

                String[] foundFiles = scanner.getIncludedFiles();

                for (String fileName : foundFiles) {

                    File exec = new File(baseDir, fileName);

                    if (!exec.exists()) {

                        continue;
                    }

                    getLog().debug("loading exec file " + exec.getAbsolutePath());
                    loader.load(exec);
                }
            }

            loader.save(mergedExecFile, false);

        } catch (Exception e) {

            throw new MojoExecutionException(e);
        }
    }

}