package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Calculator extends Controller {
    
    private val operators = "[+-/*^%]".r
    
    implicit val rd = ((__ \ 'calculation).read[List[String]])
    
      def handleJson(json:JsValue) ={
        json.validate[(List[String])].map{
            case (calculation) => {
                var result = 0.0
                try {
                    Ok(Json.obj("result"-> 0.0))
                } catch {
                   case ex: IllegalArgumentException => {BadRequest(Json.obj("message"->ex.toString, "Status"->400))}
                   case ex: Exception => {InternalServerError(Json.obj("message"->"Unknown Error", "Status"->500))}
                }
            }
        }.recoverTotal {
            ex => BadRequest(JsError.toFlatJson(ex))
        }
    }
    
    private def calculate(o:List[String]):Double = {
        0.0
    }
    
    def rpn = Action { request => 
        request.body.asJson.map{ json =>
            handleJson(json)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
    
}