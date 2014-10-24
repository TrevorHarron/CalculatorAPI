package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Calculator extends Controller {
    
    private val operators = "[+-/*^%]".r
    private val numbers = "[0-9]".r
    
    implicit val rd = ((__ \ 'calculation).read[List[String]])
    
      def handleJson(json:JsValue) ={
        json.validate[(List[String])].map{
            case (calculation) => {
                var result = 0.0
                try {
                    val result =  calculate(calculation)
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
    
    private def calculate(l:List[String]):Double = {
        def calcAcc(li:List[String],stack: List[Double],acc:Double):Double = {
            if(li.isEmpty) acc
            else if(!(numbers findAllIn li.head isEmpty)){
                val i = li.head.toInt
                calcAcc(li.tail,i::stack,acc)
            } else if(!(operators findAllIn li.head isEmpty)){ 
                0.0//placeholder for matching syntax
            } else {
                throw new Exception("Bad operation "+li.head)
            }
            
        }
        calcAcc(l,List[Double](),0.0)
    }
    
    def rpn = Action { request => 
        request.body.asJson.map{ json =>
            handleJson(json)
        }.getOrElse {
            BadRequest("Expecting Json data")
        }
    }
    
}