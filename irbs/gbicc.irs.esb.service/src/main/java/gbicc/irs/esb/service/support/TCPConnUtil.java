package gbicc.irs.esb.service.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 类功能: TCP读写公共类
 * 
 * @author chen.jie
 * @date 2011-9-14
 */
public class TCPConnUtil {

	/**
	 * 日志
	 */
	private static Log log = LogFactory.getLog(TCPConnUtil.class);

	/**
	 * socket连接超时时间,默认是5秒
	 */
	public static final int CONNECT_TIME_OUT = 5000;

	/**
	 * 按指定长度读取报文
	 * 
	 * @param client
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public static byte[] readLenContent(InputStream is, int length)
			throws IOException {

		// 每次最大接收1M的请求报文
		if (length > 1024000) {
			if (log.isErrorEnabled()) {
				log.error("接收的请求长度太长,length =[" + length + "]");
			}
			return null;
		}
		int count = 0;
		int offset = 0;

		byte[] retData = new byte[length];

		while ((count = is.read(retData, offset, length - offset)) != -1) {
			if (log.isDebugEnabled()) {
				log.debug("读取字节长度为[" + count + "]");
			}
			offset += count;
			if (offset == length)
				break;
		}

		return retData;
	}

	/**
	 * 写消息
	 * 
	 * @param client
	 * @param buff
	 * @throws IOException
	 */
	public static void writeMessage(OutputStream os, byte[] buff)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("开始发送数据！");
		}
		os.write(buff);
		os.flush();
		if (log.isDebugEnabled()) {
			log.debug("数据发送完毕，发送数据的长度为：[" + buff.length + "]");
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param socket
	 * @param os
	 * @param is
	 */
	public static void closeConnect(Socket socket, OutputStream os, InputStream is) {
		try {
			if (is != null) {
				is.close();
				is = null;
			}

			if (os != null) {
				os.close();
				os = null;
			}

			if (socket != null) {
				socket.close();
				socket = null;
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("关闭连接异常", e);
			}
		}
	}

	/**
	 * 连接
	 * 
	 * @param request
	 * @param port
	 * @param timeOut
	 * @return
	 * @throws Exception
	 */
	public static Socket getConnect(String address, int port, int timeOut)
			throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("开始建立socket连接,address[" + address + "]port[" + port + "]");
		}

		Socket socket = new Socket();
		SocketAddress endpoint = new InetSocketAddress(address, port);
		socket.connect(endpoint, CONNECT_TIME_OUT);

		// 设置超时时间，参数为毫秒
		socket.setSoTimeout(timeOut);
		// 无延迟
		//socket.setTcpNoDelay(true);
		// 0x04可靠的 0x10最小延迟
		socket.setTrafficClass(0x04 | 0x10);

		if (log.isDebugEnabled()) {
			log.debug("建立Socket套接字连接成功");
		}

		return socket;
	}
}
