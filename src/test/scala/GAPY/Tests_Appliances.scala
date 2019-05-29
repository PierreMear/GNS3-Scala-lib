package GAPY

import org.junit._
import Assert._
import scalaj.http._


@Test
class Test_Appliances {
    
    // Fonctions d'aides
    def returnServerAddress(): String = {
      return "http://148.60.11.201:3080"
    }
    
    def returnUrlAPI(): String = {
      return returnServerAddress() + "/v2/projects"
    }
    
    def checkProjectsAPI(): String = {
      val http = Http(returnUrlAPI())
      val response: HttpResponse[String] = http.asString
      
      return response.body
    }
    
    //TODO
}