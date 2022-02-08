

/**
 *
 * @author revan
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author revan
 */

//class that stores the corresponding co ordinates of cell(vertice)
//Say 14 will have (x,y,z) as (1,1,1)
class cell{
    int x,y,z;
    //constructor for intializing x,y,z with corresponding values
    cell(int x,int y,int z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
}

//Main class in which the execution is carried out
public class TicTacToe {
    //3d array that stores magic cube values
    static int[][][] magic_cube=new int[3][3][3];
    //Hashmap that maps magic value with it's coordinates
    static HashMap<Integer,cell> hash=new HashMap<Integer,cell>();
    //Arraylist that stores the cells marked by human while playing
    static ArrayList<Integer> human=new ArrayList<Integer>();
    //Arraylist that stores the cells marked by computer while playing
    static ArrayList<Integer> computer=new ArrayList<Integer>();
    
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_RED = "\u001B[31m";
    public static final String TEXT_BLUE = "\u001B[34m";
    
    //Main function
    public static void main(String args[])
    {   
        Scanner sc=new Scanner(System.in);
        generateCube(3);
        hashmapmagic();
        char choice;
        int f,g,h,temp;
        //Asks if user wants to play first and stores his choice
        System.out.println("Would you like to play first Y/N: ");
        choice=sc.next().charAt(0);
        int i=0;
        //check if number of moves are < 27 and no.of lines won by human
        //and computer are less than 10
        while(i<27&&hscore()<10&&cscore()<10){
            if(choice=='Y'){
                //if choice is yes user input is taken first
                System.out.println("Enter a value: ");
                temp=sc.nextInt();
                if(human.contains(temp)||computer.contains(temp)){
                    System.out.println("Element already marked\n");
                    continue;      
                }              
                human.add(temp);
                i++;
                if(i>=27||hscore()>=10){
                    printboard(choice);
                    break;
                }
                if(cpossiblewin()!=0){
                    System.out.println("Computer Choice1: "+cpossiblewin());
                    computer.add(cpossiblewin());
                }
                else if(hpossiblewin()!=0){
                    System.out.println("Computer Choice2: "+hpossiblewin());
                    computer.add(hpossiblewin());
                }
                else{
                    temp=computer_choice();
                    System.out.println("Computer Choice3: "+temp);
                    computer.add(temp);
                }
                i++;              
            }
            else{
                if(cpossiblewin()!=0){
                    System.out.println("Computer Choice1: "+cpossiblewin());
                    computer.add(cpossiblewin());
                }
                else if(hpossiblewin()!=0){
                    System.out.println("Computer Choice2: "+hpossiblewin());
                    computer.add(hpossiblewin());
                }
                else{
                    temp=computer_choice();
                    System.out.println("Computer Choice3: "+temp);
                    computer.add(temp);
                }
                i++;
                if(i>=27||cscore()>=10){
                    printboard(choice);
                    break;
                }
                printboard(choice);
                System.out.println("Computer Score: "+cscore());
                System.out.println("Human Score: "+hscore());
                System.out.println("Enter a value: ");
                temp=sc.nextInt();
                if(human.contains(temp)||computer.contains(temp)){
                    System.out.println("Element already marked\n");
                    continue;
                }                    
                human.add(temp);
                i++;
            }
            printboard(choice);
            System.out.println("Computer Score: "+cscore());
            System.out.println("Human Score: "+hscore());
            
            printmagic();
        }
        System.out.println("Computer Score: "+cscore());
        System.out.println("Human Score: "+hscore());
        if(cscore()>hscore())
                System.out.println("\nCOMPUTER WINS\n");
        else if(hscore()>cscore())
                System.out.println("\nHUMAN WINS\n");
        else
                System.out.println("\nIT'S A DRAW\n");
    }
    
