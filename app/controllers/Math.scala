package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.math.pow

object Math extends Controller {
    
  implicit val rds = ((__ \ 'values).read[List[Double]])
  
  def handleJson(json:JsValue,start:Double,f:(Double,Double)=>Double,c: Double => Boolean) ={
        json.validate[(List[Double])].map{
            case (values) => {
                var result = 0.0
                try {
                    if(start.equals(Double.NaN)) result = baseFunction(values.head, values.tail, f, c)
                    else result = baseFunction(start, values, f, c)
                    Ok(Json.obj("result"-> result))
                } catch {
                   case ex: IllegalArgumentException => {BadRequest(Json.obj("message"->ex.toString, "Status"->400))}
                   case ex: Exception => {InternalServerError(Json.obj("message"->"Unknown Error", "Status"->500))}
                }
            }
        }.recoverTotal {
            ex => BadRequest(JsError.toFlatJson(ex))
        }
  }
  
   def baseFunction(base:Double, values: List[Double],f: (Double,Double) => Double, c: Double => Boolean):Double = {
        def iter(acc:Double,values:List[Double],g: (Double,Double) => Double, d: Double => Boolean):Double = {
            if(values.isEmpty) acc
            else if (d(values.head)) throw new IllegalArgumentException(values.head + " was invalid")
            else iter(g(acc,values.head), values.tail,g,c)
        }
        iter(base, values,f,c)
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
            handleJson(json,Double.NaN,(x:Double,y:Double)=>x-y,(x:Double)=>false)
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
        request.body.asJson.map{ json =>
            handleJson(json,Double.NaN,(x:Double,y:Double)=>x/y,(x:Double)=>x==0)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
  
    def mod = Action { request =>
        request.body.asJson.map{ json =>
            handleJson(json,Double.NaN,(x:Double,y:Double)=>x%y,(x:Double)=>x==0)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
  
    def pwr = Action { request =>
        request.body.asJson.map{ json =>
            handleJson(json,Double.NaN,(x:Double,y:Double)=>pow(x,y),(x:Double)=>false)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
    
    def home = Action { Ok(views.html.math()) }

}