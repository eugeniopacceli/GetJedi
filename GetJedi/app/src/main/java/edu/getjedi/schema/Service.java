package edu.getjedi.schema;

/**
 * Created by Administrador on 14/06/2017.
 */

public class Service {
    private String professionalId;
    private String category;
    private String serviceName;
    private String description;
    private String hourlyPrice;

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHourlyPrice() {
        return hourlyPrice;
    }

    public void setHourlyPrice(String hourlyPrice) {
        this.hourlyPrice = hourlyPrice;
    }
}
