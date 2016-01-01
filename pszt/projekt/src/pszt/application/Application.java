package pszt.application;
import java.io.*;
import java.util.List;

import pszt.parser.*;
import pszt.algorithms.*;
import pszt.userinterface.*;
public class Application
{
	static Parser parser = new Parser();
	static ResolvingMachine resolver = new ResolvingMachine();
       
	public static void main(String[] args)
	{   
            MainWindow mainWindow = new MainWindow(parser, resolver); 
            
	}
}
/*		resolver.addKnowledgeBase(parser.parseClausesFromFile("resources/KnowledgeBase1.in"));
		resolver.addTheses(parser.parseClausesFromFile("resources/thesis1.in"));
		List<ClauseWrapper> result = resolver.linearResolver(false);
		try
		{
			PrintWriter out = new PrintWriter("resources/result1.out");
			for (ClauseWrapper node : result)
			{
				String text = node.toString();
				System.out.print(text);
				out.print(text);
			}
			out.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Brak pliku" + e.getMessage());
			return;
		}*/