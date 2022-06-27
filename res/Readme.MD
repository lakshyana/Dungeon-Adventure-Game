## **About / Overview:**

This project is the third part of the MVC based adventure game project. The goal of this project is
to design and implement an asynchronous controller and view using the model that we built in the
previous projects. In addition, we will be adding enhancements to the dungeon to make the game more
interactive and fun.

**Objective**:
To design and implement a solution to a complex problem using proper object-oriented principles. The
project follows the recommended best practices for designing a complex system, some of which are as
follows:

- Use of Interfaces
- Applying abstraction for shared behavior among classes and use of Interface types instead of
  concrete types
- Avoidance of duplication of code
- Designing for extensibility
- Use of reusable components
- Hiding of implementation details through encapsulation

## **Requirements**

### **New Requirements of the Model**

The goal of this assignment is design and implement an asynchronous controller and view using the
model that we built in the previous projects, and enables the following new operations:
<br>

#### **View**

Add an option to play the game using an interactive graphical user interface (GUI), with the
following features:

- Expose all game settings including the size of the dungeon, the interconnectivity, whether it is
  wrapping or not, the percentage of caves that have treasure, and the number of Otyughs through one
  or more items on a JMenu
- Provide an option for quitting the game, restarting the game as a new game with a new dungeon or
  reusing the same dungeon through one or more items on a JMenu
- Display the dungeon to the screen using graphical representation. The view should begin with a
  mostly blank screen and display only the pieces of the maze that have been revealed by the user's
  exploration of the caves and tunnels. Dungeons that are bigger than the area allocated it to the
  screen should provide the ability to scroll the view.
- Allow the player to move through the dungeon using a mouse click on the screen in addition to the
  keyboard arrow keys. A click on an invalid space in the game would not advance the player. display
  the details of a dungeon location to the screen. For instance, does it have treasure, does it have
  an arrow, does it smell.
- Provide an option to get the player's description
- Provide an option for the player to pick up a treasure or an arrow through pressing a key on the
  keyboard.
- Provide an option for the player to shoot an arrow by pressing a key on the keyboard followed by
  an arrow key to indicate the direction.
- Provide a clear indication of the results of each action a player takes.

#### **Model**

There are some optional functionalities to be added in the Dungeon Model:

- Add a pit that the player could fall into with some kind of indicator that a pit is nearby
- Add another monster to the dungeon that the roams around the dungeon but requires the player to
  battle in hand-to-hand combat.
- Add a thief into the dungeon that steals treasure from the player whenever they are encountered

#### **Controller**

The requirements state that one or more controllers could be implemented for the text-based game,
and the GUI based game.

#### **Optional Functionalities**

- Provide an additional obstacle in the dungeon:
    - Add a pit that the player could fall into with some kind of indicator that a pit is nearby
    - Add another monster to the dungeon that the roams around the dungeon but requires the player
      to battle in hand-to-hand combat.
    - Add a thief into the dungeon that steals treasure from the player whenever they are
      encountered

### **Previous Requirements of the Model**

### **Dungeons**

The following are the requirements for dungeons:

- Consists of tunnels (with 2 entrances) and caves (1,3, or 4 entrances) that can be traveled from
  cave to cave.
- Each cave to be connected to at most 4 other locations: North, South, East, West.
- Should be able to be represented on a 2-D grid.
- Have a path from every cave in the dungeon to every other cave.
- To be constructed with a degree of interconnectivity representing the number of paths between
  caves
  (0 meaning only one path from a cave to every other cave)
- Locations may or may not "wrap" from one side to the other.
- They can be both wrapping and non-wrapping type with varying degrees of interconnectivity.
- Two caves to be randomly selected as the _start_ and the _end_ respectively.
- Minimum length of path between the start and the end is 5.

### **Dungeon: Cave**

- Two caves are selected at random as the start and end locations.
- The selected caves need to have a minimum distance of 5 cell nodes.
- Cave may have one or more treasures.
- The caves are assigned treasures at random but must meet the minimum percentage of caves with
  treasure provided by the user in command line arguments.

### **Dungeon: Tunnels**

- A variation of cave, in that it have 2 entrances, and doesn't hold treasure.

### **Treasures**

- Types: Diamonds, Rubies, and Sapphires
- Random treasure to be added to at least a percentage of caves specified by the client

### **Player**

- Place a player at a random starting cave location
- Provide a description of a player's location, including at the minimum a description of treasure
  in the room, and the possible moves (N, S, E, W) that can be made from player's current location
- Allow a player to move from the current location
- Allow a player to pick up treasure from their location.

