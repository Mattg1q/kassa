import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerAndMembershipTests {

    @Test
    void testCustomerInformation() {
        // Arrange
        Customer customer = new Customer("John Doe", "123 Main St", "123456789", "555-1234", "john.doe@example.com");

        // Act & Assert
        assertEquals("John Doe", customer.getName(), "Customer name should be 'John Doe'");
        assertEquals("123 Main St", customer.getAddress(), "Customer address should be '123 Main St'");
        assertEquals("123456789", customer.getPersonalId(), "Customer personal ID should be '123456789'");
        assertEquals("555-1234", customer.getPhoneNumber(), "Customer phone number should be '555-1234'");
        assertEquals("john.doe@example.com", customer.getEmail(), "Customer email should be 'john.doe@example.com'");
    }

    @Test
    void testCustomerUpdateInformation() {
        // Arrange
        Customer customer = new Customer("Jane Doe", "456 Elm St", "987654321", "555-5678", "jane.doe@example.com");

        // Act
        customer.setName("Jane Smith");
        customer.setAddress("789 Pine St");
        customer.setPhoneNumber("555-8765");
        customer.setEmail("jane.smith@example.com");

        // Assert
        assertEquals("Jane Smith", customer.getName(), "Customer name should be updated to 'Jane Smith'");
        assertEquals("789 Pine St", customer.getAddress(), "Customer address should be updated to '789 Pine St'");
        assertEquals("555-8765", customer.getPhoneNumber(), "Customer phone number should be updated to '555-8765'");
        assertEquals("jane.smith@example.com", customer.getEmail(), "Customer email should be updated to 'jane.smith@example.com'");
    }

    @Test
    void testMembershipPoints() {
        // Arrange
        Customer customer = new Customer("John Doe", "123 Main St", "123456789", "555-1234", "john.doe@example.com");
        Membership membership = new Membership(customer);

        // Act
        membership.addPoints(100);
        boolean redeemed = membership.redeemPoints(50);
        boolean failedRedemption = membership.redeemPoints(100);

        // Assert
        assertEquals(50, membership.getPoints(), "Points should update correctly after redemption");
        assertTrue(redeemed, "Redemption should succeed when sufficient points are available");
        assertFalse(failedRedemption, "Redemption should fail when insufficient points are available");
    }

    @Test
    void testMembershipAddPoints() {
        // Arrange
        Customer customer = new Customer("John Doe", "123 Main St", "123456789", "555-1234", "john.doe@example.com");
        Membership membership = new Membership(customer);

        // Act
        membership.addPoints(200);

        // Assert
        assertEquals(200, membership.getPoints(), "Membership points should increase to 200 after adding points");
    }

    @Test
    void testCustomerSettersAndGetters() {
        // Arrange
        Customer customer = new Customer("", "", "", "", "");

        // Act
        customer.setName("Alice Wonderland");
        customer.setAddress("Wonderland Lane 1");
        customer.setPhoneNumber("555-6789");
        customer.setEmail("alice@wonderland.com");

        // Assert
        assertEquals("Alice Wonderland", customer.getName(), "Customer name should be 'Alice Wonderland'");
        assertEquals("Wonderland Lane 1", customer.getAddress(), "Customer address should be 'Wonderland Lane 1'");
        assertEquals("555-6789", customer.getPhoneNumber(), "Customer phone number should be '555-6789'");
        assertEquals("alice@wonderland.com", customer.getEmail(), "Customer email should be 'alice@wonderland.com'");
    }

    @Test
    void testMembershipNegativePoints() {
        // Arrange
        Customer customer = new Customer("John Doe", "123 Main St", "123456789", "555-1234", "john.doe@example.com");
        Membership membership = new Membership(customer);

        // Act
        membership.addPoints(-100);

        // Assert
        assertEquals(-100, membership.getPoints(), "Membership should allow negative points for testing edge cases");
    }

    @Test
    void testMembershipRedeemZeroPoints() {
        // Arrange
        Customer customer = new Customer("John Doe", "123 Main St", "123456789", "555-1234", "john.doe@example.com");
        Membership membership = new Membership(customer);

        // Act
        boolean redeemed = membership.redeemPoints(0);

        // Assert
        assertTrue(redeemed, "Redeeming zero points should always succeed");
        assertEquals(0, membership.getPoints(), "Membership points should remain unchanged after redeeming zero points");
    }

    @Test
    void testCustomerEdgeCaseEmptyName() {
        // Arrange
        Customer customer = new Customer("", "123 Main St", "123456789", "555-1234", "john.doe@example.com");

        // Assert
        assertEquals("", customer.getName(), "Customer name should allow empty strings");
    }

    @Test
    void testMembershipWithNullCustomer() {
        // Arrange
        Membership membership = new Membership(null);

        // Assert
        assertNull(membership.getMember(), "Membership should allow null customers for testing edge cases");
    }
}
