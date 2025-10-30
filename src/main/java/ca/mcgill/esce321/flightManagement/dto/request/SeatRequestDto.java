package ca.mcgill.esce321.flightManagement.dto.request;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.model.SeatClass;
import ca.mcgill.esce321.flightManagement.model.Flight;
import java.time.LocalDate;



public class SeatRequestDto {
    private SeatClass seatClass;
    // private double price;
    private String seatNumber;
    private Flight flight;
    // private LocalDate creationDate;
    

    

    @SuppressWarnings("unused")
    private SeatRequestDto() {

    }

    public SeatRequestDto(SeatClass seatClass, String seatNumber, Flight flight) {
            
        this.seatClass = seatClass;
        // this.price = price;
        this.seatNumber = seatNumber;
        // this.seatStatus = seatStatus;
        this.flight = flight;
        
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(SeatClass seatClass)  {
        this.seatClass = seatClass;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Flight getFlight() {
        return flight;

    }

    public void setFlight(Flight flight) {
        this.flight = flight;

    }
}




// // import jakarta.validation.constraints.Email;
// // import jakarta.validation.constraints.NotBlank;

// // what you're getting from front end
// public class ManagerRequestDto {

//     // @NotBlank(message = "Name cannot be blank")
//     private String firstName;
//     private String lastName; 
//     private String password;

//     // @Email(message = "Invalid email format")
//     private String email;

//     //maybe flights list

//     // Default constructor (needed by Jackson)
//     @SuppressWarnings("unused")
//     private ManagerRequestDto() {
//     }

//     public ManagerRequestDto(String firstName, String lastName, String email) {
//         this.firstName = firstName;
//         this.lastName = lastName;
//         this.email = email;
//         // this.password = password;
//     }

//     // Getters and setters
//     public String getFirstName() {
//         return firstName;
//     }

//     public String getLastName() {
//         return lastName;
//     }

//     public void setFirstName(String name) {
//         this.firstName = name;
//     }

//     public void setLastName(String name) {
//         this.lastName = name;
//     }

//     public String getEmail() {
//         return email;
//     }

//     public void setEmail(String email) {
//         this.email = email;
//     }

//     public String getPassword() {
//         return password;
//     }

//     public void setPassword(String password) {
//         this.password = password;
//     }
// }

//------------------

// package ca.mcgill.esce321.flightManagement.model;

// import jakarta.persistence.*;

// @Entity
// public class Seat {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
// import ca.mcgill.esce321.flightManagement.model.Flight;
// import ca.mcgill.esce321.flightManagement.model.Owner;
// import ca.mcgill.esce321.flightManagement.model.SeatClass;
// import ca.mcgill.esce321.flightManagement.model.SeatStatus;
//     private Long seatId;

//     @ManyToOne
//     @JoinColumn(name = "flight_id")
//     private Flight flight;

//     @ManyToOne
//     @JoinColumn(name = "owner_id", referencedColumnName = "id")
//     private Owner owner;

//     private SeatClass seatClass;
//     private double price;
//     private String seatNumber;
//     private SeatStatus seatStatus;

//     public Seat(){}
//     public Seat(SeatClass seatClass, double price, String seatNumber, SeatStatus seatStatus, Flight flight) {
//         this.seatClass = seatClass;
//         this.price = price;
//         this.seatNumber = seatNumber;
//         this.seatStatus = seatStatus;
//         this.flight = flight;
//     }

//     public Long getSeatId() {
//         return seatId;
//     }

//     public void setSeatId(Long seatId) {
//         this.seatId = seatId;
//     }

//     public SeatClass getSeatClass() {
//         return seatClass;
//     }

//     public void setSeatClass(SeatClass seatClass) {
//         this.seatClass = seatClass;
//     }

//     public double getPrice() {
//         return price;
//     }

//     public void setPrice(double price) {
//         this.price = price;
//     }

//     public String getSeatNumber() {
//         return seatNumber;
//     }

//     public void setSeatNumber(String seatNumber) {
//         this.seatNumber = seatNumber;
//     }

//     public SeatStatus getSeatStatus() {
//         return seatStatus;
//     }

//     public void setSeatStatus(SeatStatus seatStatus) {
//         this.seatStatus = seatStatus;
//     }

//     public Flight getFlight() {
//         return flight;
//     }

//     public void setFlight(Flight flight) {
//         this.flight = flight;
//     }

//     public Owner getOwner() {
//         return owner;
//     }

//     public void setOwner(Owner owner) {
//         this.owner = owner;
//     }