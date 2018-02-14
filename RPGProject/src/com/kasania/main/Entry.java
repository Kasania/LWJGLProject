package com.kasania.main;

import com.kasania.core.DummyLogic;
import com.kasania.core.GameManager;
import com.kasania.core.Logic;

public class Entry {
	public static void main(String[] args) {
		try{
			boolean vsync = true;
			Logic logic = new DummyLogic();
			GameManager gameManager = new GameManager("TEST", 1600, 900, vsync, logic);
			gameManager.start();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
