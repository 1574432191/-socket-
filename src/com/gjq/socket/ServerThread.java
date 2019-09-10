package com.gjq.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
/*
 * 服务器线程处理类
 */
public class ServerThread  extends Thread{
	public Socket socket;
	public InputStream inputStream;
	public OutputStream outputStream;
	public ServerThread(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			//获取客户端发送给服务端的信息
			inputStream=socket.getInputStream();
			//服务端给客户端发送信息
			outputStream=socket.getOutputStream();
			//发消息给客户端
			String msg = "welcome to zhou's server !";
			sendMsg(outputStream, msg);
			//发送要求登录信息给客户端
			String userinfo = "please input your name";
			sendMsg(outputStream, userinfo);
			//获取客户端输入的用户名
			String username = readMsg(inputStream);
			//发送要求密码信息给客户端
			String pwd = "please input your password";
			sendMsg(outputStream, pwd);
			//获取客户端输入的密码
			String password = readMsg(inputStream);
			//登录检验
			boolean flag = loginCheck(username, password);
			//校验不通过时，循环校验
			while(!flag){
				//发消息给客户端
				 msg = "welcome to zhou's server !";
				sendMsg(outputStream, msg);
				//发送要求登录信息给客户端
				 userinfo = "please input your name";
				sendMsg(outputStream, userinfo);
				//获取客户端输入的用户名
				 username = readMsg(inputStream);
				//发送要求密码信息给客户端
				 pwd = "please input your password";
				sendMsg(outputStream, pwd);
				//获取客户端输入的密码
				 password = readMsg(inputStream);
				//登录检验
				 flag = loginCheck(username, password);
			}
			//校验成功后，开始聊天
			msg = "successful connected..... you can chat with your friends now ......";
			sendMsg(outputStream, msg);
			//聊天处理逻辑
			//读取客户端发来的消息
			msg=readMsg(inputStream);
			//输入bye结束聊天
			while(!"bye".equals(msg)){
				//给容器的每个对象转发消息
				for (int i = 0; i < MyServer.list.size(); i++) {
					ServerThread serverThread = MyServer.list.get(i);
					//不该自己转发消息
					if (serverThread!=this) {
						sendMsg(serverThread.outputStream, username+" is say:"+msg);
					}
				}
				//等待下一次的消息
				msg=readMsg(inputStream);
			}
			
		} catch (IOException e) {
			System.out.println("客户端不正常关闭");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//有异常后，统一将流关闭
		try {
			inputStream.close();
			outputStream.close();
			socket.close();
			//将当前已经关闭的客户端从容器中移除
			MyServer.list.remove(this);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//校验客户端输入的账号和密码的函数，由于没有数据库，暂时写死
	public boolean loginCheck(String name,String pwd){
		if (name.equals("zhou") && pwd.equals("zhou") || name.equals("user") && pwd.equals("pwd")
				|| name.equals("gjq") && pwd.equals("gjq")) {
 
			return true;
		}
		return false;
		
	}
	
	//发送消息的函数
	public void sendMsg(OutputStream outputStream,String s) throws IOException{
		//向客户端输出信息
		byte[] bytes = s.getBytes();
		outputStream.write(bytes);
		outputStream.write(13);
		outputStream.write(10);
		outputStream.flush();
	}
	
	public String readMsg(InputStream inputStream)throws Exception{
		//读取客户端的信息
		int value = inputStream.read();
		//读取整行，读取到回车（13） 换行（10）时停止读
		String str = "";
		while(value!=10){
			//点击关闭客户端时会返回-1值
			if (value==-1) {
				throw new Exception();
			}
			str = str+((char)value);
			value=inputStream.read();
		}
		//trim()方法，返回一个新的字符串，这个字符串将删除原始字符串头部和尾部的空格
		str = str.trim();
		return str;
	}
}
