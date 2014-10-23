package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Calculator extends Controller {
    
    
    implicit val rd = ((__ \ 'calculation).read[List[String]])
    
    def rpn = TODO
    
}