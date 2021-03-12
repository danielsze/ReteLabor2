package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;

import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
import java.io.BufferedReader;
import java.io.InputStreamReader;



public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		
		
		
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean running = true;
		while(running) {
			String line = br.readLine();
			line = line.toLowerCase();
			if(line.equals("exit")) {
				running = false;
			}else if(line.equals("start")){
				s.raiseStart();
			}else if(line.equals("white")){
				s.raiseWhite();
			}else if(line.equals("black")){
				s.raiseBlack();
			}
			s.runCycle();
			print(s);
		}
		System.exit(0);
	}

	public static void print(IExampleStatemachine s) {
		System.out.println("w = " + s.getSCInterface().getWhiteTime());
		System.out.println("b = " + s.getSCInterface().getBlackTime());

	}
}
