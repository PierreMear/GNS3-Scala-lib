package GAPY

import org.junit._
import Assert._
import scalaj.http._
import org.json.simple._ 

@Test
class Tests_Topology {
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

    //TODO
    @Test
    def testFullyConnected() = {
      val projFC = new GNS3_Manager(returnServerAddress())
      val p = projFC.createProject("projFC")
      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projFC' should have been created")

      p.addTopology(new topologies.FullyConnectedNetwork(List(objectTypes.LocalHub("PC1"),objectTypes.LocalHub("PC2"),objectTypes.LocalHub("PC3"),objectTypes.LocalHub("PC4"),objectTypes.LocalHub("PC5"),objectTypes.LocalHub("PC6"))))
      
      var res_nodes = ""
      var res_links = ""
      val returned_nodes = checkProjectsAPI("/" + proj_id + "/nodes" )
      val returned_links = checkProjectsAPI("/" + proj_id + "/links" )
      var boolean_error = false
      try {

        val returned_3 = checkProjectsAPI("/" + proj_id + "/links" )
        val obj = JSONApi.parseJSONArray(returned_3).value[JSONArray]
        println(obj.toJSONString())
        res_nodes = JSONApi.parseJSONArray(returned_nodes).getFromArray(5).getFromObject("name").value[String]
        res_links = JSONApi.parseJSONArray(returned_links).getFromArray( ((6 * 5) / 2) - 1 ).getFromObject("link_id").value[String]
       
      } catch {
        case ex: Exception => print("Error : " + ex.printStackTrace()) ; boolean_error = true
      }
      projFC.deleteProject(proj_id) 
      assert(res_nodes == "PC6", "The last node created should have been PC6 and not " + res_nodes)
      assert(res_links != null, "The last link created should have the (n*n-1)/2 th but it doesn't exist, res = " + res_links)

      
      assert(!boolean_error, "A error happened, look earlier in the logs")
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projFC' should have been destroyed")  


    }
}