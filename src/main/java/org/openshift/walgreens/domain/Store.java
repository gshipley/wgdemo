package org.openshift.walgreens.domain;

public class Store {

	private Object id;
    private Object position;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getPosition() {
        return position;
    }

    public void setPosition(Object position) {
        this.position = position;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(Object phone) {
        this.phone = phone;
    }

    public Object getHasClinic() {
        return hasClinic;
    }

    public void setHasClinic(Object hasClinic) {
        this.hasClinic = hasClinic;
    }

    public Object getIs24Hours() {
        return is24Hours;
    }

    public void setIs24Hours(Object is24Hours) {
        this.is24Hours = is24Hours;
    }

    private Object address;
    private Object phone;
    private Object hasClinic;
    private Object is24Hours;


}
