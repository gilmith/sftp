/**
 * 
 */
package sftp;

import org.apache.log4j.Logger;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * @author jake
 *
 */
public class SFTPClient2 extends SFTPAbstract {
	
	private ChannelSftp canalSftp;
	private final static Logger logger = Logger.getLogger(SFTPClient2.class);

	public SFTPClient2(String isp) throws JSchException {
		super(isp);
		connect();
		canalSftp = super.getChannelSftp();
	}
	
	private void connect(){
		super.connnect();
	}
	
	public void disconnect(){
		super.disconnect();
	}
	
	public void enviaSFTP(String rutaOrigen, String rutaDestino){
		try {
			if (canalSftp.isConnected()){
				logger.info("ha conectado por el canal de sftp");
			}
			canalSftp.put(rutaOrigen, rutaDestino);
			logger.info("fichero enviado");
			canalSftp.disconnect();
			logger.info("canalSftp desconectado");
		} catch (SftpException e) {
			logger.error("error en el envio del fichero " + rutaOrigen + " reintentar ", e);
			System.exit(3);
		}
	}

}
