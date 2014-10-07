package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.Logger

object Math extends Controller {
    
  implicit val rds = ((__ \ 'values).read[List[Double]])
  
  def handleJson(json:JsValue,start:Double,f:(Double,Double)=>Double,c: Double => Boolean) ={
        json.validate[(List[Double])].map{
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
            ex => BadRequest(JsError.toFlatJson(ex))
        }
  }
  
  
    def add = Action { request => 
        request.body.asJson.map{ json =>
            handleJson(json,0.0,(x:Double,y:Double)=>x+y,(x:Double)=>false)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
  
    def sub = Action { request => 
        request.body.asJson.map{ json =>
            handleJson(json,0.0,(x:Double,y:Double)=>x-y,(x:Double)=>false)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
  
    def mul = Action { request => 
        request.body.asJson.map{ json =>
            handleJson(json,1.0,(x:Double,y:Double)=>x*y,(x:Double)=>false)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
  
    def div = Action { request => 
        request.body.asJson.map{ json =>//find the start value
            handleJson(json,1.0,(x:Double,y:Double)=>x/y,(x:Double)=>x==0)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
  
    def mod = Action { request => 
        request.body.asJson.map{ json =>
            handleJson(json,1.0,(x:Double,y:Double)=>x%y,(x:Double)=>x==0)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
  
    def pwr = Action { request => 
        request.body.asJson.map{ json =>
            handleJson(json,1.0,(x:Double,y:Double)=>x^y,(x:Double)=>false)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
  
    def home = Action{request => defaultMSG }
  
    private val defaultMSG = NotImplemented(Json.obj("message"->"sorry this is not ready", "Status"->501))
  
  private def baseFunction(base:Double, values: List[Double],
    func: (Double,Double) => Double, cond: Double => Boolean):Double = {
        def iter(acc:Double,values:List[Double],f: (Double,Double) => Double, c: Double => Boolean):Double = {
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