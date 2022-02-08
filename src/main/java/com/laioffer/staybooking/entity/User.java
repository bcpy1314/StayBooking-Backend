package com.laioffer.staybooking.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "user")

// is to tell json to transform json to java object using builder constructor
@JsonDeserialize(builder = User.Builder.class)
public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    // fields
    @Id
    private String username;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private boolean enabled;

    //constructor
    public User(Builder builder){
        this.username = builder.username;
        this.password = builder.password;
        this.enabled = builder.enabled;
    }

    public User() { }

    //getter and setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static class Builder{
        @JsonProperty("username")// json syntax is snake-case
        private String username; // java syntax is camel-case
        // when transfer json to java, we need to use @JsonProperty

        @JsonProperty("password")
        private String password;

        @JsonProperty("enabled")
        private boolean enabled;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
    // Builder builder = new Builder();
    // builder.setName("vincent");
    // builder.setPassword("1234");
    // builder is good to use when there a lot of private fields in a class, but some of them
    // are optional for users to fill, so user could choose which field to fill, only one builder
    // constructor could handle all kinds of situations

}
