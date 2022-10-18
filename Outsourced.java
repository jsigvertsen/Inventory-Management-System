package sigvertsen.c482.model;

/** class Outsourced.java*/
public class Outsourced extends Part {
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * a method to set the company name to a Outsourced object
     * @param companyName is the Company Name to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * a method to get the company name of a Outsourced object
     * @return this.companyName returns the Company Name set to the Outsourced Object
     */
    public String getCompanyName() {
        return this.companyName;
    }
}
