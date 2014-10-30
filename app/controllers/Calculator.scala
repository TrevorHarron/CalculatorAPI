package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.math.pow

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
            val num = numbers findFirstIn li.head
            val op = operators findFirstIn li.head
            if(li.isEmpty) acc
            else if(num != None){
                val i = li.head.toInt
                calcAcc(li.tail,i::stack,acc)
            } else if(op != None){ 
                op.get match {
                    case "+" => calcAcc(li.tail, Nil,Math.baseFunction(acc,stack,(x:Double,y:Double)=>x+y,(x:Double)=>false))
                    case "-" => calcAcc(li.tail, Nil,Math.baseFunction(acc,stack,(x:Double,y:Double)=>x-y,(x:Double)=>false))
                    case "*" => calcAcc(li.tail, Nil,Math.baseFunction(acc,stack,(x:Double,y:Double)=>x*y,(x:Double)=>false))
                    case "/" => calcAcc(li.tail, Nil,Math.baseFunction(acc,stack,(x:Double,y:Double)=>x/y,(x:Double)=>x==0))
                    case "%" => calcAcc(li.tail, Nil,Math.baseFunction(acc,stack,(x:Double,y:Double)=>x%y,(x:Double)=>x==0))
                    case "^" => calcAcc(li.tail, Nil,Math.baseFunction(acc,stack,(x:Double,y:Double)=>pow(x,y),(x:Double)=>false))
                }
                
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