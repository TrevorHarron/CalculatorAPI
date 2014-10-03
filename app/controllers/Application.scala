package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object Application extends Controller {

  def index = Action {
     Ok(views.html.index("Your new application is ready."))
  }

  def ping = Action {
      Ok(Json.obj("result"->"OK"))
  }

}