package hu.bme.mit.yakindu.analysis.workhere;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;
import org.yakindu.sct.model.stext.stext.EventDefinition;


import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static String generateCode(Iterable<String> events, Iterable<String> variables) {
		StringBuilder sb = new StringBuilder();
		sb.append("package hu.bme.mit.yakindu.analysis.workhere;\r\n" + 
				"\r\n" + 
				"import java.io.IOException;\r\n" + 
				"\r\n" + 
				"import hu.bme.mit.yakindu.analysis.RuntimeService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.TimerService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\r\n" + 
				"import java.io.BufferedReader;\r\n" + 
				"import java.io.InputStreamReader;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"public class RunStatechart {\r\n" + 
				"	\r\n" + 
				"	public static void main(String[] args) throws IOException {\r\n" + 
				"		\r\n" + 
				"		\r\n" + 
				"		\r\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\r\n" + 
				"		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));\r\n" + 
				"		boolean running = true;\r\n" + 
				"		while(running) {\r\n" + 
				"			String line = br.readLine();\r\n" + 
				"			line = line.toLowerCase();\r\n");
		sb.append(
				"			if(line.equals(\"exit\")) {\r\n" + 
				"				running = false;\r\n" + 
				"			}");
		for(String event : events) {
			sb.append(String.format(
				"else if(line.equals(\"%s\")){\r\n" + 
				"				s.raise%s();\r\n" +
				"			}", event, event.substring(0, 1).toUpperCase()+ event.substring(1)));
		}
		sb.append(
				"\r\n"+
				"			s.runCycle();\r\n" + 
				"			print(s);\r\n" + 
				"		}\r\n" + 
				"		System.exit(0);\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	public static void print(IExampleStatemachine s) {\r\n");
		for(String variable : variables) {
			sb.append(String.format(
				"		System.out.println(\"%s = \" + s.getSCInterface().get%s());\r\n", variable.charAt(0), variable.substring(0, 1).toUpperCase() + variable.substring(1)));
		}
		sb.append(
				"\r\n	}\r\n" + 
				"}\r\n" + 
				"");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		int unique_id = 0;
		List<String> events = new ArrayList<String>();
		List<String> variables = new ArrayList<String>();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			
			if(content instanceof VariableDefinition) {
				VariableDefinition vd = (VariableDefinition)content;
				variables.add(vd.getName());
				//System.out.println(vd.getName());
			}
			if(content instanceof EventDefinition) {
				EventDefinition ed = (EventDefinition) content;
				events.add(ed.getName());
				//System.out.println(ed.getName());
			}
		}
		
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
	
		
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
		System.out.println(generateCode(events,variables));
	}
}
