public class Customer {
    private String name;
    private String address;
    private String personalId;
    private String phoneNumber;
    private String email;

    public Customer(String name, String address, String personalId, String phoneNumber, String email) {
        this.name = name;
        this.address = address;
        this.personalId = personalId;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Getter och setter för name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter och setter för address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter och setter för personalId
    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    // Getter och setter för phoneNumber
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Getter och setter för email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}