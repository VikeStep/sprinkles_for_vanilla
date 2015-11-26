package io.github.vikestep.sprinklesforvanilla.common.modules;

public interface IProperty
{
    enum Type
    {
        INT,
        STRING,
        DOUBLE,
        BOOLEAN,
        INTLIST,
        STRINGLIST,
        DOUBLELIST,
        BOOLEANLIST
    }

    // Returns the default value of the property
    Object getDefault();

    // Sets the value of the property
    void setValue(Object object);

    // Returns the type of the property
    Type getType();

    // Returns the name of the property (as seen in the config)
    String getName();

    // Returns the description of the property (as seen in the config)
    String getDescription();

    // Returns whether or not a world restart is needed to change this property
    boolean requiresWorldRestart();

    // Returns whether or not a minecraft restart is needed to change thie property
    boolean requiresMcRestart();
}
