package indb

import eug.shared.GenericObject
import org.jooq.{DSLContext, Field, Table}
import org.jooq.impl.DSL._
import org.jooq.impl.SQLDataType
import interop.EUGInterop._

import collection.JavaConverters._

/**
  * Created by Ataraxia on 02/06/2016.
  */
case class ForeignKey (table: String, field: String)

object Province {
  private val provinceFields = Seq(
    field("id", SQLDataType.INTEGER.nullable(false)),
    field("name", SQLDataType.VARCHAR)  ,
    field("owner", SQLDataType.VARCHAR),
    field("controller", SQLDataType.VARCHAR),
    field("garrison", SQLDataType.FLOAT),
    field("colonial", SQLDataType.FLOAT),
    field("life_rating", SQLDataType.FLOAT),
    field("last_imigration", SQLDataType.VARCHAR),
    field("crime", SQLDataType.FLOAT)
  )

  private val popFields = Seq(
    field("id", SQLDataType.INTEGER.nullable(false)),
    field("type", SQLDataType.VARCHAR),
    field("size", SQLDataType.INTEGER),
    field("culture", SQLDataType.VARCHAR),
    field("religion", SQLDataType.VARCHAR),
    field("money", SQLDataType.FLOAT),
    field("con", SQLDataType.FLOAT),
    field("literacy", SQLDataType.FLOAT),
    field("bank", SQLDataType.FLOAT),
    field("con_factor", SQLDataType.FLOAT),
    field("everyday_needs", SQLDataType.FLOAT),
    field("luxury_needs", SQLDataType.FLOAT),
    field("random", SQLDataType.INTEGER),
    field("promoted", SQLDataType.INTEGER),
    field("mil", SQLDataType.FLOAT),

    // artisan-esque specific (e.g. vanilla: artisans, pdm: capitalists)
    field("production_type", SQLDataType.VARCHAR),
    field("last_spending", SQLDataType.FLOAT),
    field("current_producing", SQLDataType.FLOAT),
    field("percent_afforded", SQLDataType.FLOAT),
    field("percent_sold_domestic", SQLDataType.FLOAT),
    field("percent_sold_expert", SQLDataType.FLOAT),
    field("leftover", SQLDataType.FLOAT),
    field("throttle", SQLDataType.FLOAT),
    field("need_cost", SQLDataType.FLOAT),
    field("production_income", SQLDataType.FLOAT),

    //foreign keys
    field("prov_id", SQLDataType.INTEGER.nullable(false))
  )

  val PROVINCE_FK = ForeignKey("province", "id")

  def insertProvinces(context: DSLContext, provinces: Seq[GenericObject]) = {
    createProvinceTables(context)

    insertProvinceValues(context, provinces)

    provinces.foreach {
      province =>
        insertProvinceProperties(context, province)
    }

    println("provinces done......")
  }

  private def insertProvinceValues(context: DSLContext, provinces: Seq[GenericObject]): Int = {
    val order = context.insertInto(
      table("province"),
      provinceFields: _*
    )

    provinces.foreach {
      province =>
        val map = province.valueMap()
        val vs = Seq(
          province.name.toInt,
          map.getOrElse("name", ""),
          map.getOrElse("owner", ""),
          map.getOrElse("controller", ""),
          map.getOrElse("garrison", "0"),
          map.getOrElse("colonial", "0"),
          map.getOrElse("life_rating", "0"),
          map.getOrElse("last_imigration", ""),
          map.getOrElse("crime", "0")
        )

        order.values(vs.asJavaCollection)
    }
    order.execute()
  }

  def createProvinceTables(context: DSLContext): Int = {
    context.createTableIfNotExists("province")
      .columns(provinceFields: _*)
      .constraint(constraint("PROVINCE_ID").primaryKey("id"))
      .execute()

    context.createTableIfNotExists("pop")
      .columns(popFields: _*)
      .constraints(
        constraint("POP_PK").primaryKey("id", "type"),
        constraint("POP_FK").foreignKey("prov_id").references(PROVINCE_FK.table, PROVINCE_FK.field)
      ).execute()
  }

  private def insertProvinceProperties(context: DSLContext, provinceBlock: GenericObject): Unit = {
    val except = Seq("rgo", "building_construction", "party_loyalty", "unit_names", "modifier", "military_construction")

    val provinceID = provinceBlock.name.toInt
    val pops = provinceBlock.childrenSeq().filter( child => !except.contains(child.name) )

    insertPops(context, provinceID, pops)
  }

  private def insertPops(context: DSLContext, provinceID: Int, pops: Seq[GenericObject]): Unit = {
    // PK = (Province ID, Pop ID, Pop Type)

    val order = context.insertInto(
      table("pop"),
      popFields: _*
    )

    pops.foreach {
      popBlock =>
        val culture = popBlock.getVariable(2) //paradox, why the f-????

        val map = popBlock.valueMap()

        val vs = Seq(
          map.getOrElse("id", throw new NullPointerException(s"Province ID cannot be null: $popBlock")),
          popBlock.name,
          map.getOrElse("size", "0"),
          culture.varname,
          culture.getValue,
          map.getOrElse("money", "0"),
          map.getOrElse("con", "0"),
          map.getOrElse("literacy", "0"),
          map.getOrElse("bank", "0"),
          map.getOrElse("con_factor", "0"),
          map.getOrElse("everyday_needs", "0"),
          map.getOrElse("luxury_needs", "0"),
          map.getOrElse("random", "0"),
          map.getOrElse("promoted", "0"),
          map.getOrElse("mil", "0"),
          map.getOrElse("production_type", ""),
          map.getOrElse("last_spending", "0"),
          map.getOrElse("current_producing", "0"),
          map.getOrElse("percent_afforded", "0"),
          map.getOrElse("percent_sold_domestic", "0"),
          map.getOrElse("percent_sold_expert", "0"),
          map.getOrElse("leftover", "0"),
          map.getOrElse("throttle", "0"),
          map.getOrElse("need_cost", "0"),
          map.getOrElse("production_income", "0"),
          provinceID
        )

        order.values(vs.asJavaCollection)
    }

    order.execute()
  }
}
