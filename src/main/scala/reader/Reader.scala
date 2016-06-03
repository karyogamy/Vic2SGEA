package reader

import java.io.File
import java.sql.DriverManager

import eug.parser.EUGFileIO
import interop.EUGInterop._
import org.jooq.SQLDialect
import org.jooq.impl.DSL

import indb.Province._

/**
  * Created by Ataraxia on 30/05/2016.
  */
object Reader {

  type FileLocation = String

  def main(args: Array[String]) = {
    args match {
      case Array(fileLocation: FileLocation) =>
        val file = new File(fileLocation)
        read(file)
        server()
        System.exit(0)
      case _ => printUsage()
    }
  }

  def printUsage() = {
    println(s"Usage: ${this.getClass.getCanonicalName} <file location>")

    System.exit(1)
  }

  def server() = {
    import org.h2.tools.Server

    Server.createWebServer().start()
    while(true)()
  }


  def read(file: File) = {
    val connection = DriverManager.getConnection("jdbc:h2:~/test;DATABASE_TO_UPPER=false", "sa", "")
    val context = DSL.using(connection, SQLDialect.H2)

    val readFile = EUGFileIO.load(file)

    val child = readFile.childrenSeq()

    val countryRegex = "[A-Z]{3}".r
    val countries = child.filter(block => countryRegex.pattern.matcher(block.name).matches())

    val provinceRegex = "\\d+".r
    val provinces = child.filter(block => provinceRegex.pattern.matcher(block.name).matches())

    context.execute("DROP ALL OBJECTS")

    insertProvinces(context, provinces)
  }
}
