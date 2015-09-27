package sftp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * Clase objeto para el envio del sftp 
 * lanzara excepciones del tipo JSCHexception si ha habido algun problema de conexion 
 * o IOException si no tiene el ficheroq ue se va a enviar. 
 * @author jake
 *
 */

public class SFTPClient {
	
	private JSch jschObj;
	private Session sesion = null;
	private Channel canal;
	private ChannelSftp canalsftp;
	private Properties properties;
	private final static Logger logger = Logger.getLogger(SFTPClient.class);
	
	public SFTPClient() throws IOException, JSchException{
		properties = new Properties();
		FileInputStream input = new FileInputStream("/home/jake/workspace/sftp/properties/FTP.properties");
		properties.load(input);
		jschObj = new JSch();
		jschObj.setKnownHosts("/home/jake/.ssh/known_hosts");
		try {
			setConfig();
		} catch (NumberFormatException | JSchException e) {
			logger.error("error de en la apertura del canal sftp ", e);
			System.exit(2);
		}
	}
	
	/**
	 * Metodo para enviar el fichero, para ello tiene que tener abierta una 
	 * sesion contra el servidor de destino y un canal sftp que este conectado
	 * la sesion se setea como NoStrictedKey y el knowhost tiene que estar tambien 
	 * seteado
	 * @param rutaOrigen del fichero
	 * @param rutaDestino del fichero en el servidor 
	 */
	
	public void enviaSFTP(String rutaOrigen, String rutaDestino){
		try {
			canalsftp.connect();
			if (canalsftp.isConnected()){
				logger.info("ha conectado por el canal de sftp");
			}
			canalsftp.put(rutaOrigen, rutaDestino);
			logger.info("fichero enviado");
			canalsftp.disconnect();
			canal.disconnect();
			sesion.disconnect();
			logger.info("sesion desconectada");
		} catch (SftpException | JSchException e) {
			logger.error("error en el envio del fichero " + rutaOrigen + " reintentar ", e);
			System.exit(3);
		}
	}

	private void setConfig() throws NumberFormatException, JSchException {
		sesion = jschObj.getSession(properties.getProperty("USUARIO"), properties.getProperty("SERVIDOR"), 
				Integer.valueOf(properties.getProperty("PUERTO")));
		sesion.setConfig("StrinctHostKeyChecking", "no");
		sesion.setPassword(properties.getProperty("PASSWORD"));
		sesion.connect();
		canal = sesion.openChannel("sftp");
		canalsftp = (ChannelSftp) canal;
	}

}