    //function to print TIC TAC TOE board when called
    static void printboard(char choice){
        int f,g,h;
        for(f=0;f<3;f++)
            { 
                if(f>0)
                    System.out.print("|     ---------------\n");
                else
                    System.out.print("     ---------------\n");
                for(g=0;g<3;g++)
                {
                    if(f>0)
                        System.out.print("|");
                    if(g%3==0)
                    System.out.print("   /");
                    else if(g%3==1)
                        System.out.print("  /");
                    else
                        System.out.print(" /");
                     for(h=0;h<3;h++){
                         //check if cell is marked by human
                         if(human.contains(magic_cube[f][g][h])){
                             //If choice is yes then the identifier for human is "X"
                             if(choice=='Y')
                                 System.out.print(" "+TEXT_BLUE+"X"+TEXT_RESET+"  /");
                             //If choice is yes then the identifier for human is "O"
                             else
                                 System.out.print(" "+TEXT_RED+"O"+TEXT_RESET+"  /");
                         }
                          //check if cell is marked by computer
                         else if(computer.contains(magic_cube[f][g][h])){
                             //If choice is no then the identifier for computer is "X"
                             if(choice=='N')
                                 System.out.print(" "+TEXT_BLUE+"X"+TEXT_RESET+"  /");
                             //If choice is yes then the identifier for computer is "O"
                             else
                                 System.out.print(" "+TEXT_RED+"O"+TEXT_RESET+"  /");
                         }
                         //If element is not marked by human or computer print blank space
                         else
                             System.out.print("    /");                      
                     }
                     if(f<1&&g%3==0)
                         System.out.print(" |");
                     else if(f<1&&g%3==1)
                         System.out.print("  |");
                     else if(f<1)
                         System.out.print("   |");
                     else if(f<2&&g%3==0)
                         System.out.print("|");
                     else if(f<2&&g%3==1)
                         System.out.print(" |");
                     else if(f<2)
                         System.out.print("  |");
                    System.out.print("\n");
                }
                if(f<2)
                    System.out.print(" ---------------    |");
                else
                    System.out.print(" ---------------");
               System.out.print("\n");
            }
    }
    
    //checks if three points are collinear
    static boolean collinear(int a,int b,int c){
        cell tempa=hash.get(a);
        cell tempb=hash.get(b);
        cell tempc=hash.get(c);
        int dx1=tempb.x-tempa.x;
        int dx2=tempc.x-tempa.x;
        int dy1=tempb.y-tempa.y;
        int dy2=tempc.y-tempa.y;
        int dz1=tempb.z-tempa.z;
        int dz2=tempc.z-tempa.z;
        double num=Math.abs(dx1*dx2+dy1*dy2+dz1*dz2);
        int modab=(dx1*dx1+dy1*dy1+dz1*dz1);
        int modac=(dx2*dx2+dy2*dy2+dz2*dz2);
        double den=Math.sqrt(modab*modac);
        if(num==den)
            return true;
        else
            return false;        
    }
    
    //check if three points form a legal line
    //i.e those points are collinear and they sum to 42
    static boolean legalline(int x,int y,int z){
        if(x>27||y>27||z>27)
            return false;
        else if((x==y)||(y==z)||(z==x))
            return false;
        else if((x+y+z)==42&&collinear(x,y,z))
            return true;
        else 
            return false;
    }
    
    //check if computer can score a line by marking a single cell
    //If yes returns the value of cell so that we can win or if not returns 0
    static int cpossiblewin(){
        for(int i=0;i<computer.size();i++){
            for(int j=i+1;j<computer.size();j++){
                if(computer.contains(42-computer.get(j)-computer.get(i)))
                    continue;
                if(computer.get(i)+computer.get(j)>=42)
                    continue;
                //checks if a pair of values have a corresponding value by marking 
                //which we can score a line For Ex:(27,14) have 1
                //Also check if points are collinear and if desired point has already been marked
                if(legalline(computer.get(i),computer.get(j),(42-computer.get(j)-computer.get(i)))){
                    if(!human.contains(42-computer.get(j)-computer.get(i)))
                        return (42-computer.get(j)-computer.get(i));
                }
            }
        }
        return 0;
    }
    
    //check if human can score a line by marking a single cell
    //If yes returns the value of cell so that we can block it or if not returns 0
    static int hpossiblewin(){
        for(int i=0;i<human.size();i++){
            for(int j=i+1;j<human.size();j++){
                if(human.contains(42-human.get(j)-human.get(i)))
                    continue;
                if(human.get(i)+human.get(j)>=42)
                    continue;
                //checks if a pair of values have a corresponding value by marking 
                //which we can block a line For Ex:(27,14) have 1
                //Also check if points are collinear and if desired point has already been marked
                if(legalline(human.get(i),human.get(j),(42-human.get(j)-human.get(i)))){
                    if(!computer.contains(42-human.get(j)-human.get(i)))
                        return (42-human.get(j)-human.get(i));
                }
            }
        }
        return 0;
    }
    
