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
      
      def reverse:Link = RawLink(this.to,this.from,this.toPort,this.fromPort,this.toAdapter,this.fromAdapter)
    }