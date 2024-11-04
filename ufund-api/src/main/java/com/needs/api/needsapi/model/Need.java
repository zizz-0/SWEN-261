package com.needs.api.needsapi.model;
import java.util.logging.Logger;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Need entity
 * 
 * @author Team Swiss Pandas
 * @author SWEN Faculty
 */
public class Need {
    private static final Logger LOG = Logger.getLogger(Need.class.getName());

    // Package private for tests
    static final String STRING_FORMAT = "Need [id=%d, name=%s, price=%f, quantity=%d/%d, description=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("type") private NeedType type;
    @JsonProperty("price") private double price;
    @JsonProperty("quantityNeeded") private int quantityNeeded;
    @JsonProperty("quantityFulfilled") private int quantityFulfilled;
    @JsonProperty("urgency") private UrgencyTag urgencyTag;
    @JsonProperty("urgencyImage") private String urgencyImage;
    @JsonProperty("description") private String description;
    @JsonProperty("image") private String image;
  
    /**
     * Create a need with the given id and name
     * @param id The id of the need
     * @param name The name of the need
     * @param type The type of the need
     * @param price The price of the need
     * @param quantity The quantity of the need
     * @param description The description of the need
     * @param urgencyTag The urgency of the need
     * @param image String image associated with need
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Need(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("type") NeedType type, @JsonProperty("price") double price, @JsonProperty("quantityNeeded") int quantityNeeded, @JsonProperty("quantityFulfilled") int quantityFulfilled, @JsonProperty("urgency") UrgencyTag urgencyTag, @JsonProperty("description") String description, @JsonProperty("image") String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantityNeeded = quantityNeeded;
        this.quantityFulfilled = quantityFulfilled;
        this.urgencyTag = urgencyTag;
        this.urgencyImage = urgencyTag.getImage();
        this.description = description;
        this.image = image;
    }
    
    /**
     * Retrieves the id of the need
     * @return The id of the need
     */
    public int getId() {return id;}

    /**
     * Sets the name of the need - necessary for JSON object to Java object deserialization
     * @param name The name of the need
     */
    public void setName(String name) {this.name = name;}

    /**
     * Sets the price of the need - necessary for JSON object to Java object deserialization
     * @param price The price of the need
     */
    public void setPrice(double price) {this.price = price;}

    /**
     * Sets the quantity needed of the need - necessary for JSON object to Java object deserialization
     * @param quantityNeeded The quantity of the need
     */
    public void setQuantityNeeded(int quantityNeeded) {this.quantityNeeded = quantityNeeded;}

     /**
     * Sets the quantity fulfilled of the need - necessary for JSON object to Java object deserialization
     * @param quantityNeeded The quantity fulfilled of the need
     */
    public void setQuantityFulfilled(int quantityFulfilled) {this.quantityFulfilled = quantityFulfilled;}

    /**
     * Sets the type of the need - necessary for JSON object to Java object deserialization
     * @param quantity The type of the need
     */
    public void setType(NeedType type) {this.type = type;}

    /**
     * Sets the urgency type of the need - necessary for JSON object to Java object deserialization
     * @param urgencyTag The urgency of the need
     */
    public void setUrgency(UrgencyTag urgencyTag) { this.urgencyTag = urgencyTag;}

    /**
     * Sets the urgency image of the need - necessary for JSON object to Java object deserialization
     * @param urgencyTag The urgency of the need
     */
    public void setUrgencyImage(String urgencyImage) { this.urgencyImage = urgencyImage;}

    /**
     * Retrieves the name of the need
     * @return The name of the need
     */
    public String getName() {return name;}
     
    /**
     * Retrieves the type of the need
     * @return The type of the need
     */
    public NeedType getType() {return type;}
    
    /**
     * Retrieves the price of the need
     * @return The price of the need
     */
    public double getPrice() {return price;}

    /**
     * Retrieves the quantity needed of the need
     * @return The quantity needed of the need
     */
    public int getQuantityNeeded() {return quantityNeeded;}

    /**
     * Retrieves the quantity fulfilled of the need
     * @return The quantity fulfilled of the need
     */
    public int getQuantityFulfilled() {return quantityFulfilled;}

    /**
     * Retries the urgency of the need
     * @return The urgency of the need
     */
    public UrgencyTag getUrgency() { return urgencyTag;}

    /**
     * Sets the description of the need - necessary for JSON object to Java object deserialization
     * @param description The description of the need
     */
    public void setDescription(String description) {this.description = description;}

    /**
     * Retrieves the description of the need
     * @return The description of the need
     */
    public String getDescription() {return description;}

    /**
     * Retreieves the image string representing the name of the image
     * @return The image string representing the image name
     */
    public String getImage() {return image;}

    /**
     * Sets the image string representing the name of the image
     * @param imageString The image string to set. It should match a predefined image name.
     */

    public void setImage(String image) {this.image = image;}

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,name,price,quantityNeeded,quantityFulfilled,description);
    }
}