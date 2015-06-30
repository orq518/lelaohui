package com.llh.utils;

import javax.json.Json;

import com.sun.commontransfer.adroid.ResponseHandler;
import com.sun.commontransfer.adroid.TransferClientNetworkImpl;
import com.sun.commontransfer.adroid.TransferObject;
import com.tool.utils.LogTool;

public class BaesResponseHandler implements ResponseHandler {

	// 关闭
	@Override
	public void handleClosed() {
		TransferClientNetworkImpl.getInstance().stop();
	}

	// 连接
	@Override
	public void handleConnected() {
		String strSn = String.format("%d", System.currentTimeMillis());
		LogTool.i(strSn);
		// 创建一个业务包
		TransferObject business = new TransferObject();

		{
			business.head = Json
					.createObjectBuilder()
					.add("sn", strSn)
					.add("uid",
							TransferClientNetworkImpl.getInstance().getUid())
					.add("userdata", "").add("category", "echo")
					.add("action", "mytesting").build();

			business.body = Json.createObjectBuilder()
					.add("first parameter", "1001")
					.add("threadname", Thread.currentThread().getName())
					.build();
		}

		boolean ret = TransferClientNetworkImpl.getInstance()
				.sendTransferObject(business);
		if (ret)
			LogTool.d("send package successrull!");
		else
			LogTool.d("send package failed");

	}

	// 应答
	@Override
	public void handleResponse(TransferObject arg0) {

	}

}
