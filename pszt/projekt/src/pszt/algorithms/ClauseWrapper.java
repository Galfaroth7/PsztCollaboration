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
		if(parentClause1!= null)
			firstParent = new ClauseWrapper(parentClause1);
		if(parentClause2!= null)
			secondParent = new ClauseWrapper(parentClause2);
	}
	 /**
	  * konstruktor kopiujacy, uzywany w powyzszym
	  * @param other
	  */
	ClauseWrapper(ClauseWrapper other)
	{
		wrapped = new Clause(other.getClause());
		if(other.getFirstParent() != null)
			firstParent = new ClauseWrapper(other.getFirstParent());
		if(other.getSecondParent() != null)
			secondParent = new ClauseWrapper(other.getSecondParent());
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
	/**
	 * implementacja interfejsu Comparable
	 */
	@Override
	public int compareTo(ClauseWrapper o) 
	{
		return (this.wrapped.getPredicates().size() - o.getClause().getPredicates().size());
	}
}
