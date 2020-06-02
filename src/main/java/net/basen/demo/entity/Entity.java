package net.basen.demo.entity;

import java.util.Map;
import java.util.Set;

public interface Entity {
    // Returns entity name
    String getName();
    // Returns a unique identifier
    String getID();
    // Returns the sub-entities of this entity
    Set<Entity> getSubEntities();
    // Returns a set of key-value data belonging to this entity
    Map<String,String> getData();
}

