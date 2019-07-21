package com.hit.server;

import java.io.IOException;
import com.hit.services.CacheUnitController;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Server implements Observer {
	private ServerSocket mServerSocket;
	private static final int mPort = 12345;
	public static int numOfClient = 0;
	private HandleRequest<String> client;
	private boolean mRunning = false;
	public ArrayList<HandleRequest<String>> clientList;

	public Server() {
		try {
			mServerSocket = new ServerSocket(mPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not open a Socket");
		}
	}

	public void start() {
		mRunning = true;
		Socket socket = null;
		CacheUnitController<String> c = new CacheUnitController<String>();
		clientList = new ArrayList<HandleRequest<String>>();
		try {
			while (mRunning) {
				socket = mServerSocket.accept(); // waiting for client to connect - await block
				System.out.println("Client Connected " + socket.getInetAddress());
				numOfClient++;
				client = new HandleRequest<String>(socket, c);
				clientList.add(client);
				new Thread(client).start();
			}
			System.out.println("finished while loop");
		} catch (IOException e) {
		} finally {
			for (HandleRequest<String> cl : clientList) {
				if (client != null) {
					System.out.print("shutting down client: ");
					client.close();
				}
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg != null) {
			String command = (String) arg;
			switch (command) {
			case "start":
				new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println("Starting server.......");
						start();
					}
				}).start();

				break;
			case "stop":
				if (mRunning) {
					System.out.println("Shutdown server");
					mRunning = false;
					try {
						mServerSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
					}
				} else
					System.out.println("Server is already shut down");
				break;
			default:
				System.out.println(String.format("Unknown Command %s", command));
				break;
			}
		}

	}
}
