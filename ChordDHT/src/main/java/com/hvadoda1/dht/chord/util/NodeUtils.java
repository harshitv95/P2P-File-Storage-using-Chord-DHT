package com.hvadoda1.dht.chord.util;

import static com.hvadoda1.dht.chord.util.StringUtils.Comparing.gt;
import static com.hvadoda1.dht.chord.util.StringUtils.Comparing.lt;
import static com.hvadoda1.dht.chord.util.StringUtils.Comparing.lteq;

import com.hvadoda1.dht.chord.rpc.connect.IChordNode;

public class NodeUtils {
	/**
	 * @param <Node>
	 * @param node   Node to check for being the predecessor
	 * @param id
	 * @param succ   Successor of Node
	 * @return
	 */
	public static <Node extends IChordNode> boolean isPredOf(Node node, String id, Node succ) {
		return isSuccOf(succ, id, node);
//		if (lt(succ.getId(), node.getId())) // Overflow
//			return lt(node.getId(), id) || lteq(id, succ.getId());
//		else
//			return lt(node.getId(), id) && lteq(id, succ.getId());
	}

	/**
	 * @param <Node>
	 * @param node   Node to check for being the successor
	 * @param id
	 * @param pred   Predecessor of Node
	 * @return
	 */
	public static <Node extends IChordNode> boolean isSuccOf(Node node, String id, Node pred) {
		if (gt(pred.getId(), node.getId()))
			return lt(pred.getId(), id) || lteq(id, node.getId());
		else if (lt(pred.getId(), node.getId()))
			return lt(pred.getId(), id) && lteq(id, node.getId());
		else
			return lt(id, node.getId());
	}

	public static String nodeAddress(IChordNode node) {
		return node.getIp() + ":" + node.getPort();
	}

}
