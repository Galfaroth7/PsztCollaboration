package pszt.algorithms;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import pszt.structures.*;

/**
 * klasa ktora w miare mozliwosci wytwarza dowody (linearResolver) podanych tez (addTheses)
 * uzywajac danej bazy wiedzy (addKnowledgeBase)
 * @author Lesziy
 */
public class ResolvingMachine 
{
	List<ClauseWrapper> theses = new LinkedList<ClauseWrapper>();
	List<ClauseWrapper> knowledgeBase= new LinkedList<ClauseWrapper>();
	/**
	 * funkcja sluzaca do prowadzenia rezolucji
	 * 
	 * na razie preferencja krotkich klauzul opiera sie na sortowaniu list
	 * 
	 * 
	 * @param shortest okresla, czy uzytkownik chce, zeby rezolucja byla przeprowadzana
	 * metoda z preferencja krotkich klauzul
	 * @return wywnioskowane drzewa dowodu jako liste
	 */
	public List<ClauseWrapper> linearResolver(boolean shortest)
	{
		LinkedList<ClauseWrapper> resolved = new LinkedList<ClauseWrapper>();
		if(shortest)
		{
			Collections.sort(knowledgeBase);
		}
		for(ClauseWrapper thesis : theses)
		{
			ClauseWrapper resolve = recursiveResolve(thesis, shortest);//wola rekursywna metode rezolucji
			if(resolve != null)
			{
				resolved.add(resolve);
			}
		}
		return resolved;
	}
	/**
	 * @param parsedThesis zanegowana teza
	 */
	public void addTheses(List<Clause> parsedThesis)
	{
		for (Clause copy : parsedThesis)
		{
			theses.add(new ClauseWrapper(copy, null, null));
		}
	}
	/**
	 * 
	 * @param parsedKnowledgeBase baza wiedzy
	 */
	public void addKnowledgeBase(List<Clause> parsedKnowledgeBase)
	{
		for (Clause copy : parsedKnowledgeBase)
		{
			knowledgeBase.add(new ClauseWrapper(copy, null, null));
		}
	}
	/**
	 * rekursywna metoda rezolucji
	 * @param cell element drzewa decyzyjnego ktory wywolal rezolucje
	 * @param shortest czy jest preferencja dla krotkich klauzul
	 * @return wywnioskowane drzewo dowodu lub null
	 */
	private ClauseWrapper recursiveResolve(ClauseWrapper cell, boolean shortest)
	{
		for (ClauseWrapper knowledge : knowledgeBase)
		{
			List<Clause> resolvedClauses = cell.getClause().performResolution(knowledge.getClause());
			if(shortest)
			{
				Collections.sort(resolvedClauses);
			}
			if(resolvedClauses.size()>0)
			{
				for(Clause resolved : resolvedClauses)
				{
					
					ClauseWrapper newCell = new ClauseWrapper(resolved,cell,knowledge);
					ClauseWrapper resolvent = recursiveResolve(newCell,shortest);
					if(resolvent == null)
					{
						continue;
					}
					if(resolvent.getClause().getPredicates().isEmpty())
					{
						return resolvent;
					}
				}
			}
		}
		return null;
	}
}
