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
  
  def get(url: String, serverAddress:String): String = {
    var http = Http(serverAddress + url)
    val response: HttpResponse[String] = http.asString
    return response.body
  }

  def post(url: String, body: String, serverAddress:String): String = {
    var http = Http(serverAddress + url)
    val response: HttpResponse[String] = http.postData(body).asString
    return response.body
  }
  
  def delete(url: String, serverAddress:String): String = {
    var http = Http(serverAddress + url)
    val response: HttpResponse[String] = http.method("delete").asString
    return response.body
  }
  
  def put(url: String, body:String, serverAddress:String): String = {
    var http = Http(serverAddress + url)
    val response: HttpResponse[String] = http.postData(body).method("put").asString
    return response.body
  }
}