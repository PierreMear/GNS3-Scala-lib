package GAPY

import org.junit._
import Assert._
import scalaj.http._
import org.json.simple._ 

import objectTypes._

//TODO dans tout lee fichier remplacer les nodes par des appliances avec les tests que vont bien, ce fichier n'est pour l'instant qu'un template
//TODO une fois les tests faits, ne pas oublier de décommenter les //@Test en face des fonctions de test

case class Alpine(override val name:String) extends Appliance("Alpine Linux",name,"docker","local")


@Test
class Test_Appliances {
    
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
    
    //Test si les nodes et liens sont bien créées, avec le bon nom et le bon type
    //@Test
    def testProjectAndAppliancesAndLinks() = {
      val projNodeTest = new GNS3_Manager(returnServerAddress())
      val p = projNodeTest.createProject("projAppliances")
      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projAppliances' should have been created")

      //On créé les objets Appliance que l'on va ajouter au projet
      p.addNode(Alpine("Link"))
      p.addNode(Alpine("Zelda"))
      p.addNode(Alpine("Ganon"))
      p.addNode(Alpine("Sheik"))

      //On récupère les données sur les nodes du projet

      var returned = checkProjectsAPI("/" + proj_id + "/nodes" )

      //On récupère les données des nodes via l'API Basiques de GNS3
      val id_link = JSONApi.parseJSONArray(returned).getFromArray(0).getFromObject("node_id").value[String]
      val id_zelda = JSONApi.parseJSONArray(returned).getFromArray(1).getFromObject("node_id").value[String]
      val id_ganon = JSONApi.parseJSONArray(returned).getFromArray(2).getFromObject("node_id").value[String]
      val id_sheik = JSONApi.parseJSONArray(returned).getFromArray(3).getFromObject("node_id").value[String]

      val link_one = objectTypes.SimpleLink(Alpine("Link"), Alpine("Ganon"), 0,0)
      val link_two = objectTypes.SimpleLink(Alpine("Zelda"), Alpine("Sheik"), 0,0)

      try{
        p.addLink(link_one)
        p.addLink(link_two)
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
      
      assert(resultsLinks_one == id_link, "Link detected but not the one we were waiting for")
      assert(resultsLinks_two == id_zelda, "Link detected but not the one we were waiting for")
      assert(resultsLinks_one_bis == id_ganon, "Link detected but not the one we were waiting for")
      assert(resultsLinks_two_bis == id_sheik, "Link detected but not the one we were waiting for")
      
      projNodeTest.deleteProject(proj_id) 
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projAppliance' should have been destroyed")             
    }

    //@Test
    def testProjectFalseLink() = {
      val projNodeTest = new GNS3_Manager(returnServerAddress())
      val p = projNodeTest.createProject("projFalseLink")
      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projNode' should have been created")

      val unique = Alpine("Galatron")
      val two = Alpine("RNG")
      p.addNode(unique)

      // On test que l'ajout d'un lien impossible rend bien la bonne erreur
      var good_error = false
      try {
        val link_unique = objectTypes.SimpleLink(unique, two, 0,0)
        p.addLink(link_unique)
      } catch {
        case ext: GAPY.GNS3_Exceptions.NodeNotFoundException => good_error = true
        case exc: Exception => throw new Exception("Error : \n" + exc.printStackTrace()) 
      }
      assert(good_error, "The code should have raised a NodeNotFoundException ! We created a link with a non-existing node")

      // On test dans l'autre sens
      good_error = false
      try {
        val link_unique_two = objectTypes.SimpleLink(two, unique, 0,0)
        p.addLink(link_unique_two)
      } catch {
        case ext: GAPY.GNS3_Exceptions.NodeNotFoundException => good_error = true
        case exc: Exception => throw new Exception("Error : \n" + exc.printStackTrace()) 
      }
      assert(good_error, "The code should have raised a NodeNotFoundException ! We created a link with a non-existing node")

      projNodeTest.deleteProject(proj_id) 
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projNode' should have been destroyed")
    }
    
    // TODO: make secure the SSH test
    def testApplianceWithConfig() = {

      // On recup la config, qui doit être de la forme config_test_ssh.json avec dedans un objet JSON avec les champs user et pass
      val stream = new FileInputStream("./config_test_ssh.json")
      val json_data = try {  Json.parse(stream) } finally { stream.close() }
      val creds_user = JSONApi.parseJSONObject(json_data).getFromObject("user")
      val creds_pass = JSONApi.parseJSONObject(json_data).getFromObject("pass")
      println(creds_user)
      val projNodeTest = new GNS3_Manager(returnServerAddress()).enableSSH("148.60.11.201", creds_user, creds_pass)
      val p = projNodeTest.createProject("projConfig")

      var check = checkProjectsAPI("")
      val proj_id = p.ProjectId
      assert(check != "[]", "The project 'projConfig' should have been created")

      val alpineTurbo = Alpine("Turbo")
      p.addNode(alpineTurbo,"./interfaces")
      
      val node_id = JSONApi.parseJSONArray(checkProjectsAPI("/"+proj_id+"/nodes")).getFromArray(0).getFromObject("node_id").value[String]
      val remotePath:String = "/opt/gns3/projects/%s/project-files/docker/%s/etc/network/interfaces".format(proj_id,node_id)



      val cat = SSHApi.cat(remotePath, SSH_Config("148.60.11.201", creds_user, creds_pass))

      projNodeTest.deleteProject(proj_id) 
      check = checkProjectsAPI("")
      assert(check == "[]", "The project 'projNode' should have been destroyed")
    }
}