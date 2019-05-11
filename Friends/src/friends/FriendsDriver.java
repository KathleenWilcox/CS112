package friends;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class FriendsDriver {

	public static void main(String[] args) {

		Graph g;
		try {
			g = new Graph(new Scanner(new File("Friends.txt")));
			//System.out.println(Friends.shortestChain(g, "sam", "michelle"));
			System.out.println(Friends.shortestChain(g, "sam","michele" ));

			// System.out.println(Friends.cliques(g, "rutgers"));
			// jane aparna, nick, tom, michele
			// Friends.connectors(g);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}


