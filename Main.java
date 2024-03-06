import java.util.*;

class Passenger{
    static int id=1;
    String name;
    int age;
    String berth;
    String allocted;
    int passengerId;
    int number;
    Passenger(String name, int age, String berth){
        this.name=name;
        this.age=age;
        this.berth=berth;
        this.passengerId=id++;
        allocted="";
        number=-1;
    }
}

class TicketBooker{
    static int availableLowerBerths =1;
    static int availableMiddleBerths =1;
    static int availableUpperBerths =1;
    static int availableRacTickets =1;
    static int availableWaitingList =1;

    static ArrayList<Integer> lowerBerthPosition = new ArrayList<>(Arrays.asList(1));
    static ArrayList<Integer> middleBerthPosition = new ArrayList<>(Arrays.asList(1));
    static ArrayList<Integer> upperBerthPosition = new ArrayList<>(Arrays.asList(1));
    static ArrayList<Integer> racPosition = new ArrayList<>(Arrays.asList(1));
    static ArrayList<Integer> waitingPosition = new ArrayList<>(Arrays.asList(1));

    static Queue<Integer> racList= new LinkedList<>();
    static Queue<Integer> waitingList= new LinkedList<>();

    static ArrayList<Integer> bookedTickets =new ArrayList<>();
    static HashMap<Integer, Passenger> passengers=new HashMap<>();


    public void Ticket(Passenger P, int position, String berth){
        P.number=position;
        P.allocted=berth;
        bookedTickets.add(P.passengerId);
        passengers.put(P.passengerId, P);
        System.out.println("Ticket Booked Successfully");
    }

    void addToRac(Passenger P, int position, String berth){
        P.number=position;
        P.allocted=berth;
        racList.add(P.passengerId);
        passengers.put(P.passengerId,P);
        System.out.println("Added to RAC Successfully");
    }

    void addToWaiting(Passenger P, int position, String berth){
        P.number=position;
        P.allocted=berth;
        waitingList.add(P.passengerId);
        passengers.put(P.passengerId, P);
        System.out.println("Added to Waiting list Successfully");
    }

    void cancelTickets(int id){
        Passenger passengerDetail=passengers.get(id);
        
        int positionNo=passengerDetail.number;
        String berthHaving=passengerDetail.berth;
        passengers.remove(id);
        bookedTickets.remove(Integer.valueOf(id));
        
        System.out.println("Canceled Successfully");

        if(passengerDetail.allocted.equals("L")){
            lowerBerthPosition.add(positionNo);
            availableLowerBerths++;
        }else if(passengerDetail.allocted.equals("M")){
            middleBerthPosition.add(positionNo);
            availableMiddleBerths++;
        }else if(passengerDetail.allocted.equals("M")){
            upperBerthPosition.add(positionNo);
            availableUpperBerths++;
        }

        if(racList.size()>0){
            Passenger newPassenger=passengers.get(racList.poll());
            racPosition.add(newPassenger.number);
            passengers.remove(newPassenger.passengerId);
            availableRacTickets++;

            if(waitingList.size()>0){
                Passenger newRac=passengers.get(waitingList.poll());
                waitingPosition.add(newRac.number);
                passengers.remove(newRac.passengerId);
                availableWaitingList++;

                newPassenger.number=racPosition.get(0);
                newPassenger.allocted="RAC";
                racList.add(newPassenger.passengerId);
                racPosition.remove(0);

                availableWaitingList++;
                availableRacTickets--;
            }
            Main.bookTicket(newPassenger);
        }

    }

