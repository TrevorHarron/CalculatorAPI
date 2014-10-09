package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object Application extends Controller {

  def index = Action {
     Ok(views.html.index("Welcome to the calculator Home!"))
  }

  def ping = Action {
      Ok(Json.obj("result"->"OK"))
  }

}