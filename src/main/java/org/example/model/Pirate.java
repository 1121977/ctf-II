package org.example.model;

import com.opencsv.bean.CsvBindByPosition;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Screw")
public class Pirate {
    @Id
    private long id;


    @CsvBindByPosition(position = 0)
    private String name;
    @CsvBindByPosition(position = 1)
    private String range;

    @CsvBindByPosition(position = 3)
    private String password;

    private String flag;

    public Pirate() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getFlag() {
        return flag;
    }

    public String getPassword() {
        return password;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return "" + id + " " + this.name + " " + this.range + " " + this.password;
    }
}
