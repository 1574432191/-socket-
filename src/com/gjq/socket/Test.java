package com.gjq.socket;
/*
*该程序的启动是先启动Test里面的主函数，开启服务端
*然后再在dos窗口输入telnet localhost 端口号 连接到服务端
*输入用户名和密码就能够实现通信
*/
public class Test {
	public static void main(String[] args) {
		MyServer mServer = new MyServer();
		mServer.initServer();
	}

}
