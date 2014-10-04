package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object Math extends Controller {
    
  private val defaultMSG = 
    NotImplemented(Json.obj("message"->"sorry this is not ready", "Status"->501))
  private def mapReduce = TODO
  
  def home = Action{
      defaultMSG
  }

  def add = Action{
      defaultMSG
  }
  
  def sub = Action{
      defaultMSG
  }
  
  def mul = Action{
      defaultMSG
  }
  
  def div = Action{
      defaultMSG
  }
  
  def mod = Action{
      defaultMSG
  }
  
  def pwr = Action{
      defaultMSG
  }

}