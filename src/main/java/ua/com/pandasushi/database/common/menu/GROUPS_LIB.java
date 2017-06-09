package ua.com.pandasushi.database.common.menu;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Тарас on 09.01.2017.
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(include = "all", usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "LIB_GROUPS")
public class GROUPS_LIB implements Serializable {
    @Id
    @Column(name = "GROUP_ID")
    private Integer group_id;

    @Column(name = "NAME")
    private String name;

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GROUPS_LIB{" +
                "group_id=" + group_id +
                ", name='" + name + '\'' +
                '}';
    }
}
