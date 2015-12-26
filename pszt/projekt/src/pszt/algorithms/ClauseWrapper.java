package pszt.algorithms;
import pszt.structures.*;
/**
 * 
 * @author Lesziy
 *	Teoretycznie powinna tworzyc strukture jakby "odwroconego drzewa" (bo korzen dodawany jest na koncu)
 *	co wydaje sie, bedzie przydatne przy wizualizacji drzewa decyzyjnego
 */
public class ClauseWrapper  implements Comparable<ClauseWrapper>
{
	
	private Clause wrapped;	//to jest klauzula ktorej ten wrapper dotyczy
	private ClauseWrapper firstParent = null; 
	private ClauseWrapper secondParent = null; /*poprzednie klauzule z ktorych powstala obecna*/
											   /*one rowniez moga miec rodzicow, ale nie musza*/
	/**
	 * podstawowy konstruktor tej klasy
	 * @param wrappedClause
	 * @param parentClause1
	 * @param parentClause2
	 */
	public ClauseWrapper(Clause wrappedClause, ClauseWrapper parentClause1, ClauseWrapper parentClause2)
	{
		wrapped = new Clause(wrappedClause);
		firstParent  = parentClause1;
		secondParent = parentClause2;
	}
	 /**
	  * konstruktor kopiujacy, uzywany w powyzszym
	  * @param other
	  */
	ClauseWrapper(ClauseWrapper other)
	{
		wrapped = other.getClause();
		firstParent = other.getFirstParent();
		secondParent = other.getSecondParent();
	}
	/**
	 * 
	 * @return klauzula ktorej dotyczy obiekt
	 */
	public Clause getClause()
	{
		return wrapped;
	}
	/**
	 * @return pierwszego rodzica klauzuli
	 */
	public ClauseWrapper getFirstParent()
	{
		return firstParent;
	}
	/**
	 * @return drugiego rodzica klauzuli
	 */
	public ClauseWrapper getSecondParent()
	{
		return secondParent;
	}
	public String toString()
	{
		String returned = new String();
		returned = recursiveToString(this);
		return returned;
	}
	private String recursiveToString(ClauseWrapper node)
	{
		String clauseString = new String();
		clauseString += "clause :" +  node.getClause().toString()+ "\n";
		if(node.getFirstParent() !=null)
		{
			clauseString += "firstParent : " + node.getFirstParent().getClause().toString() + "\n";
		}
		if(node.getSecondParent() != null)
		{
			clauseString += "secondParent : " + node.getSecondParent().getClause().toString() + "\n";
		}
		if(node.getFirstParent() != null)
		{
			clauseString += recursiveToString(node.getFirstParent());
		}
		if(node.getSecondParent() != null)
		{
			clauseString += recursiveToString(node.getSecondParent());
		}
		return clauseString;
	}
	/**
	 * implementacja interfejsu Comparable
	 */
	@Override
	public int compareTo(ClauseWrapper o) 
	{
		return (this.wrapped.getPredicates().size() - o.getClause().getPredicates().size());
	}
}
