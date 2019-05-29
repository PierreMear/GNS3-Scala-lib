package GAPY

import org.junit._
import Assert._
import scalaj.http._
import org.json.simple._ 

@Test
class Tests_Basic_Nodes {
  
    ////// Fonctions d'aide
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
    
    //Test si les nodes et liens sont bien créées, avec le bon nom et le bon type
    @Test
    def testProjectAndCNodesAndLinks() = {
      val projNodeTest = new GNS3_Manager(returnServerAddress())
      val p = projNodeTest.createProject("projNode")
      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projNode' should have been created")

      //On créé les objets Nodes que l'on va ajouter au projet
      val one = objectTypes.LocalHub("Hyrule")
      val two = objectTypes.LocalVpcs("Ganon")
      val three = objectTypes.LocalVpcs("Zelda")
      val four = objectTypes.LocalVpcs("Link")
      p.addNode(one)
      p.addNode(two)
      p.addNode(three)
      p.addNode(four)

      //On récupère les données sur les nodes du projet

      var returned = checkProjectsAPI("/" + proj_id + "/nodes" )

      //On récupère les données des nodes via l'API Basiques de GNS3
      val obj_hyrule = JSONApi.parseJSONArray(returned).getFromArray(0).getFromObject("name").value[String]
      val obj_ganon = JSONApi.parseJSONArray(returned).getFromArray(1).getFromObject("name").value[String]
      val obj_zelda = JSONApi.parseJSONArray(returned).getFromArray(2).getFromObject("name").value[String]
      val obj_link = JSONApi.parseJSONArray(returned).getFromArray(3).getFromObject("name").value[String]

      val id_hyrule = JSONApi.parseJSONArray(returned).getFromArray(0).getFromObject("node_id").value[String]
      val id_ganon = JSONApi.parseJSONArray(returned).getFromArray(1).getFromObject("node_id").value[String]
      val id_zelda = JSONApi.parseJSONArray(returned).getFromArray(2).getFromObject("node_id").value[String]
      val id_link = JSONApi.parseJSONArray(returned).getFromArray(3).getFromObject("node_id").value[String]

      val link_one = objectTypes.SimpleLink(one, two, 0,0)
      val link_two = objectTypes.SimpleLink(one, three, 1,0)
      val link_three = objectTypes.SimpleLink(one, four, 2,0)

      try{
        p.addLink(link_one)
        p.addLink(link_two).addLink(link_three)
      } catch {
        case ex: Exception => throw new Exception("Error : \n" + ex.printStackTrace()) 
      }

      returned = checkProjectsAPI("/" + proj_id + "/links" )
      val obj = JSONApi.parseJSONArray(returned).value[JSONArray]
      println(obj.toJSONString()) //On print les données pour observer la structure du document renvoyé

      // On test si la node Hube Hyrule est bien au centre du layout avec les non bis
      val resultsLinks_one =     JSONApi.parseJSONArray(returned).getFromArray(0).getFromObject("nodes").getFromArray(0).getFromObject("node_id").value[String]
      val resultsLinks_one_bis = JSONApi.parseJSONArray(returned).getFromArray(0).getFromObject("nodes").getFromArray(1).getFromObject("node_id").value[String]
      val resultsLinks_two =     JSONApi.parseJSONArray(returned).getFromArray(1).getFromObject("nodes").getFromArray(0).getFromObject("node_id").value[String]
      val resultsLinks_two_bis = JSONApi.parseJSONArray(returned).getFromArray(1).getFromObject("nodes").getFromArray(1).getFromObject("node_id").value[String]
      val resultsLinks_thr =     JSONApi.parseJSONArray(returned).getFromArray(2).getFromObject("nodes").getFromArray(0).getFromObject("node_id").value[String]
      val resultsLinks_thr_bis = JSONApi.parseJSONArray(returned).getFromArray(2).getFromObject("nodes").getFromArray(1).getFromObject("node_id").value[String]
      assert(resultsLinks_one == id_hyrule, "Link detected but not the one we were waiting for")
      assert(resultsLinks_two == id_hyrule, "Link detected but not the one we were waiting for")
      assert(resultsLinks_thr == id_hyrule, "Link detected but not the one we were waiting for")

      //On test si le hub est bien connecté aux autres
      assert(resultsLinks_one_bis == id_ganon, "Link detected but not the one we were waiting for")
      assert(resultsLinks_two_bis == id_zelda, "Link detected but not the one we were waiting for")
      assert(resultsLinks_thr_bis == id_link, "Link detected but not the one we were waiting for")
      
      projNodeTest.deleteProject(proj_id) 
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projNode' should have been destroyed")
      assert(obj_hyrule == "Hyrule", "The project should have a Node nammed Hyrule, and it's not there. " + obj_hyrule)
      assert(obj_ganon == "Ganon", "The project should have a Node nammed Ganon, and it's not there. " + obj_ganon) 
      assert(obj_zelda == "Zelda", "The project should have a Node nammed Zelda, and it's not there. " + obj_zelda) 
      assert(obj_link == "Link", "The project should have a Node nammed Link, and it's not there. " + obj_link)             
    }

    @Test
    def testProjectFalseLink() = {
      val projNodeTest = new GNS3_Manager(returnServerAddress())
      val p = projNodeTest.createProject("projFalseLink")
      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projNode' should have been created")

      val unique = objectTypes.LocalSwitch("Galatron")
      val two = objectTypes.LocalSwitch("RNG")
      p.addNode(unique)

      // On test que l'ajout d'un lien impossible rend bien la bonne erreur
      var good_error = False
      try {
        val link_unique = objectTypes.SimpleLink(unique, two, 0,0)
        p.addLink(link_unique)
      } catch {
        case ext: NodeNotFoundException => good_error = True
        case exc: Exception => throw new Exception("Error : \n" + ex.printStackTrace()) 
      }
      assert(good_error, "The code should have raised a NodeNotFoundException ! We created a link with a non-existing node")

      // On test dans l'autre sens
      good_error = False
      try {
        val link_unique_two = objectTypes.SimpleLink(two, unique, 0,0)
        p.addLink(link_unique_two)
      } catch {
        case ext: NodeNotFoundException => good_error = True
        case exc: Exception => throw new Exception("Error : \n" + ex.printStackTrace()) 
      }
      assert(good_error, "The code should have raised a NodeNotFoundException ! We created a link with a non-existing node")

      projNodeTest.deleteProject(proj_id) 
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projNode' should have been destroyed")
    }
}