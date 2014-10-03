package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object Application extends Controller {

  def index = Action {
    Redirect(routes.Application.ping)
  }

  def ping = Action {
      Ok(Json.obj("result"->"OK"))
  }

}