    void viewBooked(){
        for(Passenger p: passengers.values()){
            System.out.println("------------------------------");
            System.out.println("Passenger ID: "+p.passengerId);
            System.out.println("Name: "+p.name);
            System.out.println("Age: "+p.age);
            System.out.println("Status: "+p.number+p.allocted);
        }
    }


}
public class Main{
    static void bookTicket(Passenger P){
        TicketBooker book=new TicketBooker();
        if(book.availableWaitingList==0){
            System.out.println("No ticket available");
            return;
        }

        if((P.berth.equals("L") && book.availableLowerBerths>0) || (P.berth.equals("M") && book.availableMiddleBerths>0) || (P.berth.equals("U") && book.availableUpperBerths>0)){
            System.out.println("Prefered berth available");
            if(P.berth.equals("L")){
                System.out.println("Lower Berth Given");
                book.Ticket(P, (book.lowerBerthPosition.get(0)), "L");
                book.lowerBerthPosition.remove(0);
                book.availableLowerBerths--;
            }else if(P.berth.equals("M")){
                System.out.println("Middle Berth Given");
                book.Ticket(P, (book.middleBerthPosition.get(0)), "M");
                book.middleBerthPosition.remove(0);
                book.availableMiddleBerths--;
            }else{
                System.out.println("Upper Berth Given");
                book.Ticket(P, (book.upperBerthPosition.get(0)), "U");
                book.upperBerthPosition.remove(0);
                book.availableUpperBerths--;
            }
        }else if(book.availableLowerBerths>0){
            System.out.println("Lower Berth Given");
            book.Ticket(P, (book.lowerBerthPosition.get(0)), "L");
            book.lowerBerthPosition.remove(0);
            book.availableLowerBerths--;
        }else if(book.availableMiddleBerths>0){
            System.out.println("Middle Berth Given");
            book.Ticket(P, (book.middleBerthPosition.get(0)), "M");
            book.middleBerthPosition.remove(0);
            book.availableMiddleBerths--;
        }else if(book.availableUpperBerths>0){
            System.out.println("Upper Berth Given");
            book.Ticket(P, (book.upperBerthPosition.get(0)), "U");
            book.upperBerthPosition.remove(0);
            book.availableUpperBerths--;
        }else if(book.availableRacTickets>0){
            System.out.println("RacTickets Available");
            book.addToRac(P, (book.racPosition.get(0)), "RAC");
            book.racPosition.remove(0);
            book.availableRacTickets--;
        }else if(book.availableWaitingList>0){
            System.out.println("Waiting List Available");
            book.addToWaiting(P, (book.waitingPosition.get(0)), "WL");
            book.waitingPosition.remove(0);
            book.availableWaitingList--;
        }
    }

    static void cancelTicket(int id){
        TicketBooker book=new TicketBooker();
        if(!book.passengers.containsKey(id)){
            System.out.println("Passenger not found");
        }else{
            book.cancelTickets(id);
        }
    }

    static void availableTicktet(){
        TicketBooker book =new TicketBooker();
        System.out.println("Available Lower Berth"+book.availableLowerBerths);
        System.out.println("Available Middle Berth"+book.availableMiddleBerths);
        System.out.println("Available Upper Berth"+book.availableUpperBerths);
        System.out.println("Available RAC Tickets"+book.availableRacTickets);
        System.out.println("Available Waiting Tickets"+book.availableWaitingList);
    }

    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        while(true){
            System.out.println("\n 1. Book Ticket \n 2. Cancel Ticket \n 3. Available Tickets \n 4.Booked Tickets \n 5.Exit");
            int choice = sc.nextInt();
            switch(choice){
                case 1: 
                {
                    System.out.println("Enter passenger name");
                    String name=sc.next();
                    System.out.println("Enter age");
                    int age=sc.nextInt();
                    System.out.println("Enter berth preference (L, M, U)");
                    String berth=sc.next();
                    Passenger P=new Passenger(name, age, berth);
                    bookTicket(P);
                }
                break;

                case 2:
                {
                    System.out.println("Enter passenger id to cancel");
                    int id=sc.nextInt();
                    cancelTicket(id);
                }
                break;

                case 3:
                {
                    availableTicktet();
                }
                break;

                case 4:
                {
                    TicketBooker book=new TicketBooker();
                    book.viewBooked();
                }

                case 5:
                {
                    System.exit(0);
                }
            }
        }
    }
}