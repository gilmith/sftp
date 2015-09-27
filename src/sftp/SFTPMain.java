package sftp;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * Clase principal para el envio de ficheros via sftp por java
 * para ello tiene que pasarle dos ficheros el de origen y el de destino
 * Si el numero de parametros es menor que dos peta
 * Salidas:
 * -1 error de parametros, no se le han pasado el ficheor de origen y el de destino con la ruta absoluta de ambos
 *  1 error de conexion sftp ha habido un fallo en el sftp en algun punto
 *  2 error en el envio del fichero se ha podido mover antes de hacer el evnio. 
 *  Los datos del servidor de destino estan dados en un fichero de properties 
 * @author jake
 * 
 */

public class SFTPMain {
	
	private final static Logger logger = Logger.getLogger(SFTPMain.class);

	public static void main(String[] args) {
		
		PropertyConfigurator.configure("/home/jake/workspace/sftp/properties/log4j.properties");
		if (args.length < 2) {
			logger.error("Error en el paso de parametros, comprobar los parametros y volver a ejecutar");
			System.exit(-1);
		} else {
			try {
				SFTPClient SFTPCliente = new SFTPClient();
				SFTPCliente.enviaSFTP(args[0], args[1]);
				logger.info("ahora envio el segundo fichero, lo meto a mano");
				SFTPClient2 sftpClient2 = new SFTPClient2("192.168.56.101");
				sftpClient2.enviaSFTP("/tmp/fichero2.txt", "/tmp/fichero2.txt");
				sftpClient2.disconnect();
				logger.info("ahora el nuevo envio que tiene que ser tratado desde el"
						+ "sistema llamante");
				SFTPClient3 sftpClient3 = new SFTPClient3("192.168.56.101");
				try {
					logger.info("pueba que va a funcionar, al /tmp siempre puede enviar");
					sftpClient3.enviaSFTP("/tmp/fichero3.txt", "/tmp/fichero3.txt");
					 logger.info("pueba que no va a funcinoar enviar a la carpeta de otro usuario");
					 sftpClient3.enviaSFTP("/tmp/fichero3.txt", "/home/pepi/fichero3.txt");
				} catch (SftpException e) {
					logger.info("excepcion capturada", e);
					sftpClient3.disconnect();
					logger.info("codigo recuperacion de la excepcion");
					SFTPClient3 sftpClient32 = new SFTPClient3("192.168.56.101", "pepi");
					try {
						sftpClient32.enviaSFTP("/tmp/fichero3.txt", "/home/pepi/fichero3.txt");
						sftpClient32.disconnect();
					} catch (SftpException e1) {
						logger.error("exception irrecuperable", e1);
					}
				}
			} catch (IOException | JSchException e) {
				logger.error("no se encuentra el archivo de known_hosts RC 1" + e );
				System.exit(1);
			}

			
		}
		
		
	}

}
