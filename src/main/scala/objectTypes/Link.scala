package objectTypes

abstract class Link(
    val from        :Node, 
    val to          :Node, 
    val fromPort    :Int, 
    val toPort      :Int, 
    val fromAdapter :Int, 
    val toAdapter   :Int) {
  
      override def toString:String = {
        "["+fromAdapter+"\\"+from.toString()+":"+fromPort+"|----- Linked to -----|"+toPort+":"+to.toString()+"/"+toAdapter+"]"
      }
    }