# Github Scraper

Small Java project to see what I could do in a very quick time. It was my own sort of hackathon. The code quality is not of the level that I would want from a project I worked for a while on however that wasn't the goal of this project, rather to just see what I can do when I just start writing. The project is extremely brute force, but I am happy that I completed it.

Breadth first searches from a single users page and can find almost all other users on github who are followed or follow others im sure. It then parses their repositories searching for the file types and the source lines of code for each file. Keeps running count of the average of many languages. 

Cool features include serialization of the conductor which has a set of all users it has already visited, the current pool of tasks and of course the underlying map counting average sloc's by language. This is cool because the scraper can pick up where it left off. It is of course multithreaded.