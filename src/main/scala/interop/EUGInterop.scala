package interop

import eug.shared.{GenericList, GenericObject, ObjectVariable}
import collection.JavaConverters._

/**
  * Created by Ataraxia on 30/05/2016.
  */
object EUGInterop {

  implicit class GenericObjectScala(val javaObj: GenericObject) {
    type VariableKey    = String
    type VariableValue  = String

    val genericObject = Option(javaObj).getOrElse(throw new NullPointerException("GenericObject must not be null."))

    def getChildrenSeq(name: String): Seq[GenericObject] = {
      genericObject.getChildren(name).asScala
    }

    def childrenSeq(): Seq[GenericObject] = {
      genericObject.children.asScala
    }

    def listSeq(): Seq[GenericList] = {
      genericObject.lists.asScala
    }

    def valueSeq(): Seq[ObjectVariable] = {
      genericObject.values.asScala
    }

    def getStringSeq(name: String): Seq[String] = {
      genericObject.getStrings(name).asScala
    }

    def valueMap(): Map[VariableKey, VariableValue] = {
      genericObject.valueSeq().map { v => ( v.varname, v.getValue ) }.toMap
    }
  }

  implicit class GenericListScala(val javaList: GenericList) {
    val genericList = Option(javaList).getOrElse(throw new NullPointerException("GenericList must not be null."))

    def getListSeq(name: String): Seq[String] = {
      genericList.getList.asScala
    }
  }

  implicit class ObjectVariableScala(val javaVar: ObjectVariable) {
    val objectVariable = Option(javaVar).getOrElse(throw new NullPointerException("ObjectVariable must not be null."))
  }
}