### **Monsters- Otyughs**

- Atleast one Otyugh to be placed at the _end_ cave.
- The number of the monsters to be placed is specified via command line.
- There shouldn't be an Otyugh at the start cave.
- They should be detectable by their smell.
    - The less pungent smell, where there is one Otyugh smelled 2 positions from the player's
      current location.
    - And more pungent smell, where there may be multiple Otyughs 2 positions from the player's
      current location, or if there is one Otyugh 1 position from player's current location.
    - The monsters will kill and eat the player that enters the cave where they are present.

### **Slaying Monsters**

- It is possible for a player to splay a Otyugh monster with an arrow.
- A player starts with 3 crooked arrows and can find additional arrows with the same frequency as
  treasure, that may or may not be found together.
- As opposed to treasures that are only found in caves, arrows may be found at any location.
- A player can attempt to slay an Otygh by specifying a direction and distance to shoot the crooked
  arrow, where distance is defined by number of caves that an arrow travels.
- An arrow can only travel in a straight line through a cave, but can travel through crooked
  tunnels. For example,
    - A tunnel that has exits to the west and south can have an arrow enter the tunnel from the west
      and exit the tunnel to the south, or vice-versa (this is the reason the arrow is called a
      crooked arrow) a cave that has exits to the east, south, and west will allow an arrow to enter
      from the east and exit to the west, or vice-versa; but an arrow that enters from the  
      south would be stopped since there is no exit to the north.
- Distances must be exact. For example, if you shoot an arrow a distance of 3 to the east and the
  Otyugh is at a distance of 2, you miss the Otyugh.
- It takes 2 hits to kill an Otyugh. Players has a 50% chance of escaping if the Otyugh if they  
  enter a cave of an injured Otyugh that has been hit by a single crooked arrow.

## **Implementation**

To accommodate the project requirements, the implementation consists of the following structure with
interfaces, abstract classes, and concrete classes:

### **Interfaces**:

The project implementation includes the following interfaces:

1. **Readonly Adventure Game Interface**: This interface serves as the main interface to access all
   the necessary methods in the driver class to run the game.
2. **Actors Interface**: This interface includes the common operations to a player, monster, or any
   future object that might exist in a Dungeon cell.
3. **Modified Kruskal**: This interface is for accessing methods related to building a Dungeon
   object for the game. It is accessed by the Adventure Games interface.
4. **RandomGenerator Interface**: This interface is for accessing methods Random objects, which was
   designed to allow for generating true random values as well as fake random values, which would
   help for testing purposes.

### **Concrete Classes**:

1. **Games class**: Implementing Adventure game interface methods.
2. **Player class** to represent a player in the game.
3. **Monster class** to represent a monster in the game.
4. **Dungeon class** to represent a Dungeon in the game and containing cell objects of cave and
   tunnel types.
5. **Location class** to represent a objects of cave and tunnel cell types contained in a Dungeon.
6. Two concrete classes implementing RandomGenerator Interface:
    - One generating mock random values to be used for testing purpose,
    - And the other class to generate true random values.

### **List of Features**

- The program meets all the stipulated project functionality requirements and defines certain
  assumptions where there was a room for multiple interpretations of the requirements.
- In addition to the requirements listed above, the program is designed to provide flexibility to
  the users and makes it easier for extension by overriding the methods in the interface.

- The program design also facilitates future extension of the scope and characteristics of the
  battle game beyond the existing functionalities. The Adventure Games interface is the main
  user-facing interface that handles all the inner workings of other classes to hide the
  implementation details and provide an easy interaction for the user.
- The implementation details and other features are also described in assumptions and limitations,
  and implementation sections.

### **How to Run**

To run the program, run the driver class to see the text outputs of the sample demonstrations, or
run the jar file and check example one or follow the provided instructions to run the program
interactively:
<br>
**Example Run 1: Text-based game**
-Provide game arguments for a text-based game demo: # rows, # columns, wrapping, interconnectivity,
percent of treasure, percent of monster.
`cd res`
`java -jar Project5.jar 6 6 false 2 100 20`

**Graphical Interface Run**
`cd res`
`java -jar Project5.jar`

### **How to Use the Program**

_Description of Examples_

**The examples are demonstrated in the driver class. Some scenarios that can be tested in the
program are as follows:

**Run 1: ExampleRun1.txt:**

1. Provide the inputs to create a non-wrapping Dungeon with 0 interconnectivity and 6 rows and
   columns, and with 30% of caves with some treasure and caves and tunnels with arrows and 30% of
   caves with Monsters with the following command:
   `cd res`
   `java -jar Project5.jar 6 6 false 2 100 20`
   
