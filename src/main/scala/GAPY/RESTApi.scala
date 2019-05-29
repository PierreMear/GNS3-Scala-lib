package GAPY

import scalaj.http._
import org.json.simple._

/**
 * REST API request object
 * Use this object to make easier HTTP requests
 *
 * @author Gwandalff
 */
object RESTApi {

  /**
   * get : do a GET http request on a server at a specific URL
   *
   * @param url the URL of the page on the server
   * @param serverAddress the server address(with port ex: 127.0.0.1:8080)
   * @return the body of the page requested
   */
  def get(url: String, serverAddress:String, user:String, pass:String): String = {
    var http = null
    if(user == "" && pass == ""){
      http = Http(serverAddress + url)
    }else{
      http = Http(serverAddress + url).headers(("Authorization: Basic",user+ ":" + pass))
    }
    val response: HttpResponse[String] = http.asString
    return response.body
  }

  /**
   * post : do a POST http request on a server at a specific URL
   *
   * @param url the URL of the page on the server
   * @param body the data to send(often in JSON)
   * @param serverAddress the server address(with port ex: 127.0.0.1:8080)
   * @return the body of the page requested
   */
  def post(url: String, body: String, serverAddress:String, user:String, pass:String): String = {
    var http = null
    if (user == "" && pass == ""){
      http = Http(serverAddress + url)
    }else{
      http = Http(serverAddress + url).headers(("Authorization: Basic",user+ ":" + pass))
    }
    val response: HttpResponse[String] = http.postData(body).asString
    return response.body
  }

  /**
   * delete : do a DELETE http request on a server at a specific URL
   *
   * @param url the URL of the page on the server
   * @param serverAddress the server address(with port ex: 127.0.0.1:8080)
   * @return the body of the page requested
   */
  def delete(url: String, serverAddress:String, user:String, pass:String): String = {
    var http = null
    if (user == "" && pass == ""){
      http = Http(serverAddress + url)
    }else{
      http = Http(serverAddress + url).headers(("Authorization: Basic",user + ":" + pass))
    }
    val response: HttpResponse[String] = http.method("delete").asString
    return response.body
  }

  /**
   * put : do a PUT http request on a server at a specific URL
   *
   * @param url the URL of the page on the server
   * @param body the data to send(often in JSON)
   * @param serverAddress the server address(with port ex: 127.0.0.1:8080)
   * @return the body of the page requested
   */
  def put(url: String, body:String, serverAddress:String, user:String, pass:String): String = {
    var http= null
    if(user == "" && pass == ""){
      http = Http(serverAddress + url)
    }else{
      http = Http(serverAddress + url).headers(("Authorization: Basic",user+ ":" + pass))
    }
    val response: HttpResponse[String] = http.postData(body).method("put").asString
    return response.body
  }
}
