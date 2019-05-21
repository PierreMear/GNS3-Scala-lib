package objectTypes

case class RawLink(
    override val from        :Node, 
    override val to          :Node, 
    override val fromPort    :Int, 
    override val toPort      :Int, 
    override val fromAdapter :Int, 
    override val toAdapter   :Int
    ) 
    extends Link(from,to,fromPort,toPort,fromAdapter,toAdapter)


case class SimpleLink(
    override val from        :Node, 
    override val to          :Node, 
    override val fromPort    :Int, 
    override val toPort      :Int
    ) 
    extends Link(from,to,fromPort,toPort,0,0) 