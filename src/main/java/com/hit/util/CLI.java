package com.hit.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Scanner;
import com.hit.server.Server;

public class CLI extends Observable implements Runnable {
	private Scanner is_string;
	private PrintWriter os_string;
	private boolean mRunning = true;
	private boolean mCountinue = true;
	public CLI(InputStream in, OutputStream out) {
		is_string = new Scanner(new BufferedReader(new InputStreamReader(in)));
		os_string = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
	}

	public void write(String string) {
		os_string.write(string + "\n");
		os_string.flush();

	}

	@Override
	public void run() {
		
		while (mRunning) {
			if(mCountinue) {
				write("please enter your command");
			}
			mCountinue = true;
			String input = is_string.nextLine();
			if (!input.isEmpty()) {
				switch (input) {
				case "start":
				case "stop":
					setChanged();
					notifyObservers(input);
					mCountinue = false;
					break;
				default:
					write(String.format("Not a vaild command:[%s]", input));
					break;
				}
			}
		}
		is_string.close();
		os_string.close();
	}
}
