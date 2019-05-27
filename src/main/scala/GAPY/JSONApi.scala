package GAPY

import org.json.simple._ 
import scala.util.control.Exception.Catch
import JSON_Exceptions._

object JSONApi {
  
  private var pointer : Object = null
  
  def parseJSONObject(json_string:String) = {
    pointer = JSONValue.parse(json_string)
    this
  }
  
  def parseJSONArray(json_string:String) = {
    pointer = JSONValue.parse(json_string)
    this
  }
  
  def getFromObject(paramName:String) = {
    if(pointer == null){
      throw JSONNotExisting("The JSON Object you wanted was null")
    }
    try{
      pointer = pointer.asInstanceOf[JSONObject].get(paramName).asInstanceOf[Object]
    } catch {
      case _:ClassCastException => throw JSONCastError("You've tried to get from a JSONObject but that wasn't an object")
      case e:Throwable => throw e 
    }
    this
  }
  
  def getFromArray(paramName:Int) = {
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
  
  def value[T]() : T = {
    pointer.asInstanceOf[T]
  }
}