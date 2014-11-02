package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Documentation extends Controller {
    
    def doc(action:String) = Action {Ok(views.html.math_doc(action))}
    
    //THis will detail the math controller on the whole
    def math = TODO
    
    //Detail the different calcualtor types
    def calculator = TODO
}