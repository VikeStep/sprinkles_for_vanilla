package io.github.vikestep.sprinklesforvanilla.common.modules;

public interface IProperty
{
    enum Type
    {
        INT (false),
        STRING (false),
        DOUBLE (false),
        BOOLEAN (false),
        INT_LIST (true),
        STRING_LIST (true),
        DOUBLE_LIST (true),
        BOOLEAN_LIST (true);

        public final boolean isArray;

        Type(boolean isArray)
        {
            this.isArray = isArray;
        }
    }

    // Returns the default value of the property
    Object getDefault();

    // Returns the current value of the property
    Object getValue();

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

    // Returns whether or not a minecraft restart is needed to change this property
    boolean requiresMcRestart();
}
