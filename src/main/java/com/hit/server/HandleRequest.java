package com.hit.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.hit.dm.DataModel;
import com.hit.services.CacheUnitController;
import com.hit.services.CacheUnitService;

public class HandleRequest<T> implements Runnable {
	private Socket m_Socket;
	private CacheUnitController<T> m_controller;
	private String req;
	private Map<String, String> m_header;
	private DataModel<T>[] m_body;
	private String statistic;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	public Server server;
	
	public HandleRequest(Socket s, CacheUnitController<T> controller) {
		m_Socket = s;
		m_controller = controller;
	}

	@Override
	public void run() {
		Type ref = new TypeToken<Request<DataModel<T>[]>>() {
		}.getType();

		try {
			oos = new ObjectOutputStream(m_Socket.getOutputStream());
			ois = new ObjectInputStream(m_Socket.getInputStream());
		} catch (IOException e) {
			System.err.println("Could not handle socket");
			return;
		}
		while (m_Socket.isConnected() && !m_Socket.isClosed()) {
			try {
				req = (String) ois.readObject();
			} catch (IOException | ClassNotFoundException e) {
				req = null;
			}
			if (req != null) {
				if (!req.contains("STATISTIC") && !req.contains("CLIENT")) {
					try {
						Request<DataModel<T>[]> request = null;
						request = new Gson().fromJson(req, ref);
						m_header = request.getHeaders(); // action and GET/DELETE/...
						m_body = request.getBody();

						if (m_header.containsValue("GET")) {
							m_controller.get(m_body);
						} else if (m_header.containsValue("DELETE")) {
							m_controller.delete(m_body);
						} else if (m_header.containsValue("UPDATE")) {
							m_controller.update(m_body);
						}
						respondToClient("Success!");
					} catch (JsonParseException e) {
						respondToClient("Failed!");
					}
				} else if(req.contains("STATISTIC")) {
					respondToClient(m_controller.getStatistic());
				}
				else{
					Integer clientSize = server.numOfClient;
					respondToClient("Number of Connected Client: "+ clientSize.toString());
				}
					
			}
		}
		System.out.println("Server:client socket closed");
	}

	public void close() {
		if (m_Socket != null) {
			try {
				System.out.println(m_Socket.getInetAddress());
				m_Socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
	}

	private void respondToClient(String aMsg) {
		try {
			if (m_Socket != null && m_Socket.isConnected() && oos != null && aMsg != null && !aMsg.isEmpty()) {
				oos.writeObject(aMsg);
				oos.flush();
			}
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

}
