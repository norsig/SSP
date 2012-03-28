# The SqlServer JDBC driver must be downloaded and separately due to licensing restrictions.  You can get the driver from:  http://msdn.microsoft.com/data/jdbc
# Extract the driver, and then install it to your local maven repository (~/.m2) with the following line.  It will allow you to build the project.
mvn install:install-file -Dfile=sqljdbc4-2.0.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqlserver-jdbc4 -Dversion=2.0 -Dpackaging=jar
# Alternately, if you are just running ssp (not building it), you can drop the jar file in your application server's "lib" directory.
