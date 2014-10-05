package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object Math extends Controller {
    
  private val defaultMSG = 
    NotImplemented(Json.obj("message"->"sorry this is not ready", "Status"->501))
  
  
  private def baseFunction(base:Double, values: Array[Double],
    func: (Double,Double) => Double, cond: Double => Boolean):Double = {
        def iter(acc:Double,values:Array[Double], 
            f: (Double,Double) => Double, c: Double => Boolean):Double = {
            if(values.isEmpty){ 
                base
            } else if (c(values.head)) {
                throw new IllegalArgumentException(values.head + " was invalid")
            } else { 
                iter(f(acc,values.head), values.tail,f,c)
            }
        }
        iter(base, values,func,cond)
    }
  
  def home = TODO

  def add = Action(parse.json){request => defaultMSG }
  
  def sub = Action(parse.json){request => defaultMSG }
  
  def mul = Action(parse.json){request => defaultMSG }
  
  def div = Action(parse.json){request => defaultMSG }
  
  def mod = Action(parse.json){request => defaultMSG }
  
  def pwr = Action(parse.json){request => defaultMSG }

}