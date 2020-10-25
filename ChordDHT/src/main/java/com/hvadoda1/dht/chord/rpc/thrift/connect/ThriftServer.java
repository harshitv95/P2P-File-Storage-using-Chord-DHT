package com.hvadoda1.dht.chord.rpc.thrift.connect;

import java.util.List;

import com.hvadoda1.dht.chord.rpc.connect.IRpcServer;
import com.hvadoda1.dht.chord.rpc.thrift.generated.FileStore;
import com.hvadoda1.dht.chord.rpc.thrift.generated.NodeID;
import com.hvadoda1.dht.chord.rpc.thrift.generated.RFile;
import com.hvadoda1.dht.chord.rpc.thrift.generated.RFileMetadata;
import com.hvadoda1.dht.chord.rpc.thrift.generated.SystemException;

public class ThriftServer implements IRpcServer<RFile, RFileMetadata, NodeID, SystemException>, FileStore.Iface {

	@Override
	public void writeFile(RFile file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RFile readFile(String filename) throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFingertable(List<NodeID> node_list) throws SystemException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NodeID findSucc(String key) throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeID findPred(String key) throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NodeID getNodeSucc() throws SystemException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
