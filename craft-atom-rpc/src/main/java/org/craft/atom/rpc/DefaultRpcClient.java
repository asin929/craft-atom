package org.craft.atom.rpc;

import java.net.InetSocketAddress;

import lombok.Getter;
import lombok.Setter;

import org.craft.atom.rpc.api.RpcClient;
import org.craft.atom.rpc.spi.RpcConnector;
import org.craft.atom.rpc.spi.RpcInvoker;
import org.craft.atom.rpc.spi.RpcProtocol;
import org.craft.atom.rpc.spi.RpcProxyFactory;

/**
 * @author mindwind
 * @version 1.0, Aug 14, 2014
 */
public class DefaultRpcClient implements RpcClient {

	
	@Getter @Setter private String          host                  ;
	@Getter @Setter private int             port                  ;
	@Getter @Setter private int             connections           ;
	@Getter @Setter private int             heartbeatInMillis     ;
	@Getter @Setter private int             connectTimeoutInMillis;
	@Getter @Setter private int             rpcTimeoutInMillis    ;
	@Getter @Setter private RpcConnector    connector             ; 
	@Getter @Setter private RpcProtocol     protocol              ;
	@Getter @Setter private RpcProxyFactory proxyFactory          ;
	@Getter @Setter private RpcInvoker      invoker               ;
	
	
	// ~ -------------------------------------------------------------------------------------------------------------
	
	
	public DefaultRpcClient() {
		connector              = new DefaultRpcConnector()    ;
		protocol               = new DefaultRpcProtocol()     ;
		proxyFactory           = new DefaultRpcProxyFactory() ;
		invoker                = new DefaultRpcClientInvoker();
		connectTimeoutInMillis = Integer.MAX_VALUE            ;
		rpcTimeoutInMillis     = Integer.MAX_VALUE            ;
		connections            = 1                            ;
		init();
	}
	
	public void init() {
		connector   .setProtocol(protocol);
		connector   .setAddress(new InetSocketAddress(host, port));
		connector   .setHeartbeatInMillis(heartbeatInMillis);
		connector   .setConnectTimeoutInMillis(connectTimeoutInMillis);
		invoker     .setConnector(connector);
		proxyFactory.setInvoker(invoker);
	}
	
	
	// ~ -------------------------------------------------------------------------------------------------------------
	
	
	@Override
	public <T> T refer(Class<T> rpcInterface) {
		return proxyFactory.getProxy(rpcInterface);
	}

	@Override
	public void open() throws RpcException {
		if (connections < 1) throw new IllegalStateException("Client connections configuration should >= 1");
		for (int i = 0; i < connections; i++) {
			connector.connect();
		}
	}

	@Override
	public void close() {
		connector.close();
	}

}
