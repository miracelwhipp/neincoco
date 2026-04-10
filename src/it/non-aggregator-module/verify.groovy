import groovy.xml.XmlSlurper
import java.io.File

def expectedJacocoFile = "server/integration-test/target/jacoco.xml"
File reportFile = new File(basedir, expectedJacocoFile)

assert reportFile.isFile() : "$reportFile does not exist"
assert reportFile.length() > 0 : "$reportFile is empty"

def report = new XmlSlurper().parse(reportFile)

def emptyClasses = report.package.class.findAll { it.counter.size() == 0 }
assert emptyClasses.size() == 0 : "there should be no classes without a counter, but found " + emptyClasses*.@name


def expectedSourceDirFile = "server/integration-test/target/jacoco-source-directories.txt"
def sourceDirectories = new File(basedir, expectedSourceDirFile)

assert sourceDirectories.isFile() : "$expectedSourceDirFile does not exist"
assert sourceDirectories.length() > 0 : "$expectedSourceDirFile is empty"

assert sourceDirectories.text == """generated${File.separator}src${File.separator}main${File.separator}java
generated${File.separator}target${File.separator}generated-sources${File.separator}annotations
common${File.separator}src${File.separator}main${File.separator}java
common${File.separator}target${File.separator}generated-sources${File.separator}annotations
client${File.separator}src${File.separator}main${File.separator}java
client${File.separator}target${File.separator}generated-sources${File.separator}annotations
server${File.separator}implementation${File.separator}src${File.separator}main${File.separator}java
server${File.separator}implementation${File.separator}target${File.separator}generated-sources${File.separator}annotations
"""

return true