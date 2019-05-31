package GAPY

import com.decodified.scalassh._

case class SSH_Config(host: String, user: String, password: String)

/**
 * SSH API connection object<br>
 * Use this object to make easily SSH commands 
 *
 * @author Gwandalff
 */
object SSHApi {
  
  /**
   * upload : upload a file from your local computer on a remote computer
   * 
   * Used for sending the configuration files to the GNS3 Server via SSH
   * 
   * @param localPath the path of the file on the local computer
   * @param remotePath the path of the copy on the remote computer
   * @param config the SSH necessary information like host, user, pass
   */
  def upload(localPath: String, remotePath: String, config:SSH_Config) {
    SSH(config.host,HostConfig(PasswordLogin(config.user, SimplePasswordProducer(config.password)))) { client =>
      for {
        result  <- client.upload(localPath, remotePath)
      } yield result
    }
  }
}