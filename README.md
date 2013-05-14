#### Listing Element Pairs

Given a .txt file with each line representing a list of artists that a user likes, return a list of those artists that appear together at least N amount of times.

My methodology was:

1. Make a HashMap, where Artists are keys, and a set of users who like them are the values

2. Cut that map down to only those Artist keys where the values are N or more

3. Using the new map, check the intersection of every key's value set. Find the ones that return N or more (thus, N or more user's list these two artists together) and print those pairs.

Comments are also in the code.
