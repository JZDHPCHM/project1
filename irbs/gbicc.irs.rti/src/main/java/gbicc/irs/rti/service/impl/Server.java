package gbicc.irs.rti.service.impl;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Server {
	
	private static final int PORT = 8891;
	private static final int POOL_SIZE = 50;
	
	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(POOL_SIZE);;
	
	private static Log log = LogFactory.getLog(Server.class);
	
	public void doListen() throws Exception {
		log.debug("Strat ESB service listener @ port: " + PORT);
		
		Socket socket = null;
		ServerSocket serverSocket = new ServerSocket(PORT);
		while(true) {
			socket = serverSocket.accept();
			InetAddress inetAddress = socket.getInetAddress();
			log.debug("Connected to client at " + inetAddress);
			
			executor.execute(new EsbServiceHandler(socket));
		}
	}
	
}