2. This will begin the game. To continue the game and to pick up any available treasure or arrows,
   move the player in a Dungeon, or shoot a monster close by when a pungent smell is found, follow
   the commands in the instructions (M for move, P for pickup, and S for shoot).

### **Design/Model Changes**

From the original model some the major changes that were made are as follows:

- Repackaged the class into different packages to make it better organized.
- Added enums for smell, player and monster status, weapons (arrows), and cell types (tunnel and
  cave).
- Added a new class for a Monster, and added methods and attributes in Cell, player, and Game
  classes to accommodate the new requirements for shooting an arrow, picking an arrow, and killing
  the player or a monster.
- Some of the redundant methods were removed as they were identified during implementation and
  testing, and combined into a common class to extend from.

### **Assumptions and Limitations**

The project design includes some assumptions that might have added some limitations to the program.

The program assumptions and / constraints are as follows:

- **Player**:
    - Only one player can be in a Dungeon at a time.
    - A new game has to be started to play a game with another player.
    - The player may stop by a tunnel while making a move within the Dungeon.
- **Start and end locations**
    - It is required that the minimum distance between cells is 5 nodes. To enforce this criteria,
      an approximation is made using the manhattan distance formula for non-wrapping Dungeon case.
      For wrapping case, the formula is used for torquoidal space.
    - The selection of these locations is done by randomly selecting nodes, and when this criteria
      is not met for those particular nodes, the user has to try to create the Dungeon again using
      the same dimensions or bigger dimensions.
- **Monster**:
    - The monsters are placed randomly based on the percentage of caves with monster, and input
      provided from the command line. So the input is assumed to be a percentage and not the number
      of monsters.

### **Citations**

#### Image

* https://stackoverflow.com/questions/2935232/show-animated-gif
* https://stackoverflow.com/questions/16343098/resize-a-picture-to-fit-a-jlabel/16345968

#### Inputs

* https://www.codejava.net/java-core/the-java-language/java-8-lambda-listener-example
* https://stackoverflow.com/questions/8961938/java-keylistener-not-registering-arrow-keys
* https://stackoverflow.com/questions/67066490/keylistener-how-to-handle-multiple-keys-pressed
* https://stackoverflow.com/questions/12873657/how-do-i-convert-key-code-to-char-or-string-to-show-it-on-textview

#### Draw String (g2d):

* https://programming.guide/java/drawing-multiline-strings-with-graphics.html

#### JMenu:

* https://www.geeksforgeeks.org/java-swing-jmenubar/

#### Lambda Expression

* https://www.codejava.net/java-core/the-java-language/java-8-lambda-listener-example

#### JScrollPane:

* https://stackoverflow.com/questions/11919941/add-scrollpane-to-jpanel-when-the-panel-is-full-java

#### Layouts:

* http://pawlan.com/monica/articles/awtlayout/#CARD
* https://stackoverflow.com/questions/1534889/how-to-make-jlabels-start-on-the-next-line

#### Hashmap:

* https://www.baeldung.com/java-copy-hashmap
* https://www.baeldung.com/java-iterate-map
* https://stackoverflow.com/questions/63652414/if-hashmap-only-contains-one-key-is-there-a-way-to-get-that-key-without-knowing

#### Stream:

* https://howtodoinjava.com/java8/stream-anymatch-example/
* https://stackoverflow.com/questions/32335335/how-to-remove-elements-from-a-list-with-lambda-based-on-another-list

#### Enums:

* Reference1: <br> https://stackoverflow.com/questions/36010167/can-i-have-enums-with-parameters
* Reference2: <br> https://stackoverflow.com/questions/142420/java-enum-parameter-in-method
*

Reference3: <br> https://stackoverflow.com/questions/6692664/how-to-get-enum-value-from-index-in-java

#### Data structures in Java: Iterating and removing duplicates

* Reference1: <br>https://www.geeksforgeeks.org/iterate-map-java/
*

Reference2: <br>https://stackoverflow.com/questions/10370750/removing-duplicate-elements-from-a-list

#### Checking for Null in Java:

* Reference1: <br> https://www.wikihow.com/Check-Null-in-Java
* Reference2: <br> https://stackoverflow.com/questions/2033088/java-how-to-check-if-object-is-null

#### Passing arguments to jar file:

*

Reference1: <br> https://stackoverflow.com/questions/8770652/run-jar-file-with-command-line-arguments

#### Creating a list from range:

* https://stackoverflow.com/questions/16711147/populating-a-list-with-a-contiguous-range-of-integers

#### Switch statements:

* https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html