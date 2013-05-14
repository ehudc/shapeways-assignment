import java.io.*;
import java.util.*;

// A short summary of what this does:
// 1) Make a HashMap of Artists and a list of users who like them
// 2) Cut that map down to only those Artists who appear 50 or more times in user lists
//   - That's because we want to find pairs of Artists that appear 50 or more times together
// 3) Using the new map, check the intersection of every Artist's associated user list
//   - If the intersection set >= 50, that pair is printed

public class ArtistPairsMain {
	
	private static int min = 50; // change value if you want to change the cutoff
	
	private static void runProgram (String filename){
		
		// HashMap where key="Artist Name" and value={List of users who like thie artist}.
		// Using a TreeSet for the user list rather than array/vector so I can use the Java method .retainAll()
		// to get the intersection of those users who list the same artists (as explained before the relevant code below).
		HashMap <String, TreeSet<Integer>> ArtistUserMatches = new HashMap();
		
		BufferedReader in = null;
        
		try
        {
            in = new BufferedReader(new FileReader(filename));

            int index = 0; // keep track of each user, update on every new line  
            String line;
            
            // loop through every line ( O(AmountLines) = O(N) )
            // loop through every artist on every line ( O(AmountArtists) = O(N) )
            // Total complexity is O(N)*O(N) or O(N^2)
            while((line = in.readLine()) != null){
            	
                String artistNames[] = line.split(",");  
                
                // check each artist on each line
                for(int i = 0; i < artistNames.length; i++){
            		String artist = artistNames[i].trim(); // clear white space  
            		
            		TreeSet<Integer> newUserList = new TreeSet();
            		
            		// If the first time a new Artist is seen on any line
            		// then create a new User list to associate with that Artist
            		// and add the Artist-UserList pair to the HashMap
                    	if(!(ArtistUserMatches.containsKey(artist))) 
                    	{
                    		newUserList.add(index);
                    		ArtistUserMatches.put(artist, newUserList);
                    	}

                    	else // else just add to the number of users who already listed the artist
                    	{                                  	
                    		ArtistUserMatches.get(artist).add(index);
                    		//ArtistUserMatches.get(artist).size(); // if want to see number of users who list it, currently
                    	}              
                }                
                index++;
            }
        }
		catch(IOException e)
        {
            System.out.println("can't open the file " + filename);
            System.exit(1);
        } finally {
            if (in != null)
            {
                try {
                  in.close();
                }
                catch (IOException e)
                {
                  e.printStackTrace();
                }
            }
        }        
		
		// loop through HashMap, remove entries with fewer than 50 (or "min") users liking an artist
		// This has basic complexity O(entries) or O(N)
        Iterator it = ArtistUserMatches.entrySet().iterator();
        while (it.hasNext()) {
        	Map.Entry<String, TreeSet<Integer>> entry = (Map.Entry)it.next();        	
        	if(entry.getValue().size() < min ){
        		it.remove();
        	}
        }
        /*
        // if want to see the remaining list:
        for (Map.Entry<String, TreeSet<Integer>> matches : ArtistUserMatches.entrySet())
        {
          	System.out.println(matches.getKey() + "/" + matches.getValue());
        }
		*/
        
        // Now we have a map where key="Artist Name" and value={TreeSet of >= 50 users who list the artist}.
        // Loop through and check each artist against each other to find where
        // they appear together in a user's list (the set intersection).
        // If the intersection set is over 50, then write to file.
        
        // The loop will run N(N-1)/2 times (comparing every element to every other element, summation from 1 .. N-1).
        // After each iteration, one less element (the last one) is needed to be compared until there are no more elements left to be compared.
        // Thus, just like a Bubble Sort, its complexity is O(N^2).
        // Considering that this comparison looping is occurring over a small Map of at most 50 Artists,
        // This is an ok solution (it would be less practical if comparing a much, much larger set).
        
        BufferedWriter bw = null;
        try { 
			File file = new File("AUMatches.txt");
			
			// if file doesn't exists, then create it        				
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
	        
	        for (int j=0; j < ArtistUserMatches.size(); j++) {
	        	// String key = (String)ArtistUserMatches.keySet().toArray()[j];
	    		// TreeSet val = (TreeSet)ArtistUserMatches.values().toArray()[j];
	    		// System.out.println("key,val: " + key + "," + val);
	        	
	        	for (int k=j+1; k < ArtistUserMatches.size(); k++){
	        		
	        		Set<Integer> intersection = new HashSet<Integer>((TreeSet)ArtistUserMatches.values().toArray()[j]);
	        		intersection.retainAll((TreeSet)ArtistUserMatches.values().toArray()[k]);
	        		if(intersection.size() >= min){
	        			// System.out.println((String)ArtistUserMatches.keySet().toArray()[j] + "," + (String)ArtistUserMatches.keySet().toArray()[k]);
	        			String content = (String)ArtistUserMatches.keySet().toArray()[j] + "," + (String)ArtistUserMatches.keySet().toArray()[k] + '\n';
	        			bw.write(content);	  			
	        		}
	        	}
	        }
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
            if (bw != null)
            {
                try {
                	System.out.println("Finished writing to file AUMatches.txt");
                	bw.close();
                }
                catch (IOException e)
                {
                  e.printStackTrace();
                }
            }
        } 
   
	}
	
    public static void main(String[] args) {
    	
    	if(args.length != 1)
    	{
    		System.out.println("be sure you entered just one file.txt for the program to check");
    		System.exit(1);
    	}
    	else
    		runProgram(args[0]);
    }
}
