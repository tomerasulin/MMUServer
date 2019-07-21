package com.hit.server;

import java.io.FileNotFoundException;
import java.util.Observable;
import java.util.Observer;

import com.hit.util.CLI;

public class CacheUnitServerDriver {

	public CacheUnitServerDriver() {
	}

	public static void main(String[] args) throws FileNotFoundException {
		CLI cli = new CLI(System.in, System.out);
		Server server = new Server();
		cli.addObserver(server);
		new Thread(cli).start();
	}

}