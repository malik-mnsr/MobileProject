package com.hai811i.mobileproject.entity;

public class DrugReference {

    private Long id;
    private String name;      // e.g., "Doliprane"
    private String molecule;  // e.g., "Paracétamol"
    private String atcCode;   // e.g., "N02BE01"

    // No-arg constructor
    public DrugReference() {}

    // All-arg constructor
    public DrugReference(Long id, String name, String molecule, String atcCode) {
        this.id = id;
        this.name = name;
        this.molecule = molecule;
        this.atcCode = atcCode;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMolecule() {
        return molecule;
    }

    public void setMolecule(String molecule) {
        this.molecule = molecule;
    }

    public String getAtcCode() {
        return atcCode;
    }

    public void setAtcCode(String atcCode) {
        this.atcCode = atcCode;
    }
}
