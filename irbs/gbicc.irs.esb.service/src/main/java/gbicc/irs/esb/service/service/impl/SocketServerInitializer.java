package gbicc.irs.esb.service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wsp.framework.core.service.support.ApplicationInitializer;

import gbicc.irs.esb.service.service.Server;

@Component
public class SocketServerInitializer implements ApplicationInitializer{
	private static final Logger log =LoggerFactory.getLogger(SocketServerInitializer.class);
	
	@Override
	public String getId() {
		return this.getClass().getName();
	}

	@Override
	public String getName() {
		return "Auto Start ESB Socket Server Initializer";
	}

	@Override
	public String getDescription() {
		return "Auto Start ESB Socket Server Initializer";
	}

	@Override
	public int getOrder() {
		return 100;
	}

	@Override
	public boolean isInitialized() {
		return false;
	}

	@Override
	public void execute() throws Exception {
		log.info("开始初始化Socket服务...");
	    new Thread(new Runnable() {
			@Override
			public void run() {
				Server server = new Server();
				try {
					server.doListen();
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();;
		log.info("初始化Socket服务结束");
	}
}
