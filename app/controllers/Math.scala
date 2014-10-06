package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Math extends Controller {
    
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
  
  def handleRequest(start:Double,f:(Double,Double) => Double,c:Double=>Boolean)={
      Action(parse.json){ request => 
       request.body.validate[(Array[Double])].map{
           case (values) => {
                   var result = 0.0
                   try {
                       var result = baseFunction(start, values, f, c)
                       Ok(Json.obj("result"-> result))
                    } catch {
                       case ex: IllegalArgumentException => {BadRequest(Json.obj("message"->"Illegal arguments", "Status"->400))}
                       case ex: Exception => {InternalServerError(Json.obj("message"->"Unknown Error", "Status"->500))}
                    }
                }
            }.recoverTotal {
                ex => BadRequest(Json.obj("message"->"Bad Request", "Status"->400))
            }
        }
    }

  def add = handleRequest(0.0,(x:Double,y:Double) => x+y,(x:Double)=>false)
      
  
  def sub = Action(parse.json){request => defaultMSG }
  
  def mul = Action(parse.json){request => defaultMSG }
  
  def div = Action(parse.json){request => defaultMSG }
  
  def mod = Action(parse.json){request => defaultMSG }
  
  def pwr = Action(parse.json){request => defaultMSG }
  
  def home = TODO

}