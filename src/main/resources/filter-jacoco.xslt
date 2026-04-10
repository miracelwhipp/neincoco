<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="ignoreNestedClassesOfIgnoredNonNestedClasses" select="true()" />


    <!-- copy every xml element as long as it is not ignored by the templates below this one -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>


    <!-- elements that contain no counters are the ones marked as ignored -->
    <xsl:template match="class[not(counter)]" />

    <xsl:template match="method[not(counter)]" />

    <xsl:template match="package[not(class/counter)]" />

    <xsl:template match="sourcefile[not(counter)]" />

    <!-- match only if feature is enabled. in this case find classes whose name are not contained in the source file name:
     those are nested classes, they have a '$' added to the containing class name.
     we do not check for '$' since it is (formally) possible to define a class with a '$' in its name - in this case a
     '$' will be part of the file name.
      -->
    <xsl:template match="class[not(contains(concat(@name, '.java'), @sourcefilename)) and $ignoreNestedClassesOfIgnoredNonNestedClasses]">
        <xsl:variable name="sourceFileName" select="@sourcefilename"/>
        <!-- this check finds a class element in the same package, that is a 'root class' and is located in the same file
        as the class inspected.
        If this class exists and has a 'counter' element as child, we copy the class inspected -->
        <xsl:if test="../class[@sourcefilename = $sourceFileName and contains(concat(@name, '.java'), @sourcefilename) and counter]">
            <xsl:copy>
                <xsl:apply-templates select="@*|node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

    <!-- this works the same for sourcefile as is done for classes: only show source files, whose most upper level
    class is not ignored -->
    <xsl:template match="sourcefile[counter and $ignoreNestedClassesOfIgnoredNonNestedClasses]">
        <xsl:variable name="sourceFileName" select="@name"/>
        <xsl:if test="../class[@sourcefilename = $sourceFileName and contains(concat(@name, '.java'), @sourcefilename) and counter]">
            <xsl:copy>
                <xsl:apply-templates select="@*|node()"/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>