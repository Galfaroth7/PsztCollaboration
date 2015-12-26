package pszt.application;
import java.io.*;
import java.util.List;

import pszt.parser.*;
import pszt.algorithms.*;
public class Application
{
	static Parser parser = new Parser();
	static ResolvingMachine resolver = new ResolvingMachine();
	public static void main(String[] args)
	{
		resolver.addKnowledgeBase(parser.parseClausesFromFile("resources/KnowledgeBase.in"));
		resolver.addTheses(parser.parseClausesFromFile("resources/thesis.in"));
		List<ClauseWrapper> result = resolver.linearResolver(false);
		try
		{
			PrintWriter out = new PrintWriter("resources/result.out");
			for (ClauseWrapper node : result)
			{
				out.print(node.toString());
			}
			out.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Brak pliku" + e.getMessage());
			return;
		}
	}
}
