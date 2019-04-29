package GAPY 

import scalaj.http._

class ProjectManager(var ProjectId: String, var serverAddress:String) {
    def addNode(): ProjectManager = {
      this
    }
    
    def RESTCall(url:String, method:String, body:Seq[(String, String)]): Unit = {
        var http = Http(serverAddress + url)
        method match {
            case "GET" => {
              val response: HttpResponse[String] = http.asString
            }
            case "POST" => {
              val response: HttpResponse[String] = http.postForm(body).asString
            }
        }
    }
    
    
}