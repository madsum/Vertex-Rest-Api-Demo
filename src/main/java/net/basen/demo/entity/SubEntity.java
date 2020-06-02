package net.basen.demo.entity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class SubEntity implements Entity {

    private String ID;
    private String name;
    private int price;
    private Set<Entity> sbuEntities;

    public SubEntity(){
        ID = UUID.randomUUID().toString();
    }

    public SubEntity(String name, int price) {
        this.name = name;
        this.price = price;
        this.ID = UUID.randomUUID().toString();
    }

    public void setId(String id) {
        this.ID = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getID() {
        return ID;
    }

    public void addSubEntity(Entity entity){
        if(sbuEntities == null){
            sbuEntities = new HashSet<>();
        }
        sbuEntities.add(entity);
    };

    public Set<Entity> getSubEntities(){
        return sbuEntities;
    }

    public Map<String, String> getData() {
        if(sbuEntities == null){
            sbuEntities = new HashSet<>();
        }
        return sbuEntities.stream()
                .collect(Collectors.toMap(Entity::getID,
                        Object::toString));
    }

    public String toString() {
        return "SubEntity{" +
                "id='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