    //Returns the rank of the input cell 
    //criteria for ranking is detailed in the function    
    static int rank(int n)
    {
        //RANKING CRITERIA:
        //Rank is defined as the no.of lines passing through the given cell which
        //does not have human cell in it + the no.of lines passing through given cell
        //which only have computer cell in it
        
        //Ex: Computer marked 14 and human marked 10
        //Rank of 8 = 4 = 1(8,12,22 line)+ 1(8,15,19 line)+0(because 8,24,10 has been already marked by human)+ 1(8,14,17 line)+1(8,14,17 line)(because 14 is already marked by computer we have advantage in marking 8)
        
        //14 is the center cell of the magic cube and 13 lines pass through it
        //hence it returns rank of 13
        if(n==14)
            return 13;
        else
        {
           cell temp=hash.get(n);
           int rank=0,weight=0;
           int i=temp.x,j=temp.y,k=temp.z;
           //checks whether a human element is present in the corresponding row 
           //if present row doesn't contribute towards rank
           //else if not present increments by 1
           //else if not present and also has a computer element rank is increased by 2
           if(i==0)
           {
               while(++i<3)
               {
                  if(human.contains(magic_cube[i][j][k]))
                      break;
                  else if(computer.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(i==2)
                      rank=rank+1+weight;    
               }
           }
           else if(i==2)
           {
               while(--i>=0)
               {
                  if(human.contains(magic_cube[i][j][k]))
                      break;
                  else if(computer.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(i==0)
                      rank=rank+1+weight;      
               }
           }
           else
           {
              if((computer.contains(magic_cube[i+1][j][k]) || computer.contains(magic_cube[i-1][j][k])))
                      weight=1;
               if(!(human.contains(magic_cube[i+1][j][k]) || human.contains(magic_cube[i-1][j][k])) )
                  rank=rank+1+weight;          
           }
           i=temp.x;
           j=temp.y;
           k=temp.z;
           weight=0;
           //checks whether a human element is present in the corresponding coloumn 
           //if present coloumn doesn't contribute towards rank
           //else if not present increments by 1
           //else if not present and also has a computer element rank is increased by 2
           if(j==0)
           {
               while(++j<3)
               {
                  if(human.contains(magic_cube[i][j][k]))
                      break;
                  else if(computer.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(j==2)
                      rank=rank+1+weight;
               }
           }
           else if(j==2)
           {
               while(--j>=0)
               {
                  if(human.contains(magic_cube[i][j][k]))
                      break;
                  else if(computer.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(j==0)
                      rank=rank+1+weight;     
               }
           }
           else
           {
               if((computer.contains(magic_cube[i][j+1][k]) || computer.contains(magic_cube[i][j-1][k])))
                      weight=1;
               if(!(human.contains(magic_cube[i][j+1][k]) || human.contains(magic_cube[i][j-1][k])) )
                      rank=rank+1+weight;
           }
           i=temp.x;
           j=temp.y;
           k=temp.z;
           weight=0;
           //checks whether a human element is present in the corresponding pillar
           //if present pillar doesn't contribute towards rank
           //else if not present increments by 1
           //else if not present and also has a computer element rank is increased by 2
           if(k==0)
           {
               while(++k<3)
               {
                  if(human.contains(magic_cube[i][j][k]))
                      break;
                  else if(computer.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(k==2)
                     rank=rank+1+weight;      
               }
           }
           else if(k==2)
           {
               while(--k>=0)
               {
                  if(human.contains(magic_cube[i][j][k]))
                      break;
                  else if(computer.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(k==0)
                      rank=rank+1+weight;
               }
           }
           else
           {
               if((computer.contains(magic_cube[i][j][k+1]) || computer.contains(magic_cube[i][j][k-1])))
                      weight=1;
               if(!(human.contains(magic_cube[i][j][k+1]) || human.contains(magic_cube[i][j][k-1])) )
                     rank=rank+1+weight;
           
           }
           i=temp.x;
           j=temp.y;
           k=temp.z;
           weight=0;
           //checks whether a human element is present in the corresponding diagonal
           //if present diagonal doesn't contribute towards rank
           //else if not present increments by 1
           //else if not present and also has a computer element rank is increased by 2
           if(!((i==1&&j==1) || (j==1&&k==1) || (i==1&&k==1)))
           {
               if(i==0)
                   i=2;
               else if(i==2)
                   i=0;
               if(j==0)
                   j=2;
               else if(j==2)
                   j=0;
               if(k==0)
                   k=2;
               else if(k==2)
                   k=0;
               if((computer.contains(magic_cube[i][j][k]) || computer.contains(magic_cube[1][1][1])))
                       weight=1;
               if(!(human.contains(magic_cube[i][j][k]) || human.contains(magic_cube[1][1][1])))
                       rank=rank+1+weight;
           }
           return rank;
        }
    }
    
     static int hrank(int n)
    {
        //RANKING CRITERIA:
        //Rank is defined as the no.of lines passing through the given cell which
        //does not have human cell in it + the no.of lines passing through given cell
        //which only have computer cell in it
        
        //Ex: Computer marked 14 and human marked 10
        //Rank of 8 = 4 = 1(8,12,22 line)+ 1(8,15,19 line)+0(because 8,24,10 has been already marked by human)+ 1(8,14,17 line)+1(8,14,17 line)(because 14 is already marked by computer we have advantage in marking 8)
        
        //14 is the center cell of the magic cube and 13 lines pass through it
        //hence it returns rank of 13
        if(n==14)
            return 13;
        else
        {
           cell temp=hash.get(n);
           int rank=0,weight=0;
           int i=temp.x,j=temp.y,k=temp.z;
           //checks whether a human element is present in the corresponding row 
           //if present row doesn't contribute towards rank
           //else if not present increments by 1
           //else if not present and also has a computer element rank is increased by 2
           if(i==0)
           {
               while(++i<3)
               {
                  if(computer.contains(magic_cube[i][j][k]))
                      break;
                  else if(human.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(i==2)
                      rank=rank+1+weight;    
               }
           }
           else if(i==2)
           {
               while(--i>=0)
               {
                  if(computer.contains(magic_cube[i][j][k]))
                      break;
                  else if(human.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(i==0)
                      rank=rank+1+weight;      
               }
           }
           else
           {
              if((human.contains(magic_cube[i+1][j][k]) || human.contains(magic_cube[i-1][j][k])))
                      weight=1;
               if(!(computer.contains(magic_cube[i+1][j][k]) || computer.contains(magic_cube[i-1][j][k])) )
                  rank=rank+1+weight;          
           }
           i=temp.x;
           j=temp.y;
           k=temp.z;
           weight=0;
           //checks whether a human element is present in the corresponding coloumn 
           //if present coloumn doesn't contribute towards rank
           //else if not present increments by 1
           //else if not present and also has a computer element rank is increased by 2
           if(j==0)
           {
               while(++j<3)
               {
                  if(computer.contains(magic_cube[i][j][k]))
                      break;
                  else if(human.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(j==2)
                      rank=rank+1+weight;
               }
           }
           else if(j==2)
           {
               while(--j>=0)
               {
                  if(computer.contains(magic_cube[i][j][k]))
                      break;
                  else if(human.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(j==0)
                      rank=rank+1+weight;     
               }
           }
           else
           {
               if((human.contains(magic_cube[i][j+1][k]) || human.contains(magic_cube[i][j-1][k])))
                      weight=1;
               if(!(computer.contains(magic_cube[i][j+1][k]) || computer.contains(magic_cube[i][j-1][k])) )
                      rank=rank+1+weight;
           }
           i=temp.x;
           j=temp.y;
           k=temp.z;
           weight=0;
           //checks whether a human element is present in the corresponding pillar
           //if present pillar doesn't contribute towards rank
           //else if not present increments by 1
           //else if not present and also has a computer element rank is increased by 2
           if(k==0)
           {
               while(++k<3)
               {
                  if(computer.contains(magic_cube[i][j][k]))
                      break;
                  else if(human.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(k==2)
                     rank=rank+1+weight;      
               }
           }
           else if(k==2)
           {
               while(--k>=0)
               {
                  if(computer.contains(magic_cube[i][j][k]))
                      break;
                  else if(human.contains(magic_cube[i][j][k]))
                      weight=1;
                  else if(k==0)
                      rank=rank+1+weight;
               }
           }
           else
           {
               if((human.contains(magic_cube[i][j][k+1]) || human.contains(magic_cube[i][j][k-1])))
                      weight=1;
               if(!(computer.contains(magic_cube[i][j][k+1]) || computer.contains(magic_cube[i][j][k-1])) )
                     rank=rank+1+weight;
           
           }
           i=temp.x;
           j=temp.y;
           k=temp.z;
           weight=0;
           //checks whether a human element is present in the corresponding diagonal
           //if present diagonal doesn't contribute towards rank
           //else if not present increments by 1
           //else if not present and also has a computer element rank is increased by 2
           if(!((i==1&&j==1) || (j==1&&k==1) || (i==1&&k==1)))
           {
               if(i==0)
                   i=2;
               else if(i==2)
                   i=0;
               if(j==0)
                   j=2;
               else if(j==2)
                   j=0;
               if(k==0)
                   k=2;
               else if(k==2)
                   k=0;
               if((human.contains(magic_cube[i][j][k]) || human.contains(magic_cube[1][1][1])))
                       weight=1;
               if(!(computer.contains(magic_cube[i][j][k]) || computer.contains(magic_cube[1][1][1])))
                       rank=rank+1+weight;
           }
           return rank;
        }
    }
    
    //compute a suitable choice for computer in case there are no possible wins
    //or losses that computer can win or block respectively
    static int computer_choice(){
        int i,max=-1,element=0,temp,temph,hmax=-1,helement=0;
        for(i=1;i<=27;i++){
            if(human.contains(i)||computer.contains(i))
                continue;
            temp=rank(i);
            temph=hrank(i);
            //Element having maximum rank is returned
            if(max<temp){
                max=temp;
                element=i;
            }
            if(hmax<temph){
                hmax=temph;
                helement=i;
            }            
        }
        if(hmax>max)
            return helement;
        else
            return element;
    }
    
    //checks the number of lines computer was able to win
    static int cscore(){
        int score =0;
        //creating a nested loop for checking if a given triplet forms a legalline
        //if line is formed score is increased by one
        for(int i=0;i<computer.size();i++){
            for(int j=i+1;j<computer.size();j++){
                for(int k=j+1;k<computer.size();k++){
                    if(legalline(computer.get(i),computer.get(j),computer.get(k)))
                        score++;
                }
            }
        }
        //return the score of human after running the function
        return score;
    }
    
    //checks the number of lines human was able to win
    static int hscore(){
        int score =0;
        //creating a nested loop for checking if a given triplet forms a legalline
        //if line is formed score is increased by one
        for(int i=0;i<human.size();i++){
            for(int j=i+1;j<human.size();j++){
                for(int k=j+1;k<human.size();k++){
                    if(legalline(human.get(i),human.get(j),human.get(k)))
                        score++;
                }
            }
        }
        //return the score of human after running the function
        return score;
    }
    
    //prints hashmap so that you can check if hashmap is being stored properly
    static void printhash(){
        for(int i:hash.keySet()){
            cell temp=hash.get(i);
            System.out.println(i+" "+temp.x+" "+temp.y+" "+temp.z);
        }
    }
    
    //prints the magic cube onto the screen
    static void printmagic(){
        int f,g,h;
        for(f=0;f<3;f++)
        {
            for(g=0;g<3;g++)
            {
                 for(h=0;h<3;h++){
                  System.out.print(magic_cube[f][g][h]+"\t");
                 }
                System.out.print("\n");
            }
           System.out.print("\n\n");
        }
    }
    
    //prints the magic cube onto the screen
    //It also maps magic cube values with corresponding coordinates in Hashmap
    static void hashmapmagic(){
        int f,g,h;
        for(f=0;f<3;f++)
        {
            for(g=0;g<3;g++)
            {
                 for(h=0;h<3;h++){
                  System.out.print(magic_cube[f][g][h]+"\t");
                  cell temp=new cell(f,g,h);
                  hash.put(magic_cube[f][g][h], temp);
                 }
                System.out.print("\n");
            }
           System.out.print("\n\n");
        }
    }
    
    //This functions initializes 3d array which stores the magic cube values
    static void generateCube(int n)
    {  
        //The First element is stored at location (n/2,n/2,0) 
        int i=n/2,j=n/2,k=0;
        int num;
        //1. subtract the x co-ordinate by 1 
        //2. subtract the y co-ordinate only when the number if divided by n leaves remainder as 1
        //3. subtract the z co-ordinate by 1  except in the case where if the number when divided by n leaves the remainder as 1 then you add the z co-ordinate by 1.  

        //In any of the above cases if you get any co-ordinate value as -1 then 
       //replace it by n-1 and if you get any co-ordinate as n then replace it with 0. 
        for(num=1;num<=n*n*n;num++)
        {
           magic_cube[i][j][k]=num;
           if(num%n==0)
           {
               i--;
               if(i==-1)
                   i=n-1;
               j--;
               if(j==-1)
                   j=n-1;
               k++;
               if(k==n)
                   k=0;
           }
           else
           {
               i--;
               if(i==-1)
                   i=n-1;
               k--;
               if(k==-1)
                   k=n-1;
           }
           // if you get x and y co-ordinate as equal and z as 0 then increase the x and y co-ordinate by 1.
           //if you get x and y co-ordinate as n and z as 0 then replace x and y co-ordinate with 0.

           if(i==j && k==0)
           {
               i++;
               if(i==n)
                   i=0;
               j++;
               if(j==n)
                   j=0;
           }
           
        }
    }    
}
