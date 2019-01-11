package com.tvestergaard.start.logic.crud;

import com.tvestergaard.start.data.repositories.base.RepositoryEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Resource implements RepositoryEntity<Integer>
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String  name;
    private String  email;

    public Resource(String name, String email)
    {
        this.name = name;
        this.email = email;
    }

    @Override public Integer getId()
    {
        return this.id;
    }

    public Resource setId(Integer id)
    {
        this.id = id;
        return this;
    }

    public String getName()
    {
        return this.name;
    }

    public Resource setName(String name)
    {
        this.name = name;
        return this;
    }

    public String getEmail()
    {
        return this.email;
    }

    public Resource setEmail(String email)
    {
        this.email = email;
        return this;
    }

    @Override public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(id, resource.id) &&
               Objects.equals(name, resource.name) &&
               Objects.equals(email, resource.email);
    }

    @Override public int hashCode()
    {
        return Objects.hash(id, name, email);
    }

    @Override public String toString()
    {
        return "Resource{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}
