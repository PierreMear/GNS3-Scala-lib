package gapy.objectTypes

/**
 * @author Gwandalff
 *
 * Raw link case class that allow you to set all {@link Link} parameters
 */
case class RawLink(
    override val from        :Node,
    override val to          :Node,
    override val fromPort    :Int,
    override val toPort      :Int,
    override val fromAdapter :Int,
    override val toAdapter   :Int
    )
    extends Link(from,to,fromPort,toPort,fromAdapter,toAdapter)

/**
 * @author Gwandalff
 *
 * Simple link case class that allow you to bind the adapter to 0 when you just don't need this parameter
 */
case class SimpleLink(
    override val from        :Node,
    override val to          :Node,
    override val fromPort    :Int,
    override val toPort      :Int
    )
    extends Link(from,to,fromPort,toPort,0,0)
