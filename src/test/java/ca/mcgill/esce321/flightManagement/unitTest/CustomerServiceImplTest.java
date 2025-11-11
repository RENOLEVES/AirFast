package ca.mcgill.esce321.flightManagement.unitTest;

import ca.mcgill.esce321.flightManagement.dto.request.CustomerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.CustomerResponseDTO;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SuppressWarnings("null")
public class CustomerServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- Tests ----------

    @Test
    void testCreateCustomer() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setEmail("test@example.com");
        dto.setPassword("pass");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setMembershipNumber(001);

        Customer savedCustomer = new Customer(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName(), dto.getMembershipNumber());
        savedCustomer.setId(1L);

        when(personRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerResponseDTO result = customerService.createCustomer(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(001,result.getMembershipNumber());
        verify(personRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testFindCustomerById_existing() {
        Customer customer = new Customer("linceketchate@gmail.com", "Pass", "Lince", "Ketchate", 001);
        customer.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponseDTO result = customerService.findCustomerById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("linceketchate@gmail.com", result.getEmail());
        assertEquals("Pass", result.getPassword());
        assertEquals("Lince", result.getFirstName());
        assertEquals("Ketchate", result.getLastName());
        assertEquals(001, result.getMembershipNumber());
    }

    @Test
    void testFindCustomerById_notFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> customerService.findCustomerById(1L));
    }

    @Test
    void testFindAllCustomers() {
        Customer c1 = new Customer("e1", "p1", "f1", "l1", 001);
        c1.setId(1L);
        Customer c2 = new Customer("e2", "p2", "f2", "l2", 002);
        c2.setId(2L);

        when(personRepository.findAll()).thenReturn(List.of(c1, c2));

        List<CustomerResponseDTO> result = customerService.findAllCustomers();

        assertEquals(2, result.size());
    }

    @Test
    void testUpdateCustomer_existing() {
        Customer customer = new Customer("email@gmail.com", "Pass", "First", "Last", 001);
        customer.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(personRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setEmail("new@example.com");
        dto.setPassword("NewP");
        dto.setFirstName("NewF");
        dto.setLastName("NewL");
        dto.setMembershipNumber(007);

        CustomerResponseDTO result = customerService.updateCustomer(1L, dto);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
        assertEquals(007, result.getMembershipNumber());
    }

    @Test
    void testUpdateCustomer_notFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setEmail("error@example.com");
        dto.setPassword("Pass");
        dto.setFirstName("No");
        dto.setLastName("User");
        dto.setMembershipNumber(404);

        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(1L, dto));

        verify(personRepository, never()).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_existing() {
        Customer customer = new Customer("email@gmail.com", "Pass", "First", "Last", 001);
        customer.setId(1L);
        when(personRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertDoesNotThrow(() -> customerService.deleteCustomer(1L));
        verify(personRepository, times(1)).delete(customer);
    }

    @Test
    void testDeleteCustomer_notFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> customerService.deleteCustomer(1L));

        verify(personRepository, never()).delete(any(Customer.class));
    }
}
