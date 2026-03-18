import groovy.xml.XmlSlurper
import java.io.File

def expectedJacocoFile = "server/integration-test/target/jacoco.xml"
File reportFile = new File(basedir, expectedJacocoFile)

assert reportFile.isFile() : "$reportFile does not exist"
assert reportFile.length() > 0 : "$reportFile is empty"

def report = new XmlSlurper().parse(reportFile)

def emptyClasses = report.package.class.findAll { it.counter.size() == 0 }
assert emptyClasses.size() == 0 : "there should be no classes without a counter, but found " + emptyClasses*.@name



return true