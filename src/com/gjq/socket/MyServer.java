package com.gjq.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/*
 * 连接服务器类
 */
public class MyServer {
	public static ArrayList<ServerThread>list =
			new ArrayList<ServerThread>();
	
	public void initServer(){
		
				try {
					//创建服务器，并指定端口号
					//ServerSocket对象，表示要在那个端口号上进行通信
					ServerSocket serverSocket = new ServerSocket(8080);
					System.out.println("服务端已经建立");
					//不断获取客户端的连接
					while(true){
						//服务器调用serverSocket类的accept()方法，此方法等待，直到客户端连接到给定端口上的服务器
						//客户端实例化Socket对象，该类的构造函数尝试将客户端连接到指定的服务器和端口号
						//如果建立通信，则客户端现在具有能够与服务器通信的socket对象
						Socket socket = serverSocket.accept();
						System.out.println("客户端连接进来");
						//当有客户端连接进来以后，开启一个线程，用来处理该客户端的逻辑
						ServerThread serverThread = new ServerThread(socket);
						serverThread.start();
						//添加该客户端到容器中
						list.add(serverThread);
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	

}
