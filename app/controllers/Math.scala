package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Math extends Controller {
    
  def add = Action(parse.json){ request => 
       request.body.validate[(Array[Double])].map{
           case (values) => {
                   var result = 0.0
                   try {
                       var result = baseFunction(0, values, (x:Double,y:Double)=>x+y, (x:Double)=>false)
                       Ok(Json.obj("result"-> result))
                    } catch {
                       case ex: IllegalArgumentException => {BadRequest(Json.obj("message"->"Illegal arguments", "Status"->400))}
                       case ex: Exception => {InternalServerError(Json.obj("message"->"Unknown Error", "Status"->500))}
                    }
                }
            }.recoverTotal {
                ex => BadRequest(JsError.toFlatJson(ex))
            }
        }
  
  def sub = Action(parse.json){request => defaultMSG }
  
  def mul = Action(parse.json){request => defaultMSG }
  
  def div = Action(parse.json){request => defaultMSG }
  
  def mod = Action(parse.json){request => defaultMSG }
  
  def pwr = Action(parse.json){request => defaultMSG }
  
  def home = Action{request => defaultMSG }
  
  private val defaultMSG = NotImplemented(Json.obj("message"->"sorry this is not ready", "Status"->501))
  
  private def baseFunction(base:Double, values: Array[Double],
    func: (Double,Double) => Double, cond: Double => Boolean):Double = {
        def iter(acc:Double,values:Array[Double],f: (Double,Double) => Double, c: Double => Boolean):Double = {
            if(values.isEmpty){ 
                acc
            } else if (c(values.head)) {
                throw new IllegalArgumentException(values.head + " was invalid")
            } else { 
                iter(f(acc,values.head), values.tail,f,c)
            }
        }
        iter(base, values,func,cond)
    }

}