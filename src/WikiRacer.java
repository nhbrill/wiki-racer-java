import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
* AUTHOR: Nate Brill
* FILE: WikiRacer.java
* PURPOSE: To create the ladder between two wiki links.
*/
public class WikiRacer {
	// Memoization 
	private static Map<String, Integer> memo = new HashMap<String, Integer>();

	public static void main(String[] args) {
		/*
         * PURPOSE: Calls findWikiLadder and prints the ladder between the two
         * wiki links.
         * 
         * @param args, args[0] is the starting page to search ladders from,
         * args[1] is the end page the program is trying to find.
         */
		List<String> ladder = findWikiLadder(args[0], args[1]);
		System.out.println(ladder);
	}

	private static List<String> findWikiLadder(String start, String end) {
		/*
         * Finds the wiki ladders by getting most similar links.
         * 
         * @param start, the starting wiki page.
         * @param end, the ending target wiki page.
         * 
         * @return ladder, the ladder between the two pages.
         */
		List<String> ladder = new ArrayList<>();
		Set<String> target = WikiScraper.findWikiLinks(end);
		String q = start;
		while (!q.equals(end)) {
			ladder.add(q);
			Set<String> links = WikiScraper.findWikiLinks(q);
			q = createQueue(links, target, end);
		}
		ladder.add(end);
		return ladder;
	}
	
	private static String createQueue(Set<String> current, Set<String> end, String target){
		/*
         * Creates a priority queue based on how many common links a current
         * link has with the target link.
         * 
         * @param current, the end set of links in the last value of the ladder
         * @param end, the end set of links on the end page.
         * 
         * @return String, the highest priority value in the queue.
         */
		MaxPQ queue = new MaxPQ();
		//Gets links in parallel.
		current.parallelStream().forEach(link -> {
			// If the link is found parallel stream exits.
			if (link.equals(target)) {
				queue.enqueue(link, end.size());
				return;
			}
			if (memo.containsKey(link)) { // Memoization 
				queue.enqueue(link, memo.get(link));
			} else {
				// Value not found before.
				Set<String> links = WikiScraper.findWikiLinks(link);
				links.retainAll(end); // Gets similarity.
				queue.enqueue(link, links.size());
				memo.put(link,  links.size());
			}
		});
		return queue.dequeue();
	}

}
