/**
 * 
 */
package sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Clase abstracta para comprobar la herencia para el sftp
 * @author jake
 *
 */
public abstract class SFTPAbstract {
	
	private JSch jschObj;
	private Session session;
	private Channel channel;
	private ChannelSftp channelSftp;
	private Properties properties;
	private Logger logger = Logger.getLogger(SFTPAbstract.class);
	
	public SFTPAbstract(String isp) throws JSchException{
		jschObj = new JSch();
		jschObj.setKnownHosts("/home/jake/.ssh/known_hosts");
		properties = new Properties();
		try{
			properties.load(new FileInputStream(new File("properties/FTP.properties")));
		} catch(IOException e){
			logger.error("no se ha leido el fichero de properties ", e);
		}
		session = jschObj.getSession(properties.getProperty("USUARIO"), isp.toUpperCase(), Integer.valueOf(properties.getProperty("PUERTO")));
		session.setPassword(properties.getProperty("PASSWORD"));
		session.setConfig("StrinctHostKeyChecking","no");
		
	}
	
	public SFTPAbstract(String isp, String usuario) throws JSchException{
		jschObj = new JSch();
		jschObj.setKnownHosts("/home/jake/.ssh/known_hosts");
		properties = new Properties();
		try{
			properties.load(new FileInputStream(new File("properties/FTP.properties")));
		} catch(IOException e){
			logger.error("no se ha leido el fichero de properties ", e);
		}
		session = jschObj.getSession(usuario, isp.toUpperCase(), Integer.valueOf(properties.getProperty("PUERTO")));
		session.setPassword("nillo");
		session.setConfig("StrinctHostKeyChecking","no");
		
	}
	
	public void connnect(){
		try {
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			logger.info("se ha iniciado la sesion");
		} catch (JSchException e) {
			logger.error("error al iniciar la sesion", e);
		}
		logger.info("conectado al servidor externo");
	}
	
	public void disconnect(){
		channel.disconnect();
		session.disconnect();
		logger.info("desconectado");
	}
	
	public ChannelSftp getChannelSftp(){
		channelSftp = (ChannelSftp) channel;
		return channelSftp;
	}
	

}
