package org.github.miracelwhipp.neincoco;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProjectHelper;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;

@Mojo(name = "post-process", aggregator = true, defaultPhase = LifecyclePhase.VERIFY)
public class PostprocessMojo extends NeinCocoMojo {

    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String INDENTATION = "{http://xml.apache.org/xslt}indent-amount";

    @Parameter(defaultValue = "${project.build.directory}/jacoco.xml", property = "neincoco.xml-report-file")
    private File xmlReportFile;

    @Parameter(defaultValue = "classpath:filter-jacoco.xslt", property = "neincoco.transformation")
    private String transformation;

    @Component
    private MavenProjectHelper projectHelper;

    @Override
    public void execute() throws MojoExecutionException {

        if (skip) {

            getLog().info("skipping execution");
            return;
        }

        if (skipDueToNonLastBehaviour()) {

            return;
        }

        if (!rawXmlReportFile.isFile()) {

            throw new MojoExecutionException("cannot find report file file: " + rawXmlReportFile);
        }

        try {

            StreamSource styleSource = resolveStylesheetSource(transformation);

            Transformer transformer = newTransformer(styleSource);

            Source xmlSource = makeSource();
            StreamResult xmlResult = new StreamResult(xmlReportFile);

            transformer.transform(xmlSource, xmlResult);

            projectHelper.attachArtifact(session.getCurrentProject(), "xml", "coverage-final", xmlReportFile);


        } catch (TransformerException e) {

            throw new MojoExecutionException(e);
        }
    }

    @NonNullDecl
    private Source makeSource() throws MojoExecutionException {

        try {

            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            parserFactory.setNamespaceAware(true);

            XMLReader xmlReader = parserFactory.newSAXParser().getXMLReader();

            xmlReader.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));

            SAXSource source = new SAXSource(xmlReader, new InputSource(new FileInputStream(rawXmlReportFile)));
            source.setSystemId(rawXmlReportFile.toURI().toString());

            return source;

        } catch (FileNotFoundException | SAXException | ParserConfigurationException e) {

            throw new MojoExecutionException(e);
        }
    }

    private Transformer newTransformer(StreamSource styleSource) throws TransformerConfigurationException {

        TransformerFactory factory = newTransformerFactory();

        Transformer transformer = factory.newTransformer(styleSource);

        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(INDENTATION, "2");

        transformer.setOutputProperty(javax.xml.transform.OutputKeys.ENCODING, "UTF-8");

        return transformer;
    }

    private TransformerFactory newTransformerFactory() {

        TransformerFactory factory = TransformerFactory.newInstance();

        return factory;
    }

    private StreamSource resolveStylesheetSource(String stylesheet) throws MojoExecutionException {

        try {

            if (stylesheet.startsWith(CLASSPATH_PREFIX)) {

                String path = stylesheet.substring(CLASSPATH_PREFIX.length());

                if (path.startsWith("/")) {

                    path = path.substring(1);
                }

                URL resource = getClass().getClassLoader().getResource(path);

                if (resource == null) {

                    throw new MojoExecutionException(path + " not found in classpath");
                }

                return new StreamSource(resource.openStream(), resource.toExternalForm());
            }


            // if this is not a url - note that a check only for ':' would also find windows like absolute paths
            if (!stylesheet.contains(":/")) {

                File file = new File(stylesheet);

                if (!file.isAbsolute()) {

                    file = new File(session.getCurrentProject().getBasedir(), stylesheet);
                }

                if (!file.isFile()) {

                    throw new MojoExecutionException("could not find stylesheet file : " + file.getAbsolutePath());
                }

                return new StreamSource(file);
            }

            URL url = new URL(stylesheet);

            return new StreamSource(url.openStream(), url.toExternalForm());

        } catch (IOException e) {

            throw new MojoExecutionException(e.getMessage(), e);
        }
    }


}
