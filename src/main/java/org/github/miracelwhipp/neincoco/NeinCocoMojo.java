package org.github.miracelwhipp.neincoco;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.List;

public abstract class NeinCocoMojo extends AbstractMojo {

    /**
     * This property skips the entire execution of every neincoco goal
     */
    @Parameter(defaultValue = "false", property = "neincoco.skip")
    protected boolean skip;

    /**
     * This parameter defines where the jacoco exec files will be merged to and where they will be transformed to an
     * xml report from.
     */
    @Parameter(defaultValue = "${project.build.directory}/merged-jacoco.exec", property = "neincoco.merged-exec-file")
    protected File mergedExecFile;

    @Parameter(defaultValue = "${session}", readonly = true)
    protected MavenSession session;

    @Parameter(defaultValue = "${project.build.directory}/jacoco.raw.xml", property = "neincoco.raw-xml-report-file")
    protected File rawXmlReportFile;

    /**
     * This parameter defines the behavior of the merge mojo if it is not executed in the last module of the build.<br/>
     * possible values are
     * <ul>
     *     <li>skip (default) - the mojo skips its execution if it is not executed in the last module of the build</li>
     *     <li>fail - the mojo fails if it is not executed in the last module of the build</li>
     *     <li>execute - the mojo works normally even if it is not executed in the last module of the build</li>
     * </ul>
     *
     */
    @Parameter(defaultValue = "skip", property = "neincoco.if-not-last")
    private String ifNotLast;

    private NonLastBehaviour nonLastBehaviour;

    protected NonLastBehaviour getNonLastBehaviour() {

        if (nonLastBehaviour != null) {

            return nonLastBehaviour;
        }

        return nonLastBehaviour = NonLastBehaviour.valueOf(ifNotLast.toUpperCase());
    }

    protected boolean skipDueToNonLastBehaviour() throws MojoExecutionException {

        if (isLastProject()) {

            return false;
        }

        return switch (getNonLastBehaviour()) {
            case FAIL ->
                    throw new MojoExecutionException("failing neincoco goal due to not being called in the last module of the reactor");
            case SKIP -> {

                getLog().info("skipping neincoco goal due to not being called in the last module of the reactor");
                yield true;
            }
            default -> false;
        };

    }

    private boolean isLastProject() {

        List<MavenProject> sorted = session.getProjectDependencyGraph().getSortedProjects();

        return session.getCurrentProject().equals(sorted.get(sorted.size() - 1));
    }
}
