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
 * @version 3 cambio para que el envio devuelva la SftpException y pueda ser tratada 
 * desde el main
 *
 */
public class SFTPClient3 extends SFTPAbstract {
	
	private ChannelSftp canalSftp;
	private final static Logger logger = Logger.getLogger(SFTPClient3.class);

	public SFTPClient3(String isp) throws JSchException {
		super(isp);
		connect();
		canalSftp = super.getChannelSftp();
	}
	
	public SFTPClient3(String isp, String usuario) throws JSchException {
		super(isp, usuario);
		connect();
		canalSftp = super.getChannelSftp();
	}
	
	private void connect(){
		super.connnect();
	}
	
	public void disconnect(){
		super.disconnect();
	}
	
	public void enviaSFTP(String rutaOrigen, String rutaDestino) throws SftpException{
		if(canalSftp.isConnected()){
			logger.info("esta conectado al servidor externo");
		}
		canalSftp.put(rutaOrigen, rutaDestino);
		canalSftp.disconnect();
		
	}

}
