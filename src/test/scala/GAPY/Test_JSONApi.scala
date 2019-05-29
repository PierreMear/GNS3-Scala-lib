package GAPY

import org.junit._
import Assert._
import scalaj.http._
import org.json.simple._ 


@Test
class Test_JSONApi {

    // Fonctions d'aides
    def returnServerAddress(): String = {
      return "http://148.60.11.201:3080"
    }
    
    def returnUrlAPI(): String = {
      return returnServerAddress() + "/v2/projects"
    }
    
    def checkProjectsAPI(added:String): String = {
      val http = Http(returnUrlAPI() + added)
      val response: HttpResponse[String] = http.asString
      
      return response.body
    }

    @Test
    def JSONError() = {

        val returned = checkProjectsAPI("https://jsonplaceholder.typicode.com/todos")

        var should = false
        try {
            val obj_fail = JSONApi.parseJSONArray.getFromObject("id")
        } catch {
            case ev: JSON_Exceptions.JSONCastError => should = true
            case ex: Exception => println("Erreur " + ex.printStackTrace())
        }

        assert(should, "Should had JSONCASTERROR")

        should = false
        try {
            val obj_fail_2 = JSONApi.getFromObject("id")
       } catch {
            case ev: JSON_Exceptions.JSONNotExisting => should = true
            case ex: Exception => println("Erreur " + ex.printStackTrace())
        }

        assert(should, "Should had JSONNotExistingError")
    }
}