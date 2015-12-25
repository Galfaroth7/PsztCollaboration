package pszt.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import pszt.structures.Clause;
import pszt.structures.Predicate;
import pszt.structures.Term;
import pszt.structures.TermType;

/**
 * Parses the clauses to Clause objects.
 * @author hejcz
 */
public class Parser {
     
    private final char DELIMITER = ',';
    private final char FUN_START = '(';
    private final char FUN_END = ')';
    private final String CLAUSE_DELIMITER = "v";
    private final char NEGATION_SIGN = '~';
    
    /**
     * Parses all clauses from the given file. The file must 
     * contain one clause per line. The method uses parseClause method
     * to parse every line from file.
     * @param filePath path to the input file
     * @return list of parsed clauses
     */
    public List<Clause> parseClausesFromFile(String filePath){
        List<Clause> clauses = new ArrayList<>();
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))){
            while(reader.ready()){
                clauses.add(parseClause(reader.readLine()));
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
        return clauses;
    }
    
    /**
     * Parses given string to Clause object. Splits clause to list of 
     * predicates and uses parsePredicates method to parse each one.
     * @param toParse string containing single clause to parse
     * @return parsed clause
     */
    public Clause parseClause(String toParse){
        String[] predicatesToParse = toParse.replaceAll(" ", "").split(CLAUSE_DELIMITER);
        Clause clause = new Clause();
        for(String predicate : predicatesToParse){
            clause.addPredicate(parsePredicate(predicate));
        }
        return clause;
    }

    /**
     * Parses given string to Predicate object. Extracts its name and sign
     * ( predicated might be negated ) and then parses its terms using parseTerms method.
     * @param toParse
     * @return 
     */
    Predicate parsePredicate(final String toParse) {
        boolean isNegated = toParse.charAt(0) == NEGATION_SIGN;
        int openBraceIndex = toParse.indexOf(FUN_START);
        int closeBraceIndex = toParse.length() - 1;
        String name = toParse.substring(isNegated?1:0, openBraceIndex);
        Predicate predicate = new Predicate(name);
        predicate.isNegated(isNegated);
        predicate.addTerms( parseTerms(toParse.substring(openBraceIndex + 1, closeBraceIndex)) );
        return predicate;
    }
    
    /**
     * Parses given text to list of Term object. 
     * Uses {@link #nextParseIndex(java.lang.String, int) nextParseIndex} method to find next
     * index needing attention. If delimiter is found then the substring is converted to
     * variable or constant. Otherwise this is function, matching brace is found and 
     * method recursively parses function's arguments.
     * @param toParse
     * @return 
     */
    private List<Term> parseTerms(String toParse){
        List<Term> terms = new LinkedList<>();
        int start = 0, current = 0;
        while(start < toParse.length()){
            current = nextParseIndex(toParse, current);
            if(current == toParse.length() || toParse.charAt(current) != '(') {
                terms.add(new Term(toParse.substring(start, current), 
                        Character.isLowerCase(toParse.charAt(start)) ? TermType.VARIABLE : TermType.CONSTANT));
                current = start = current + 1; // skip comma
            }
            else{
                int funEnd = findMatchingBrace(toParse, current);
                Term fun = new Term(toParse.substring(start, current), TermType.FUNCTION);
                fun.addArguments(parseTerms(toParse.substring(current + 1, funEnd)));
                terms.add(fun);
                current = start = funEnd + 2; // skip ) and comma
            }
        }
        return terms;
    }
    
    /**
     * Finds next index which needs attention. Returns next term
     * delimiter or opening brace of which starts at startIndex function.
     * @param text string in which search is performed
     * @param startIndex index at which search is started
     * @return 
     */
    private int nextParseIndex(String text, int startIndex){        
        for(int index = startIndex; index < text.length(); index++){
            char letter = text.charAt(index);
            if(letter == DELIMITER || letter == FUN_START){
                return index;
            }                
        }
        return text.length();
    }
 
    /**
     * Finds brace matching the one at openBraceIndex.
     * @param terms string in which matching is performed
     * @param openBraceIndex index of opening brace in the given string
     * @return index of matching brace or -1 if the match was not found
     */
    private int findMatchingBrace(String terms, int openBraceIndex) {
        int onStack = 1, i = openBraceIndex + 1;
        for(; onStack != 0 && i < terms.length(); i++){
            if( terms.charAt(i) == FUN_START ) onStack++;
            else if ( terms.charAt(i) == FUN_END ) onStack--;
        }
        return onStack == 0? i - 1 : -1;
    }
}