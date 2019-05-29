package GAPY

import org.json.simple._ 
import scala.util.control.Exception.Catch
import JSON_Exceptions._

/**
 * @author Gwandalff
 * 
 * Small API to use with comfort the json simple API to read JSON file
 */
object JSONApi {
  
  // The current object we're pointing on when we pass through the JSON
  private var pointer : Object = null
  
  /**
   * parseJSONObject : parse the JSON string in parameter and set pointer at the root of the JSON
   * 
   * Same as parseJSONArray but we made this to make it easyer to read the type for the next get
   * 
   * @param json_string the JSON as a String value
   * @return {@link JSONApi} because we want this fluent
   */
  def parseJSONObject(json_string:String) = {
    pointer = JSONValue.parse(json_string)
    this
  }
  
  /**
   * parseJSONArray : parse the JSON string in parameter and set pointer at the root of the JSON
   * 
   * Same as parseJSONObject but we made this to make it easyer to read the type for the next get
   * 
   * @param json_string the JSON as a String value
   * @return {@link JSONApi} because we want this fluent
   */
  def parseJSONArray(json_string:String) = {
    pointer = JSONValue.parse(json_string)
    this
  }
  
  /**
   * getFromObject : do a get on the pointer as if it was a {@link JSONObject}
   * 
   * @param paramName the name of the object's parameter we want to read
   * @return {@link JSONApi} because we want this fluent
   * @throws JSONNotExisting if the last get returned a null to us
   * @throws JSONCastError if the pointer is not a {@link JSONObject}
   */
  def getFromObject(paramName:String) = {
    if(pointer == null){
      throw JSONNotExisting("The JSON Object you wanted was null")
    }
    try {
      pointer = pointer.asInstanceOf[JSONObject].get(paramName).asInstanceOf[Object]
    } catch {
      case _:ClassCastException => throw JSONCastError("You've tried to get from a JSONObject but that wasn't an object")
      case e:Throwable => throw e 
    }
    this
  }
  
  /**
   * getFromArray : do a get on the pointer as if it was a {@link JSONArray}
   * 
   * @param paramIndex the index of the arra's element we want to read
   * @return {@link JSONApi} because we want this fluent
   * @throws JSONNotExisting if the last get returned a null to us
   * @throws JSONCastError if the pointer is not a {@link JSONArray}
   */
  def getFromArray(paramIndex:Int) = {
    if(pointer == null){
      throw JSONNotExisting("The JSON Array you wanted was null")
    }
    try{
      pointer = pointer.asInstanceOf[JSONArray].get(paramName).asInstanceOf[Object]
    } catch {
      case _:ClassCastException => throw JSONCastError("You've tried to get from a JSONArray but that wasn't an array")
      case e:Throwable => throw e 
    }
    this
  }
  
  /**
   * value : get the value of the pointer at a specified type
   * 
   * @param T the type we want to cast then get the pointer
   * @return the pointer as instance of T
   */
  def value[T]() : T = {
    pointer.asInstanceOf[T]
  }
  
  /**
   * isNullPointer : check if the pointer is null
   * 
   * @return true if pointer is null else false
   */
  def isNullPointer() : Boolean = {
    pointer == null
  }
}